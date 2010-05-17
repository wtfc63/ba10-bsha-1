package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;

import android.util.Log;


/**
 * Implementation of {@link IMicroGestureDetectionStrategy} that recognizes {@link MicroGesture}s by changes in curvature. 
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class MicroGestureDetectionStrategyCurvature extends BaseStrategy implements IMicroGestureDetectionStrategy {


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "Curvature";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Detect MicroGestures though curvature changes";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Detect {@link MicroGesture}s in the {@link TouchPoint}s of the given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures) {
		
		
		
		
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		Iterator<MicroGesture> itr = micro_gestures.iterator();
		while (itr.hasNext()) {
			MicroGesture mg = itr.next();
			ArrayList<TouchPoint> points = mg.getPoints();
			
			float tolerance = 230;
			MicroGesture curr_mg = new MicroGesture();
			
			// Remove points that are too close together
			TouchPoint[] temp = new TouchPoint[points.size()];
			temp = points.toArray(temp);
			ArrayList<TouchPoint> tempResult = new ArrayList<TouchPoint>();
			TouchPoint prev = temp[0];
			tempResult.add(temp[0]);
			for(int i = 1; i < temp.length; i++) {
				double dist = Math.sqrt(Math.pow(prev.x - temp[i].x, 2) + Math.pow(prev.y - temp[i].y, 2));	
				if(dist > 15) {
					tempResult.add(temp[i]);
					prev = temp[i];
				}
			}

			if ((points != null) && (tempResult.size() > 3)) {
				TouchPoint[] pts = new TouchPoint[tempResult.size()];
				pts = tempResult.toArray(pts);
				curr_mg.addPoint(pts[0]);
				float lastCurve = 0;
				for (int i = 1; i < pts.length-1; i++) {
					curr_mg.addPoint(pts[i]);
					float x1, y1;
					float x2, y2;
					x1 = pts[i-1].x - pts[i].x;
					y1 = pts[i-1].y - pts[i].y;
					x2 = pts[i+1].x - pts[i].x;
					y2 = pts[i+1].y - pts[i].y;
					float zn;		
					zn = x1 * y2 - y1 * x2;
					//Log.v(TAG, "Z: " + zn);
					if(lastCurve == 0) {
						lastCurve = zn;
					}
					else if(Math.abs(zn - lastCurve) > tolerance) {
						curr_mg.addPoint(pts[i+1]);
						
						setMicroGesture(curr_mg);
						curr_mg.setDirection(analyseMicroGestureDirection(curr_mg));
						result.add(curr_mg);
						
						curr_mg = new MicroGesture();
						//Log.v(TAG, "New Gesture: old:" + lastCurve + ", new:" + zn);
						lastCurve = 0;
					}
					else {
						//lastCurve = zn;
					}
				}
				curr_mg.addPoint(pts[pts.length -1]);
				
				setMicroGesture(curr_mg);
				curr_mg.setDirection(analyseMicroGestureDirection(curr_mg));
				result.add(curr_mg);
				
			} else {
				curr_mg = new MicroGesture(points);
				result.add(curr_mg);
			}
			
			/*ArrayList<MicroGesture> result2 = new ArrayList<MicroGesture>();
			result2.add(result.get(0));
			for (int i = 1; i < result.size(); i++) {
				MicroGesture previous = result.get(i-1);
				MicroGesture current = result.get(i);
				if (current.getPoints().size() > 3) {
					if (current.toString().equals(previous.toString())) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
					}
					else {
						result2.add(current);
					}
				}
				else {
					ArrayList<TouchPoint> list = current.getPoints();
					for (TouchPoint p : list) {
						previous.addPoint(p);
					}
				}
			}*/
		}
		
		Log.v("Curvature", "Number of Microgestures: " + result.size());
		return result;
	}


	//---------------------------------------------------------------------------
	// Helper methods
	//---------------------------------------------------------------------------
	

	private void setMicroGesture(MicroGesture mg) {
		ArrayList<TouchPoint> points = mg.getPoints();
		// calculate slope from first to last point
		TouchPoint a = points.get(0);
		TouchPoint b = points.get(points.size()-1);
		double m = (b.y - a.y) / (b.x - a.x);
		double q = a.y - m * a.x;
		
		// calculate if left or right, 
		// this will be decided on the point the farthest away from the line
		double max = 0;
		for(Iterator<TouchPoint> it = points.iterator(); it.hasNext(); ) {
			TouchPoint temp = it.next();
			double distance = calculateDistance(temp, m, q);
			if (Math.abs(distance) > Math.abs(max)) {
				max = distance;
			}
		}
		
		// Normalize distance by taking length of the curve into account
		max = max / Math.sqrt(Math.pow(b.y - a.y, 2) + Math.pow(b.x - a.x, 2));
		
		if (max < 0.15) {
			if (Math.sqrt(Math.pow(b.y - a.y, 2) + Math.pow(b.x - a.x, 2)) > 110) {
				mg.setType(MicroGesture.TYPE_LONG_LINE);
			}
			else {
				mg.setType(MicroGesture.TYPE_SHORT_LINE);
			}
		}
		else if (max < 0.5) {
			mg.setType(MicroGesture.TYPE_WIDE_CURVE);
		}
		else {
			mg.setType(MicroGesture.TYPE_NARROW_CURVE);
		}
		
		//Log.v(TAG, "y = " + m + "x + " + q + "; abstand: " + max);
		
		//
	}
	
	private double calculateDistance(TouchPoint a, double m1, double q1) {
		// Orthogonal line m2*x + q2
		double m2 = -1 / m1;
		
		// Calculate offset so the line goes through the point a
		double q2 = a.y - m2 * a.x;
		
		// Calculate crossing point between the two lines
		double x = (q2 - q1) / (m1 - m2);
		double y = m1 * x + q1;
		x = x - a.x;
		y = y - a.y;
		
		// Calculate the distance between point a and crossing point
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	private float analyseMicroGestureDirection(MicroGesture mg) {
		float result = 0;
		if (mg.getPoints().size() > 1) {
			TouchPoint first  = mg.getPoints().get(0);
			TouchPoint second = mg.getPoints().get(1);
			float dx = first.x - second.x;
			float dy = first.y - second.y;
			result = (float) (StrictMath.atan2(dy, dx));// - (Math.PI / 2));
		}
		return result;
	}
}

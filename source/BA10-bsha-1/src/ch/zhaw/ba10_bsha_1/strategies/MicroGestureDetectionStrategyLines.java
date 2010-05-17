package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.zhaw.ba10_bsha_1.*;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link IMicroGestureDetectionStrategy} which detects lines in the given {@link MicroGesture}s.
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class MicroGestureDetectionStrategyLines extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private double tolerance = 130;


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	@Override
	public void initialize() {}

	@Override
	protected String getStrategyName() {
		return "Lines";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Detect all straigt Lines and create MicroGesture";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Detect lines in the {@link TouchPoint}s of the given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
			
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		
		for(MicroGesture m : microGestures) {
			if (m.getType() != MicroGesture.TYPE_UNKNOWN) {
				result.add(m);
			}
			else {
				ArrayList<MicroGesture> tempResult = new ArrayList<MicroGesture>();
				
				ArrayList<TouchPoint> points = m.getPoints();
				MicroGesture curr_mg = new MicroGesture();
				
				if ((points != null) && (points.size() > 3)) {
					TouchPoint[] pts = new TouchPoint[points.size()];
					pts = points.toArray(pts);
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
							tempResult.add(curr_mg);
							
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
					tempResult.add(curr_mg);
					
				} else {
					curr_mg = new MicroGesture(points);
					tempResult.add(curr_mg);
				}	
				
				MicroGesture previous = tempResult.get(0);
				result.add(previous);
				for (int i = 1; i < tempResult.size(); i++) {
					MicroGesture current = tempResult.get(i);
					
					if (current.getType() == MicroGesture.TYPE_UNKNOWN && 
							previous.getType() == MicroGesture.TYPE_UNKNOWN) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
					}
					if (current.getType() == previous.getType() && 
							previous.getType() != MicroGesture.TYPE_UNKNOWN &&
							current.getDirection2() == previous.getDirection2()) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
					}
					else {
						result.add(current);
						previous = current;
					}
				}
			}
		}
		return result;
	}


	//---------------------------------------------------------------------------
	// Helper methods
	//---------------------------------------------------------------------------
	

	private void setMicroGesture(MicroGesture mg) {
		ArrayList<TouchPoint> points = mg.getPoints();
		// calculate slope from first to last point
		if (points.size() > 0) {
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
			
			if (max < 0.10) {
				if (Math.sqrt(Math.pow(b.y - a.y, 2) + Math.pow(b.x - a.x, 2)) > 140) {
					mg.setType(MicroGesture.TYPE_LONG_LINE);
				}
				else {
					mg.setType(MicroGesture.TYPE_SHORT_LINE);
				}
			}
			else {
				mg.setType(MicroGesture.TYPE_UNKNOWN);
			}
		}
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
			int size = mg.getPoints().size();
			TouchPoint first  = mg.getPoints().get(0);
			TouchPoint second = mg.getPoints().get(size-1);
			float dx = second.x - first.x;
			float dy = second.y - first.y;
			result = dy / dx;
			
			if (mg.getType() == MicroGesture.TYPE_SHORT_LINE || mg.getType() == MicroGesture.TYPE_LONG_LINE) {
				double tolerance = 0.8;
				if (result < (1-tolerance) && result > (-1 + tolerance)) {
					mg.setDirection2(2);
				}
				else if (result > 1-tolerance && result < 1 + 4*tolerance) {
					mg.setDirection2(1);
				}
				else if (result > -1-4*tolerance && result < -1 + tolerance) {
					mg.setDirection2(3);
				}
				else {
					mg.setDirection2(0);
				}
			}
			else if (mg.getType() == MicroGesture.TYPE_HALFCIRCLE) {
				if (dx < dy) {
					if (mg.getPoints().get(1).x < first.x) {
						mg.setDirection2(0);
					}
					else if (mg.getPoints().get(1).x > first.x) {
						mg.setDirection2(2);
					}
					else {
						mg.setDirection2(-1);
					}
				}
				else if (dx > dy) {
					if (mg.getPoints().get((int)size/2).y < first.y) {
						mg.setDirection2(1);
					}
					else if (mg.getPoints().get((int)size/2).y > first.y) {
						mg.setDirection2(3);
					}
					else {
						mg.setDirection2(-1);
					}
				}
			}
			else {
				mg.setDirection2(-1);
			}
		}
		return result;
	}

}

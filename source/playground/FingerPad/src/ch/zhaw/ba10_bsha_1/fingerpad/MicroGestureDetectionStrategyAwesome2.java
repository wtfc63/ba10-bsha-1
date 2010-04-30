package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import android.util.Log;

public class MicroGestureDetectionStrategyAwesome2 implements IMicroGestureDetectionStrategy {
	private static final String TAG = "AwesomeStrategy2"; 
	
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points) {
		
		float tolerance = 20;
		
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		MicroGesture curr_mg = new MicroGesture();

		if ((points != null) && (points.size() > 3)) {
			TouchPoint[] pts = new TouchPoint[points.size()];
			pts = points.toArray(pts);
			curr_mg.addPoint(pts[0]);
			float lastCurve = 0;
			float firstCurve = 0;
			float accumulator = 0;
			Map<Integer, Float> accValues = new TreeMap<Integer, Float>();
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
				accumulator += zn;
				
				accValues.put(i, accumulator);

				//Log.v(TAG, "Acc: " + accumulator);
				/*if (firstCurve == 0) {
					firstCurve = zn;
				}
				if(lastCurve == 0) {
					lastCurve = zn;
				}
				else if(zn * firstCurve < 0 && Math.abs(zn - lastCurve) > tolerance) {
					curr_mg.addPoint(pts[i+1]);
					
					setMicroGesture(curr_mg);
					curr_mg.setDirection(analyseMicroGestureDirection(curr_mg));
					result.add(curr_mg);
					
					curr_mg = new MicroGesture();
					//Log.v(TAG, "New Gesture: old:" + lastCurve + ", new:" + zn);
					lastCurve = 0;
					firstCurve = 0;
				}
				else {
					lastCurve = zn;
				}*/
			}
			float min = Float.MAX_VALUE;
			float max = Float.MIN_VALUE;
			int status = -1;
			ArrayList<Integer> breaks = new ArrayList<Integer>();
			curr_mg = new MicroGesture();
			for (Map.Entry<Integer, Float> a : accValues.entrySet()) {
				curr_mg.addPoint(pts[a.getKey()]);
				//Log.v(TAG, "ID: " + a.getKey() + " Value: " + a.getValue());
				if (status == -1) {
					min = a.getValue();
					max = a.getValue();
					status = 0;
				}
				else if (status == 0) {
					if (a.getValue() < min) {
						status = 1;
					}
					else if(a.getValue() > max) {
						status = 2;
					}
				}
				
				if (status == 1) {
					if (a.getValue() <= (min+15)) {
						min = a.getValue();
					}
					else {
						setMicroGesture(curr_mg);
						analyseMicroGestureDirection(curr_mg);
						result.add(curr_mg);
						curr_mg = new MicroGesture();
						//min = Float.MAX_VALUE;
						//max = Float.MIN_VALUE;
						status = -1;
					}
				}
				else if (status == 2) {
					if (a.getValue() >= (max-15)) {
						max = a.getValue();
					}
					else {
						setMicroGesture(curr_mg);
						analyseMicroGestureDirection(curr_mg);
						result.add(curr_mg);
						curr_mg = new MicroGesture();
						//max = Float.MIN_VALUE;
						//min = Float.MAX_VALUE;
						status = -1;
					}
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
		
		Log.v("Curvature", "Number of Microgestures: " + result.size());
		return result;
	}

	public boolean validateMicroGesture(MicroGesture microGesture) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "Curvature";
	}
	
	public void setMicroGesture(MicroGesture mg) {
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
			
			if (max < 0.15) {
				if (Math.sqrt(Math.pow(b.y - a.y, 2) + Math.pow(b.x - a.x, 2)) > 110) {
					mg.setType(MicroGesture.TYPE_LONG_LINE);
				}
				else {
					mg.setType(MicroGesture.TYPE_SHORT_LINE);
				}
			}
			else {
				if (Math.abs(a.x - b.x) < 50 && Math.abs(a.y - b.y) < 50 ) {
					mg.setType(MicroGesture.TYPE_CIRCLE);
				}
				else {
					mg.setType(MicroGesture.TYPE_HALFCIRCLE);
				}
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
	
	public float analyseMicroGestureDirection(MicroGesture mg) {
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

	@Override
	public void setFieldHeight(float field_height) {
		//fieldHeight = field_height;
	}
}

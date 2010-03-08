package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.util.Log;

public class MicroGestureDetectionStrategyCurvature implements IMicroGestureDetectionStrategy {
	private static final String TAG = "GestureTest"; 
	
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points) {
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		MicroGesture curr_mg = new MicroGesture();
		if ((points != null) && (points.size() > 2)) {
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
				else if(Math.abs(zn - lastCurve) > 150) {
					curr_mg.addPoint(pts[i+1]);
					setMicroGesture(curr_mg);
					result.add(curr_mg);
					curr_mg = new MicroGesture();
					//Log.v(TAG, "New Gesture: old:" + lastCurve + ", new:" + zn);
					lastCurve = 0;
				}
			}
			curr_mg.addPoint(pts[pts.length -1]);
			
			setMicroGesture(curr_mg);
			result.add(curr_mg);
		} else {
			curr_mg = new MicroGesture(points);
			result.add(curr_mg);
		}
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
	
	void setMicroGesture(MicroGesture mg) {
		ArrayList<TouchPoint> points = mg.getPoints();
		// calculate slope from first to last point
		TouchPoint a = points.get(0);
		TouchPoint b = points.get(points.size()-1);
		double m = (b.y - a.y) / (b.x - a.x);
		double q = a.y - m * a.x;
		
		// calculate if left or right, 
		// this will be decided on the point the furthest away from the line
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
		
		if (max < 0.1) {
			mg.setType(MicroGesture.TYPE_SHORT_LINE);
		}
		else if (max < 0.5) {
			mg.setType(MicroGesture.TYPE_WIDE_CURVE);
		}
		else {
			mg.setType(MicroGesture.TYPE_NARROW_CURVE);
		}
		
		Log.v(TAG, "y = " + m + "x + " + q + "; abstand: " + max);
		
		//
	}
	
	double calculateDistance(TouchPoint a, double m1, double q1) {
		double m2 = -1 / m1;
		double q2 = a.y - m2 * a.x;
		double x = (q2 - q1) / (m1 - m2);
		double y = m1 * x + q1;
		x = x - a.x;
		y = y - a.y;
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}

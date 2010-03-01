package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;

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
					result.add(curr_mg);
					curr_mg = new MicroGesture();
					Log.v(TAG, "New Gesture: old:" + lastCurve + ", new:" + zn);
					lastCurve = 0;
				}
			}
			curr_mg.addPoint(pts[pts.length -1]);
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
}

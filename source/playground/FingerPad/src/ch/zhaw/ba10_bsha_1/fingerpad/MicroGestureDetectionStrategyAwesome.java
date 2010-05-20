package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.util.Log;

public class MicroGestureDetectionStrategyAwesome implements IMicroGestureDetectionStrategy {
	private static final String TAG = "AwesomeStrategy"; 
	
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points) {
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		MicroGesture temp = new MicroGesture();
		
		// Check for circles
		TouchPoint[] tPoints = new TouchPoint[points.size()];
		tPoints = points.toArray(tPoints);
		
		ArrayList<TouchPoint> tempPoints = new ArrayList<TouchPoint>();
		
		for (int i = 0; i < tPoints.length; i++) {
			TouchPoint origin = tPoints[i];
			tempPoints.add(origin);
			for (int j = i+5; j < tPoints.length; j++) {
				if (Math.abs(origin.x - tPoints[j].x) < 10 && Math.abs(origin.y - tPoints[j].y) < 10) {
					MicroGesture newGesture = new MicroGesture(tempPoints);
					newGesture.setType(MicroGesture.TYPE_UNKNOWN);
					result.add(newGesture);
					tempPoints = new ArrayList<TouchPoint>();
					tempPoints.add(tPoints[j]);
					
					// Circle
					newGesture = new MicroGesture();
					for (int z = i; z <= j; z++) {
						newGesture.addPoint(tPoints[z]);
					}
					newGesture.setType(MicroGesture.TYPE_CIRCLE);
					result.add(newGesture);
					i = j;
	
					break;
				}
			}
			
		}
		MicroGesture newGesture = new MicroGesture(tempPoints);
		result.add(newGesture);

		ArrayList<MicroGesture> remove = new ArrayList<MicroGesture>();
		for (MicroGesture m : result) {
			// TODO CHECK THIS TOLERANCE (SHOULD BE IMPROVED)
			if (m.getPoints().size() < 5) {
				remove.add(m);
			}
		}
		result.removeAll(remove);
		
		/// Check for lines
		ArrayList<MicroGesture> thirdList = new ArrayList<MicroGesture>();
		for (MicroGesture m : result) {
			if (m.getType() == MicroGesture.TYPE_CIRCLE) {
				thirdList.add(m);
			}
			else {
				IMicroGestureDetectionStrategy second = new MicroGestureDetectionStrategyAwesome3();
				thirdList.addAll(second.detectMicroGestures(m.getPoints()));
			}
		}
		
		/// Cut non-lines into half-circles
		ArrayList<MicroGesture> secondList = new ArrayList<MicroGesture>();
		for (MicroGesture m : thirdList) {
			if (m.getType() == MicroGesture.TYPE_CIRCLE || m.getType() == MicroGesture.TYPE_LONG_LINE || m.getType() == MicroGesture.TYPE_SHORT_LINE) {
				secondList.add(m);
			}
			else {
				IMicroGestureDetectionStrategy second = new MicroGestureDetectionStrategyAwesome2();
				secondList.addAll(second.detectMicroGestures(m.getPoints()));
			}
		}
		 return secondList;
		//return result;
	}

	public boolean validateMicroGesture(MicroGesture microGesture) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return TAG;
	}


	@Override
	public void setFieldHeight(float field_height) {
		//fieldHeight = field_height;
	}
	
	double calculateAngleRadians(TouchPoint p1, TouchPoint p2, TouchPoint p3) {
		float x1, y1;
		float x2, y2;
		x1 = p1.x - p2.x;
		y1 = p1.y - p2.y;
		x2 = p3.x - p2.x;
		y2 = p3.y - p2.y;
		
		//Winkel
		double cos = (x1 * x2 + y1 * y2) / (Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));
		
		return Math.acos(cos);
	}
}

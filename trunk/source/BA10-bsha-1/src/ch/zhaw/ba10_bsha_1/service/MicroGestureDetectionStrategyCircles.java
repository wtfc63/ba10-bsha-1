package ch.zhaw.ba10_bsha_1.service;

import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.*;

public class MicroGestureDetectionStrategyCircles implements
		IMicroGestureDetectionStrategy {

	@Override
	public Collection<MicroGesture> detectMicroGestures(
			Collection<MicroGesture> microGestures) {
		
		// Check for circles
		
		Collection<MicroGesture> result = new ArrayList<MicroGesture>();
		
		for (MicroGesture m : microGestures) {
			
			ArrayList<TouchPoint> points = m.getPoints();
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
		}
		return result;
	}

	@Override
	public void setFieldHeight(float fieldHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "Circle Detection: Creating a MicroGesture for every circle.";
	}
	
}
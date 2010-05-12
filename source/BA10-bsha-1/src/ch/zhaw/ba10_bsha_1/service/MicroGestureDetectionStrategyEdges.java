package ch.zhaw.ba10_bsha_1.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import ch.zhaw.ba10_bsha_1.TouchPoint;

public class MicroGestureDetectionStrategyEdges 
		implements IMicroGestureDetectionStrategy {
	
	private double tolerance = 0.2d;

	@Override
	public Collection<MicroGesture> detectMicroGestures(
			Collection<MicroGesture> microGestures) {
		
		Collection<MicroGesture> result = new ArrayList<MicroGesture>();
		
		for (MicroGesture m : microGestures) {
			Collection<TouchPoint> points = m.getPoints();
			TouchPoint[] pts = new TouchPoint[points.size()];
			pts = points.toArray(pts);
			
			double lastCurve = 0;
			MicroGesture temp2 = new MicroGesture();
			temp2.addPoint(pts[0]);
			for (int i = 1; i < pts.length-1; i++) {
				temp2.addPoint(pts[i]);
				float x1, y1;
				float x2, y2;
				x1 = pts[i-1].x - pts[i].x;
				y1 = pts[i-1].y - pts[i].y;
				x2 = pts[i+1].x - pts[i].x;
				y2 = pts[i+1].y - pts[i].y;
				
				//Winkel
				double cos = (x1 * x2 + y1 * y2) / 
					(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));
	
				if(lastCurve == 0) {
					lastCurve = cos;
				}
				else if(cos > -0.1 && Math.abs(cos - lastCurve) > tolerance) {
					temp2.addPoint(pts[i]);
	
					result.add(temp2);
					
					if (i != pts.length-1) {
						temp2 = new MicroGesture();
						temp2.addPoint(pts[i]);
					}
					lastCurve = 0;
				}
				else {
					lastCurve = cos;
				}
			}
			temp2.addPoint(pts[pts.length -1]);
			result.add(temp2);
		}
		
		return result;
	}

	@Override
	public void setFieldHeight(float fieldHeight) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		return "Edge Detection: Separating the path at sharp edges.";
	}

}

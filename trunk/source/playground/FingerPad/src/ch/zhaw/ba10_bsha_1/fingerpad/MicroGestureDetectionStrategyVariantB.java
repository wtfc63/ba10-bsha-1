package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class MicroGestureDetectionStrategyVariantB implements IMicroGestureDetectionStrategy {
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points) {
		// Disclaimer: Grober Hack
		// Point normalizing

		///// Normalize Point distances /////
		TouchPoint[] temp = new TouchPoint[points.size()];
		temp = points.toArray(temp);
		ArrayList<TouchPoint> normalizedPoints = new ArrayList<TouchPoint>();
		TouchPoint prev = temp[0];
		normalizedPoints.add(temp[0]);
		for(int i = 1; i < temp.length; i++) {
			double dist = Math.sqrt(Math.pow(prev.x - temp[i].x, 2) + Math.pow(prev.y - temp[i].y, 2));	
			// Remove Points too close together
			if(dist > 15 && dist < 50) {
				normalizedPoints.add(temp[i]);
				prev = temp[i];
			}
			// Add Points if too far from each other
			else if(dist > 50) {
				TouchPoint newp = new TouchPoint((temp[i].x + prev.x)/2f, (temp[i].y + prev.y)/2f);
				normalizedPoints.add(newp);
				normalizedPoints.add(temp[i]);
				prev = temp[i];
				//prev = newp;
			}
		}
		points = new ArrayList<TouchPoint>(normalizedPoints);
		////// End of normalizing Points distances /////
		

		///// Edge detection /////
		Vector<Vector<TouchPoint>> lines = new Vector<Vector<TouchPoint>>();
		
		
		points = new ArrayList<TouchPoint>((new PathSmoothingStrategySpline()).smoothePath(points));
		
		double tolerance = 0.2d;
		TouchPoint[] pts = new TouchPoint[points.size()];
		pts = points.toArray(pts);
		double lastCurve = 0;
		Vector<TouchPoint> temp2 = new Vector<TouchPoint>();
		temp2.add(pts[0]);
		for (int i = 1; i < pts.length-1; i++) {
			temp2.add(pts[i]);
			float x1, y1;
			float x2, y2;
			x1 = pts[i-1].x - pts[i].x;
			y1 = pts[i-1].y - pts[i].y;
			x2 = pts[i+1].x - pts[i].x;
			y2 = pts[i+1].y - pts[i].y;
			
			//Winkel
			double cos = (x1 * x2 + y1 * y2) / (Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));

			if(lastCurve == 0) {
				lastCurve = cos;
			}
			else if(cos > -0.1 && Math.abs(cos - lastCurve) > tolerance) {
				temp2.add(pts[i]);

				lines.add(temp2);
				
				if (i != pts.length-1) {
					temp2 = new Vector<TouchPoint>();
					temp2.add(pts[i]);
				}
				lastCurve = 0;
			}
			else {
				lastCurve = cos;
			}
		}
		temp2.add(pts[pts.length -1]);
		lines.add(temp2);
		
		/// ENDE EDGE DETECTION ///
		
		ArrayList<TouchPoint> tempPoints = new ArrayList<TouchPoint>();
		
		ArrayList<MicroGesture>microGestures = new ArrayList<MicroGesture>();
		for(Vector<TouchPoint> v : lines) {
			///// SMOOTHING //////
			//points = new ArrayList<TouchPoint>(new PathSmoothingStrategySpline().smoothePath(v));
			points = new ArrayList<TouchPoint>(v);
			tempPoints.addAll(points);
			///// ENDE SMOOTHING /////
			
			///// MICRO GESTURE DETECTION /////
			IMicroGestureDetectionStrategy strategy = new MicroGestureDetectionStrategyAwesome();
			ArrayList<MicroGesture> tempChars = new ArrayList<MicroGesture>(strategy.detectMicroGestures(points));
			microGestures.addAll(tempChars);
			///// END MICROGESTURE DETECTION /////
		}
		points = tempPoints;
		
		
		///// COMBINE IDENTICAL MICRO GESTURES ////
		
		/*ArrayList<MicroGesture> result = new ArrayList<MicroGesture>(microGestures);
		microGestures = new ArrayList<MicroGesture>();
		
		if (result.size() > 0) {
			microGestures.add(result.get(0));
			MicroGesture previous = result.get(0);
			for (int i = 1; i < result.size(); i++) {
				MicroGesture current = result.get(i);
				if (current.getPoints().size() > 4) {
					if (current.getType() == previous.getType() && current.getDirection2() == previous.getDirection2()) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						new MicroGestureDetectionStrategyAwesome2().analyseMicroGestureDirection(previous);
					}
					else if ((current.getType() == MicroGesture.TYPE_LONG_LINE || 
								current.getType() == MicroGesture.TYPE_SHORT_LINE) 
							&& (previous.getType() == MicroGesture.TYPE_LONG_LINE ||
									previous.getType() == MicroGesture.TYPE_SHORT_LINE)) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						new MicroGestureDetectionStrategyAwesome2().setMicroGesture(previous);
						new MicroGestureDetectionStrategyAwesome2().analyseMicroGestureDirection(previous);
					
					}
					else if ((current.getType() == MicroGesture.TYPE_HALFCIRCLE && previous.getType() == MicroGesture.TYPE_SHORT_LINE &&
								current.getDirection2() == 3 && previous.getDirection2() == 0) ) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						previous.setType(MicroGesture.TYPE_HALFCIRCLE);
						new MicroGestureDetectionStrategyAwesome2().analyseMicroGestureDirection(previous);

					}
					else {
						microGestures.add(current);
						previous = current;
					}
				}
				else {
					ArrayList<TouchPoint> list = current.getPoints();
					for (TouchPoint p : list) {
						previous.addPoint(p);
					}
				}
			}
		}*/
		
		//// END OF COMBINE ////
		return microGestures;
	}

	public boolean validateMicroGesture(MicroGesture microGesture) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "Variante B";
	}


	@Override
	public void setFieldHeight(float field_height) {
		//fieldHeight = field_height;
	}
}

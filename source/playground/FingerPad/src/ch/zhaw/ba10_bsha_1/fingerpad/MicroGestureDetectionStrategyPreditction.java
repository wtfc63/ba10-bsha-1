package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;


public class MicroGestureDetectionStrategyPreditction implements IMicroGestureDetectionStrategy {

	
	private float predictionTolerance = 5;
	
	
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<TouchPoint> points) {
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		MicroGesture curr_mg = new MicroGesture();
		if ((points != null) && (points.size() > 0)) {
			TouchPoint[] pts = new TouchPoint[points.size()];
			pts = points.toArray(pts);
			boolean first = true;
			TouchPoint prev = null;
			TouchPoint curr;
			TouchPoint next;
			for (int i = 0; i < pts.length; i++) {
				curr = pts[i];
				if (first) {
					first = false;
					curr_mg.addPoint(curr);
					prev = curr;
				} else if (i < (pts.length - 2)) {
					//prev = pts[i - 1];
					next = pts[i + 1];
					float pred_x = curr.x + (curr.x - prev.x);
					float pred_y = curr.y + (curr.y - prev.y);
					if ((Math.abs(next.x - pred_x) > predictionTolerance) 
							&& (Math.abs(next.y - pred_y) > predictionTolerance)) {
						curr_mg.addPoint(next);
						prev = curr;
						result.add(curr_mg);
						curr_mg.setDirection(analyseMicroGestureDirection(curr_mg));
						curr_mg = new MicroGesture();
						first = true;
					} else if (curr.distanceTo(prev) >= (2 * predictionTolerance)) {
						curr_mg.addPoint(curr);
						prev = curr;
					}
				} else {
					curr_mg.addPoint(curr);
					curr_mg.setDirection(analyseMicroGestureDirection(curr_mg));
					result.add(curr_mg);
				}
			}
		} else {
			curr_mg = new MicroGesture(points);
			result.add(curr_mg);
		}
		return result;
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

	@Override
	public boolean validateMicroGesture(MicroGesture microGesture) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "Prediction";
	}
}

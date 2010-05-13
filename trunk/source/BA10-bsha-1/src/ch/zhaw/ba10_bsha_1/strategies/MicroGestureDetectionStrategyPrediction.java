package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.zhaw.ba10_bsha_1.StrategyArgument;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class MicroGestureDetectionStrategyPrediction extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	private float predictionTolerance = 5;
	private float field_height = 0;
	private boolean mergeMicroGestures = true;

	
	protected void initArguments() {
		addArgument(new StrategyArgument(getStrategyName(), "FieldHeight", "Height of input field"));
	}

	@Override
	protected String getStrategyName() {
		return "Prediction";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Detect MicroGestures by predicting the expected position of the next point";
	} 
	
	
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> micro_gestures) {
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		Iterator<MicroGesture> itr = micro_gestures.iterator();
		while (itr.hasNext()) {
			MicroGesture mg = itr.next();
			ArrayList<TouchPoint> points = mg.getPoints();
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
							prev = curr;
							curr_mg.addPoint(next);
							curr_mg = analyseMicroGesture(result, curr_mg);
							if (!result.contains(curr_mg)) {
								result.add(curr_mg);
							}
							curr_mg = new MicroGesture();
							first = true;
						} else if (curr.distanceTo(prev) >= (2 * predictionTolerance)) {
							curr_mg.addPoint(curr);
							prev = curr;
						}
					} else {
						curr_mg.addPoint(curr);
						curr_mg = analyseMicroGesture(result, curr_mg);
						if (!result.contains(curr_mg)) {
							result.add(curr_mg);
						}
					}
				}
			} else {
				curr_mg = new MicroGesture(points);
				result.add(curr_mg);
			}
		}
		return result;
	}
	
	private MicroGesture analyseMicroGesture(ArrayList<MicroGesture> list, MicroGesture mg) {
		mg.setDirection(analyseMicroGestureDirection(mg));
		mg.setType(analyseMicroGestureType(mg));
		mg.enableStubMerging(true);
		if (mergeMicroGestures && (list.size() > 0)) {
			MicroGesture last = list.get(list.size() - 1);
			if ((last != null) && last.canMergeWith(mg)) {
				mg = last.merge(mg);
			}
		}
		return mg;
	}
	
	private float analyseMicroGestureDirection(MicroGesture mg) {
		float direction = 0;
		if (mg.getPoints().size() > 1) {
			TouchPoint first  = mg.getPoints().get(0);
			TouchPoint second = mg.getPoints().get(1);
			float dx = first.x - second.x;
			float dy = first.y - second.y;
			direction = (float) (Math.atan2(dy, dx));
		}
		return direction;
	}
	
	private int analyseMicroGestureType(MicroGesture mg) {
		int type = MicroGesture.TYPE_UNKNOWN;
		TouchPoint[] pts = new TouchPoint[mg.getPoints().size()];
		mg.getPoints().toArray(pts);
		int nop = pts.length; //number of points
		float mg_length = mg.getLength();
		float start_end_distance = pts[0].distanceTo(pts[nop - 1]);
		if ((nop < 3) || (mg_length <= (start_end_distance + predictionTolerance))) {
			if (field_height > 0) {
				if ((start_end_distance - predictionTolerance) <= (field_height / 3)) {
					type = MicroGesture.TYPE_SHORT_LINE;
				} else {
					type = MicroGesture.TYPE_LONG_LINE;
				}
			} else {
				type = MicroGesture.TYPE_LONG_LINE;
			}
		} else {
			type = (calcAngle(pts[0], pts[1], pts[2]) < 2)
					? MicroGesture.TYPE_NARROW_CURVE : MicroGesture.TYPE_WIDE_CURVE;
		}
		return type;
	}
	
	private float calcAngle(TouchPoint first, TouchPoint second, TouchPoint third) {
		float a = first.distanceTo(second);
		float b = second.distanceTo(third);
		float c = first.distanceTo(third);
		return ((float) Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b)));
	}
}

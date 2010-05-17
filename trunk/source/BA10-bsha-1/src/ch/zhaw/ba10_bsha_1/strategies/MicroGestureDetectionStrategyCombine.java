package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.*;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link IMicroGestureDetectionStrategy} which combines {@link MicroGesture} with others when needed.
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class MicroGestureDetectionStrategyCombine extends BaseStrategy implements IMicroGestureDetectionStrategy {


	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	public void initialize() {}

	@Override
	protected String getStrategyName() {
		return "Combine";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Combine MicroGestures where applicable";
	}


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Combine {@link MicroGesture}s when needed
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
		
		ArrayList<MicroGesture> tempGestures = new ArrayList<MicroGesture>(microGestures);
		ArrayList<MicroGesture> result = new ArrayList<MicroGesture>();
		
		if (tempGestures.size() > 0) {
			result.add(tempGestures.get(0));
			MicroGesture previous = tempGestures.get(0);
			for (int i = 1; i < tempGestures.size(); i++) {
				MicroGesture current = tempGestures.get(i);
				if (current.getPoints().size() > 4) {
					if (current.getType() == previous.getType() && current.getDirection() == previous.getDirection() && current.getType() == MicroGesture.TYPE_SHORT_LINE) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						setMicroGesture(previous);
					}
					else {
						result.add(current);
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
		}
		return result;
	}
	
	private void setMicroGesture(MicroGesture mg) {
		ArrayList<TouchPoint> points = mg.getPoints();

		if (points.size() > 0) {
			TouchPoint a = points.get(0);
			TouchPoint b = points.get(points.size()-1);

			if (Math.sqrt(Math.pow(b.y - a.y, 2) + Math.pow(b.x - a.x, 2)) > 140) {
				mg.setType(MicroGesture.TYPE_LONG_LINE);
			}
			else {
				mg.setType(MicroGesture.TYPE_SHORT_LINE);
			}
		}
	}
}

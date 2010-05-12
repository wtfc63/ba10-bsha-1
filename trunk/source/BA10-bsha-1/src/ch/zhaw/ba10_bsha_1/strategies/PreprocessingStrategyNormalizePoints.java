package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


public class PreprocessingStrategyNormalizePoints extends BaseStrategy implements IPreprocessingStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "NormalizePoints";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Normalize Point-to-Point distancesR";
	} 
	
	
	@Override
	public MicroGesture process(MicroGesture microGesture) {
		Collection<TouchPoint> points = microGesture.getPoints();
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
			}
		}
		
		microGesture.setPoints(normalizedPoints);
	
		return null;
	}
}

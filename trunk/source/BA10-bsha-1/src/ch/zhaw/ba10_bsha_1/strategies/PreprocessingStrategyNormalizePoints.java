package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link IPreprocessingStrategy} that normalizes the points of the path 
 * of the given {@link TouchPoint}s.
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class PreprocessingStrategyNormalizePoints extends BaseStrategy implements IPreprocessingStrategy {
	
	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private float tolerance = 15;

	
	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "NormalizePoints";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Normalize Point-to-Point distancesR";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IPreprocessingStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Process the {@link TouchPoint}s of the given {@link MicroGesture}
	 * 
	 * @param micro_gesture
	 * @return
	 */
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
			if(dist > tolerance && dist < 2*tolerance) {
				normalizedPoints.add(temp[i]);
				prev = temp[i];
			}
			
			// Add Points if too far from each other
			else if(dist >= 2*tolerance) {

				int pointNo = (int)(dist / tolerance) - 1;
				float m = (temp[i].y - prev.y) / (temp[i].x - prev.x);
				float dx = (temp[i].x - prev.x) / (pointNo + 1);
				for (int j = 1; j <= pointNo; j++) {
					float x = prev.x + j*dx;
					float y = m * j * dx + prev.y;
					normalizedPoints.add(new TouchPoint(x, y));
				}
				normalizedPoints.add(temp[i]);
				prev = temp[i];
			}
		}
		microGesture.setPoints(normalizedPoints);
		return microGesture;
	}
}

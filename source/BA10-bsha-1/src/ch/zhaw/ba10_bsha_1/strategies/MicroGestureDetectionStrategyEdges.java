package ch.zhaw.ba10_bsha_1.strategies;


import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.MicroGesture;


/**
 * Implementation of {@link IMicroGestureDetectionStrategy} which detects edges in the paths of the {@link TouchPoint}s 
 * of the given {@link MicroGesture}s.
 * 
 * @author Dominik Giger, Julian Hanhart
 */
public class MicroGestureDetectionStrategyEdges extends BaseStrategy implements IMicroGestureDetectionStrategy {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private double tolerance = 0.2d;
	private double angleCosinus = -0.1d; // Enger Winkel

	//---------------------------------------------------------------------------
	// Implementation of BaseStrategy's abstract methods
	//---------------------------------------------------------------------------
	
	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "Edges";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Separate the paths of the given MicroGestures at sharp edges";
	} 


	//---------------------------------------------------------------------------
	// Implementation of IMicroGestureDetectionStrategy
	//---------------------------------------------------------------------------
	

	/**
	 * Detect edges in the {@link TouchPoint}s of the given {@link MicroGesture}s
	 * 
	 * @param micro_gestures
	 * @return
	 */
	@Override
	public Collection<MicroGesture> detectMicroGestures(Collection<MicroGesture> microGestures) {
		
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
				else if(cos > angleCosinus && Math.abs(cos - lastCurve) > tolerance) {
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
}

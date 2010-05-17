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
			
			////// TEST
			if (microGestures.size() == 1 && pts.length < 5) {
				MicroGesture dot = new MicroGesture();
				dot.setPoints(points);
				dot.setType(MicroGesture.TYPE_DOT);
				result.add(dot);
				break;
			}
			
			///// END TEST
			
			double lastCos = 0;
			MicroGesture newGesture = new MicroGesture();
			newGesture.addPoint(pts[0]);
			for (int i = 1; i < pts.length-1; i++) {
				newGesture.addPoint(pts[i]);
				float x1, y1;
				float x2, y2;
				x1 = pts[i-1].x - pts[i].x;
				y1 = pts[i-1].y - pts[i].y;
				x2 = pts[i+1].x - pts[i].x;
				y2 = pts[i+1].y - pts[i].y;
				
				//Winkel
				double cos = (x1 * x2 + y1 * y2) / (Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));
	
				if(lastCos == 0) {
					lastCos = cos;
				}
				else if(cos > angleCosinus && Math.abs(cos - lastCos) > tolerance) {
					newGesture.addPoint(pts[i]);
	
					result.add(newGesture);
					
					if (i != pts.length-1) {
						newGesture = new MicroGesture();
						newGesture.addPoint(pts[i]);
					}
					lastCos = 0;
				}
				else {
					lastCos = cos;
				}
			}
			newGesture.addPoint(pts[pts.length -1]);
			result.add(newGesture);
		}
		
		return result;
	}
}

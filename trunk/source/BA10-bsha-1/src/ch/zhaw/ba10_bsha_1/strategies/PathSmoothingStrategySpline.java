package ch.zhaw.ba10_bsha_1.service;

import java.util.ArrayList;
import java.util.Vector;

import ch.zhaw.ba10_bsha_1.TouchPoint;


public class PathSmoothingStrategySpline extends BaseStrategy implements IPreprocessingStrategy {

	
	protected void initArguments() {}

	@Override
	protected String getStrategyName() {
		return "Spline";
	}
	
	@Override
	protected String getStrategyDescription() {
		return "Smooth by calculating spline";
	} 
	
	
	public MicroGesture process(MicroGesture micro_gesture) {
		ArrayList<TouchPoint> points = micro_gesture.getPoints();
		
		Vector<TouchPoint> result = new Vector<TouchPoint>();

			
		int nCurvePoints = (int) 2 * points.size();
		
		TouchPoint[] controlPoints = new TouchPoint[points.size()];
		controlPoints = points.toArray(controlPoints);
		int nControlPoints = points.size();
		int d = 4;
		
		float[] weights = new float[nControlPoints];
		for(int i=0; i < nControlPoints; i++) {
			if (i == 0 || i == nControlPoints-1) {
				weights[i] = 1f;
			}
			else {
				weights[i] = 1f;
			}
			
		}
		
		float[] knot = new float[nControlPoints + d];
		for(int i=0; i < (nControlPoints + d); i++) {
			knot[i] =  i*1f;
		}
	
		TouchPoint[] curvePoints = NURBS(controlPoints, nCurvePoints, knot, weights, nControlPoints, d);
		//TouchPoint[] curvePoints = controlPoints;
		
		result.add(controlPoints[0]);
		//result.add(new TouchPoint(0, 0));
		for (int i = 1; i < curvePoints.length; i++) {
			result.add(curvePoints[i]);
		}
		for (int i = curvePoints.length; i < controlPoints.length; i++) {
			result.add(controlPoints[i]);
		}
		
		return (new MicroGesture(result));
	}
	
	public TouchPoint[] NURBS(TouchPoint[] controlPoints, int nCurvePoints, float[] knot, float[] weight, int size, int d) {
		int n = size - 1;
	
		//int curveBot = 1;
		//int curveTop = nCurvePoints;
		// Points below u[d-1] and above u[n+1] have to be discarded 

		TouchPoint[] curvePoints;
		
		curvePoints = new TouchPoint[nCurvePoints];
		for(int i = 0; i < nCurvePoints; i++) {
			curvePoints[i] = new TouchPoint(0, 0);
		}
		
		for(int i = 0; i < nCurvePoints; i++) {
			double u = (double)(n+d) * (double)i / (double)nCurvePoints;
			double x = 0;
			double y = 0;
			float ratio = 0f;
			for(int k = 0; k <= n; k++) {
				double bkd = bSplineBlend(k, d, u, knot);
				x +=  (double)controlPoints[k].x * bkd * weight[k];
				y +=  (double)controlPoints[k].y * bkd * weight[k];
				ratio += bkd * weight[k];
			}
			curvePoints[i].x = Math.round(x / ratio);
			curvePoints[i].y = Math.round(y / ratio);
		}
		
		return curvePoints;
	}
	
	double bSplineBlend(int k, int d, double u, float[] knot) {
		if(d == 1) {
			if((knot[k] <= u && u <= knot[k+1])) {
				return 1f;
			}
			else {
				return 0f;
			}
		}
		else {
			double a = bSplineBlend(k, d-1, u, knot);
			double b = bSplineBlend(k+1, d-1, u, knot);

			double result = (u - knot[k]) / (knot[k + d - 1] - knot[k]) * a;
			result += (knot[k+d] - u) / (knot[k+d] - knot[k+1]) * b;
			return result;
		}
	}
}

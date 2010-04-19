package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.math.*;

import android.util.Log;

public class PathSmoothingStrategyBezier implements IPathSmoothingStrategy {
	
	
	public Collection<TouchPoint> smoothePath(Collection<TouchPoint> points) {
		/*ArrayList<TouchPoint> tempResult = new ArrayList<TouchPoint>();
		{
			TouchPoint[] temp = new TouchPoint[points.size()];
			temp = points.toArray(temp);
			TouchPoint prev = temp[0];
			tempResult.add(temp[0]);
			for(int i = 1; i < temp.length; i++) {
				double dist = Math.sqrt(Math.pow(prev.x - temp[i].x, 2) + Math.pow(prev.y - temp[i].y, 2));	
				if(dist > 15) {
					tempResult.add(temp[i]);
					prev = temp[i];
				}
			}
		}*/
		///// Edge detection /////
		Vector<Vector<TouchPoint>> lines = new Vector<Vector<TouchPoint>>();
		
		double tolerance = 350d;
		TouchPoint[] pts = new TouchPoint[points.size()];
		pts = points.toArray(pts);
		float lastCurve = 0;
		float preLastCurve = 0;
		Vector<TouchPoint> temp = new Vector<TouchPoint>();
		for (int i = 1; i < pts.length-1; i++) {
			temp.add(pts[i]);
			float x1, y1;
			float x2, y2;
			x1 = pts[i-1].x - pts[i].x;
			y1 = pts[i-1].y - pts[i].y;
			x2 = pts[i+1].x - pts[i].x;
			y2 = pts[i+1].y - pts[i].y;
			float zn;		
			zn = x1 * y2 - y1 * x2;
			
			//Winkel
			double cos = (x1 * x2 + y1 * y2) / (Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));
			Log.v("NURBSSCHMURBS", "Winkel: " + cos);
			if(lastCurve == 0) {
				lastCurve = zn;
				preLastCurve = zn;
			}
			//else if(Math.abs(zn - lastCurve) > tolerance && Math.abs(zn - preLastCurve) > tolerance) {
			else if(cos > -0.5) {
				temp.add(pts[i+1]);

				lines.add(temp);
				
				temp = new Vector<TouchPoint>();
				//Log.v(TAG, "New Gesture: old:" + lastCurve + ", new:" + zn);
				lastCurve = 0;
			}
			else {
				preLastCurve = lastCurve;
				lastCurve = zn;
			}
		}
		temp.add(pts[pts.length -1]);
		lines.add(temp);
		
		/// ENDE ///
		
		Vector<TouchPoint> result = new Vector<TouchPoint>();
		Log.v("NURBSSCHMURBS", "-----------------------");
		for(Vector<TouchPoint> v : lines) {
			Log.v("NURBSSCHMURBS", "NEW LINE HAHAHAHAHAHA");
			int nCurvePoints = (int) 2 * v.size();
			
			TouchPoint[] controlPoints = new TouchPoint[v.size()];
			controlPoints = v.toArray(controlPoints);
			int nControlPoints = v.size();
			int d = 4;
			
			float[] weights = new float[nControlPoints];
			for(int i=0; i < nControlPoints; i++) {
				if (i == 0 || i == nControlPoints-1) {
					weights[i] = 5f;
				}
				else {
					weights[i] = 0.5f;
				}
				
			}
			
			float[] knot = new float[nControlPoints + d];
			for(int i=0; i < (nControlPoints + d); i++) {
				knot[i] =  i*3f;
			}
		
			TouchPoint[] curvePoints = NURBS(controlPoints, nCurvePoints, knot, weights, nControlPoints, d);
			//TouchPoint[] curvePoints = controlPoints;
			
			result.add(controlPoints[0]);
			for (int i = 1; i < curvePoints.length; i++) {
				result.add(curvePoints[i]);
			}
		}
		return result;
	}
	
	public String toString() {
		return "Bezier";
	}

	
	public TouchPoint[] NURBS(TouchPoint[] controlPoints, int nCurvePoints, float[] knot, float[] weight, int size, int d) {
		int n = size - 1;
	
		int curveBot;
		int curveTop;
		// Points below u[d-1] and above u[n+1] have to be discarded
		curveBot = 1; 
		curveTop = nCurvePoints;

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

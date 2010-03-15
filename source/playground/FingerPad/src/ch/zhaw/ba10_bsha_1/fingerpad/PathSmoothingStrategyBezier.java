package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.math.*;

public class PathSmoothingStrategyBezier implements IPathSmoothingStrategy {
	
	public Collection<TouchPoint> smoothePath(Collection<TouchPoint> points) {
		Vector<TouchPoint> controlPoints = new Vector<TouchPoint>(points);
		Vector<TouchPoint> curvePoints = new Vector<TouchPoint>();
		int n = points.size() - 1;	
		int nCurvePoints = points.size()*2;
		
		for(int i = 0; i < nCurvePoints; i++) {
			float u = (float)i / nCurvePoints;
			float x = 0;
			float y = 0;
			for(int k = 0; k < points.size(); k++) {
				x += (float)controlPoints.get(k).x * (float)binomCoef(n, k) * Math.pow(u, k) * Math.pow(1-u, n-k);
				y += (float)controlPoints.get(k).y * (float)binomCoef(n, k) * Math.pow(u, k) * Math.pow(1-u, n-k);
			}
			TouchPoint temp = new TouchPoint(Math.round(x),Math.round(y));
			curvePoints.add(temp);
		}
		/*curvePoints[0].x = controlPoints[0].x;
		curvePoints[0].y = controlPoints[0].y;
		curvePoints[nCurvePoints - 1].x = controlPoints[n].x;
		curvePoints[nCurvePoints - 1].y = controlPoints[n].y;*/
		return curvePoints;
	}
	
	int binomCoef(int n, int k) {
		int v = factorial(n).divide(factorial(k).multiply(factorial(n-k))).intValue();
		return v;
	}
	
	BigInteger factorial(int k) {
        //-- BigInteger solution.
        BigInteger n = BigInteger.ONE;
        for (int i=1; i<=k; i++) {
            n = n.multiply(BigInteger.valueOf(i));
        }
        return n;
	}
	
	public String toString() {
		return "Quadratic";
	}
	
}

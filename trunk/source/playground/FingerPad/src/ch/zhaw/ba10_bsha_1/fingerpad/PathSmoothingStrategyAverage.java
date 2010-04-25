package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.math.*;

public class PathSmoothingStrategyQuadratic implements IPathSmoothingStrategy {
	
	public Collection<TouchPoint> smoothePath(Collection<TouchPoint> p) {
		float alpha = 0.6f;
		ArrayList<TouchPoint> points = new ArrayList<TouchPoint>(p);
		ArrayList<TouchPoint> newPoints = new ArrayList<TouchPoint>();
		newPoints.add(points.get(0));
		for (int i = 1; i < points.size(); i++) {
			TouchPoint temp = points.get(i);
			temp.y = alpha * temp.y + (1 - alpha) * newPoints.get(i-1).y;
			//temp.x = alpha * temp.x + (1 - alpha) * newPoints.get(i-1).x;
			newPoints.add(temp);
		}
		return newPoints;
	}
	
	public String toString() {
		return "Quadratic";
	}
	
}

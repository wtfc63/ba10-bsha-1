package ch.zhaw.ba10_bsha_1.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.math.*;
import android.util.Log;

import ch.zhaw.ba10_bsha_1.TouchPoint;


public class PathSmoothingStrategyAverage implements IPreprocessingStrategy {
	
	public MicroGesture process(MicroGesture micro_gesture) {
		int M = 5;
		ArrayList<TouchPoint> points = micro_gesture.getPoints();
		ArrayList<TouchPoint> newPoints = new ArrayList<TouchPoint>();
		newPoints.add(points.get(0));
		for (int i = 1; i < points.size()-M; i++) {
			TouchPoint temp = points.get(i);
			for (int j = 1; j < M; j++) {
				temp.x += points.get(i+j).x;
				temp.y += points.get(i+j).y;
			}
			temp.x /= M;
			temp.y /= M;
			
			newPoints.add(temp);
		}
		for (int j = points.size()-M; j < points.size(); j ++) {
			newPoints.add(points.get(j));
		}
		return (new MicroGesture(newPoints));
	}
	
	public String toString() {
		return "Average";
	}
	
}

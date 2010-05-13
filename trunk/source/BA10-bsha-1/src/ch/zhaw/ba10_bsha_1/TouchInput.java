package ch.zhaw.ba10_bsha_1;


import java.util.ArrayList;
import java.util.Collection;

import android.graphics.Path;


public class TouchInput {
	
	
	private ArrayList<TouchPoint> points;
	private Path  path;
	private float fieldHeight;
	private float upperMax;
	private float lowerMax;
	
	
	public TouchInput(float field_height) {
		points = new ArrayList<TouchPoint>();
		path   = new Path();
		fieldHeight = field_height;
	}
	
	public void add(TouchPoint point) {
		if (points.size() > 0) {
			TouchPoint last = points.get(points.size() - 1);
			path.quadTo(last.x, last.y, ((point.x + last.x) / 2), ((point.y + last.y) / 2));
		} else {
	        path.reset();
	        path.moveTo(point.x, point.y);
		}
		points.add(point);
		if ((point.y > upperMax) || (points.size() == 1)) {
			upperMax = point.y;
		}
		if (point.y < lowerMax) {
			lowerMax = point.y;
		}
	}
	
	private void stretchToField() {
		float real_height = lowerMax - upperMax;
		float corr = 0;
		if (upperMax != lowerMax) { 
			if (real_height > fieldHeight) {
				corr = fieldHeight / real_height;
			} else if ((real_height > (fieldHeight * 2.0/3)) 
					&& (real_height < (fieldHeight * 5.0/6))) {
				corr = (float) (fieldHeight * 2.0/3) / real_height;
			}
		}
		if (corr != 0) {
			for (TouchPoint point : points) {
				point.y = corr * point.y;
			}
		}
	}
	
	public Collection<TouchPoint> getPoints() {
		return points;
	}
	
	public Path getPath() {
		return path;
	}
}
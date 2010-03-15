package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


public class MicroGesture {

	
	public final static int TYPE_UNKNOWN = -1;
	public final static int TYPE_WIDE_CURVE = 0;
	public final static int TYPE_NARROW_CURVE = 1;
	public final static int TYPE_LONG_LINE = 2;
	public final static int TYPE_SHORT_LINE = 3;
	
	private int type;
	private float direction;
	private ArrayList<TouchPoint> points;
	
	
	public MicroGesture() {
		type = TYPE_UNKNOWN;
		direction = 0;
		points = new ArrayList<TouchPoint>();
	}
	
	public MicroGesture(Collection<TouchPoint> points) {
		this();
		this.points.addAll(points);
	}
	
	public MicroGesture(Collection<TouchPoint> points, int type, float direction) {
		this.type = type;
		this.direction = direction;
		this.points = new ArrayList<TouchPoint>(points);
	}
	
	
	public boolean hasKnownType() {
		return (type >= 0);
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	public float getDirection() {
		return direction;
	}
	
	public boolean directionIsUp() {
		return (Math.abs(direction) <= Math.PI) 
				? (direction > 0) : false;
	}
	
	public boolean directionIsDown() {
		return (Math.abs(direction) <= Math.PI) 
				? (direction < 0) : false;
	}
	
	public boolean directionIsRight() {
		return (Math.abs(direction) <= Math.PI) 
				? (Math.abs(direction) > (Math.PI / 2)) : false;
	}
	
	public boolean directionIsLeft() {
		return (Math.abs(direction) <= Math.PI) 
				? (Math.abs(direction) < (Math.PI / 2)) : false;
	}
	
	public void setDirection(float direction) {
		this.direction = direction;
	}
	
	
	public ArrayList<TouchPoint> getPoints() {
		return points;
	}
	
	public void addPoint(float x, float y) {
		addPoint(new TouchPoint(x, y));
	}
	
	public void addPoint(TouchPoint point) {
		points.add(point);
	}
	
	public float getLength() {
		float result = 0;
		for (int i = 0; i < (points.size() - 1); i++) {
			result += points.get(i).distanceTo(points.get(i + 1));
		}
		return result;
	}
	
	public Path getPath() {
		if ((points != null) && (points.size() > 0)) {
			TouchPoint prev = points.get(0);
			Path path = new Path();
	        path.reset();
	        path.moveTo(prev.x, prev.y);
	        for (TouchPoint point : points) {
	        	if (point != prev) {
	        		path.quadTo(prev.x, prev.y, ((point.x + prev.x) / 2), ((point.y + prev.y) / 2));
	        		prev = point;
	        	}
			}
			return path;
		} else {
			return (new Path());
		}
	}
	
	public void paintPath(Canvas canvas, int color) {
		Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        
        paint.setStrokeWidth(2);
        canvas.drawPath(getPath(), paint);
        
        paint.setStrokeWidth(4);
        for (TouchPoint point : points) {
        	canvas.drawPoint(point.x, point.y, paint);
        }
        //canvas.drawPoint(points.get(0).x, points.get(0).y, paint);
        //canvas.drawPoint(points.get(points.size() - 1).x, points.get(points.size() - 1).y, paint);
	}
	
	
	public boolean validate(IMicroGestureDetectionStrategy strategy) {
		return strategy.validateMicroGesture(this);
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		switch (type) {
			case TYPE_WIDE_CURVE :
				result.append("w:");
				break;
			case TYPE_NARROW_CURVE :
				result.append("n:");
				break;
			case TYPE_LONG_LINE :
				result.append("l:");
				break;
			case TYPE_SHORT_LINE :
				result.append("s:");
				break;
			default :
				result.append("u:");
		}
		if (directionIsUp()) {
			result.append('u');
		} else if (directionIsDown()) {
			result.append('d');
		}
		if (directionIsRight()) {
			result.append('r');
		} else if (directionIsLeft()) {
			result.append('l');
		}
		return result.toString();
	}
}

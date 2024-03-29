package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


public class MicroGesture implements Cloneable {

	
	public final static int TYPE_UNKNOWN = -1;
	public final static int TYPE_WIDE_CURVE = 0;
	public final static int TYPE_NARROW_CURVE = 1;
	public final static int TYPE_LONG_LINE = 2;
	public final static int TYPE_SHORT_LINE = 3;
	public final static int TYPE_CIRCLE = 4;
	public final static int TYPE_HALFCIRCLE = 5;
	
	private int type;
	private float direction;
	private int direction2;
	private boolean switchedDirection;
	private ArrayList<TouchPoint> points;
	
	private boolean mergeStubs; 
	
	
	public MicroGesture() {
		type = TYPE_UNKNOWN;
		direction = 0;
		switchedDirection = false;
		points = new ArrayList<TouchPoint>();
		mergeStubs = false;
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
	
	public Object clone() throws CloneNotSupportedException {
		MicroGesture clone = (MicroGesture) super.clone();
		clone.type      = type;
		clone.direction = direction;
		clone.switchedDirection = switchedDirection;
		clone.points = new ArrayList<TouchPoint>(points.size());
		for (TouchPoint point : points) {
			clone.points.add((TouchPoint) point.clone());
		}
		return clone;
	}
	
	public boolean canMergeStubs() {
		return mergeStubs;
	}
	
	public void enableStubMerging(boolean merge_stubs) {
		mergeStubs = merge_stubs;
	}
	
	public MicroGesture merge(MicroGesture other) {
		if (canMergeWith(other)) {
			while (other.points.size() > 0) {
				TouchPoint point = other.points.get(0);
				other.points.remove(point);
				this.points.add(point);
			}
		}
		return (this);
	}
	
	public boolean canMergeWith(MicroGesture other) {
		return (other != null) && (other != this)
				&& ((other.type == this.type) || (mergeStubs && this.points.size() < 3)) 
				&& (Math.abs(other.direction - this.direction) < (Math.PI / 12));
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
		return direction2 == 2 || direction2 == -1;
		//return (Math.abs(direction) <= Math.PI) 
		//		? (direction > 0) : false;
	}
	
	public boolean directionIsDown() {
		return direction2 == 3 || direction2 == -1;
		//return (Math.abs(direction) <= Math.PI) 
		//		? (direction < 0) : false;
	}
	
	public boolean directionIsRight() {
		return direction2 == 1 || direction2 == -1;
		//return (Math.abs(direction) <= Math.PI) 
		//		? (Math.abs(direction) > (Math.PI / 2)) : false;
	}
	
	public boolean directionIsLeft() {
		return direction2 == 0 || direction2 == -1;
		//return (Math.abs(direction) <= Math.PI) 
		//		? (Math.abs(direction) < (Math.PI / 2)) : false;
	}
	
	public void setDirection(float direction) {
		this.direction = direction;
	}
	public void setDirection2(int direction) {
		this.direction2 = direction;
	}
	
	public int getDirection2() {
		return this.direction2;
	}
	
	
	public boolean directionHasSwitched() {
		return switchedDirection;
	}
	
	public void setDirectionSwitched(boolean switched_direction) {
		switchedDirection = switched_direction;
	}
	
	
	public ArrayList<TouchPoint> getPoints() {
		return points;
	}
	
	public String getPointList() {
		StringBuffer list = new StringBuffer();
		Iterator<TouchPoint> itr = points.iterator();
		while (itr.hasNext()) {
			TouchPoint point = itr.next();
			list.append(point.x);
			list.append('/');
			list.append(point.y);
			if (itr.hasNext()) {
				list.append(',');
			}
		}
		return list.toString();
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
			case TYPE_CIRCLE :
				result.append("c:");
				break;
			case TYPE_HALFCIRCLE :
				result.append("h:");
				break;
			default :
				result.append("u:");
		}
		result.append(Float.toString(direction2));
		return result.toString();
	}
	
	public static int StrToType(String str) {
		int type = TYPE_UNKNOWN;
		if (str.contains("w")) {
			type = TYPE_WIDE_CURVE;
		} else if (str.contains("n")) {
			type = TYPE_NARROW_CURVE;
		} else if (str.contains("l")) {
			type = TYPE_LONG_LINE;
		} else if (str.contains("s")) {
			type = TYPE_SHORT_LINE;
		} else if (str.contains("c")) {
			type = TYPE_CIRCLE;
		} else if (str.contains("h")) {
			type = TYPE_HALFCIRCLE;
		}
		return type;
	}
}

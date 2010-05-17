package ch.zhaw.ba10_bsha_1.service;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.zhaw.ba10_bsha_1.TouchPoint;


/**
 * Represents a MicroGesture, a minimal gesture in which we want separate our input point. 
 * Implements {@link Parcelable} and can therefore be send to and from a service.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class MicroGesture implements Cloneable, Parcelable {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	public final static int TYPE_UNKNOWN = -1;
	public final static int TYPE_WIDE_CURVE = 0;
	public final static int TYPE_NARROW_CURVE = 1;
	public final static int TYPE_LONG_LINE = 2;
	public final static int TYPE_SHORT_LINE = 3;
	public final static int TYPE_CIRCLE = 4;
	public final static int TYPE_HALFCIRCLE = 5;
	public final static int TYPE_DOT = 6;
	
	private int type;
	private float direction;
	private int direction2;
	private boolean switchedDirection;
	private ArrayList<TouchPoint> points;
	
	private boolean mergeStubs; 


	//---------------------------------------------------------------------------
	// Constructors and Creators
	//---------------------------------------------------------------------------
	
	
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
	
	public MicroGesture(Parcel source) {
		readFromParcel(source);
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
	
	public static final Parcelable.Creator<MicroGesture> CREATOR = new Parcelable.Creator<MicroGesture>() {
		public MicroGesture createFromParcel(Parcel in) {
			return new MicroGesture(in);
		}

		public MicroGesture[] newArray(int size) {
			return new MicroGesture[size];
		}
	};



	//---------------------------------------------------------------------------
	// Implementation of the Parcelable interface
	//---------------------------------------------------------------------------

	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeFloat(direction);
		dest.writeInt(direction2);
		dest.writeByte(switchedDirection ? Integer.valueOf(1).byteValue() : Integer.valueOf(0).byteValue());
		TouchPoint[] tmp = new TouchPoint[points.size()];
		dest.writeParcelableArray(points.toArray(tmp), flags);
	}
	
	public void readFromParcel(Parcel source) {
		type = source.readInt();
		direction = source.readFloat();
		direction2 = source.readInt();
		switchedDirection = (source.readInt() > 0) ? true : false;
		Parcelable[] tmp = source.readParcelableArray(TouchPoint.class.getClassLoader());
		points = new ArrayList<TouchPoint>(tmp.length);
		for (Parcelable element : tmp) {
			points.add((TouchPoint) element); 
		}
	}

	//---------------------------------------------------------------------------
	// Merging-methods
	//---------------------------------------------------------------------------

	
	public boolean canMergeStubs() {
		return mergeStubs;
	}
	
	public void enableStubMerging(boolean merge_stubs) {
		mergeStubs = merge_stubs;
	}
	
	/**
	 * Determine whether we can merge this MircoGesture 
	 * with another and do so if we can
	 * 
	 * @param other
	 * @return
	 */
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
	
	/**
	 * Determine whether we can merge this MircoGesture with another
	 * 
	 * @param other
	 * @return
	 */
	public boolean canMergeWith(MicroGesture other) {
		return (other != null) && (other != this)
				&& ((other.type == this.type) || (mergeStubs && this.points.size() < 3)) 
				&& (Math.abs(other.direction - this.direction) < (Math.PI / 12));
	}
	

	//---------------------------------------------------------------------------
	// Path-methods
	//---------------------------------------------------------------------------

	
	/**
	 * Calculate the overall length of this MicroGesture (point to point distances)
	 */
	public float getLength() {
		float result = 0;
		for (int i = 0; i < (points.size() - 1); i++) {
			result += points.get(i).distanceTo(points.get(i + 1));
		}
		return result;
	}
	
	/**
	 * Create a {@link Path} out of the MicroGesture's points
	 * 
	 * @return
	 */
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
	
	/**
	 * Draw the MicroGesture's {@link Path} on the given {@link Canvas} in the given color
	 * 
	 * @param canvas
	 * @param color
	 */
	public void drawPath(Canvas canvas, int color) {
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

	

	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------

	
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
	
	public void setDirection(float direction) {
		this.direction = direction;
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
	
	
	public int getDirection2() {
		return this.direction2;
	}
	
	public void setDirection2(int direction) {
		this.direction2 = direction;
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
	
	public void setPoints(Collection<TouchPoint> p) {
		points.clear();
		points.addAll(p);
	}
	
	public void addPoint(float x, float y) {
		addPoint(new TouchPoint(x, y));
	}
	
	public void addPoint(TouchPoint point) {
		points.add(point);
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
			case TYPE_DOT :
				result.append("d:");
				break;
			default :
				result.append("u:");
		}
		result.append(Float.toString(direction2));
		for (TouchPoint p : points) {
			result.append(p.toString());
		}
		
		return result.toString();
	}
	
	/**
	 * Get a list of all the MicroGesture's points as a String
	 * 
	 * @return
	 */
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
	
	/**
	 * Translate the given String-representation of a MicroGesture-Type 
	 * to its integer value
	 * 
	 * @param str
	 * @return
	 */
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
		} else if (str.contains("d")) {
			type = TYPE_DOT;
		}
		return type;
	}
}

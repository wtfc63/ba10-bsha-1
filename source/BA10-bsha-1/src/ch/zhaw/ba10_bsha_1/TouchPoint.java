package ch.zhaw.ba10_bsha_1;


import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Represents one input point from a touchscreen event. Implements {@link Parcelable} and 
 * can therefore be send to and from a service.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class TouchPoint extends PointF implements Cloneable, Parcelable {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private float strength;
	private long timeStamp;


	//---------------------------------------------------------------------------
	// Constructors and Creators
	//---------------------------------------------------------------------------

	
	public TouchPoint(PointF position, float strength, long time_stamp) {
		super(position.x, position.y);
		this.strength = strength;
		this.timeStamp = time_stamp;
	}
	
	public TouchPoint(float pos_x, float pos_y, float strength) {
		this(new PointF(pos_x, pos_y), strength, System.currentTimeMillis());
	}
	
	public TouchPoint(float pos_x, float pos_y, long time_stamp) {
		this(new PointF(pos_x, pos_y), 1, time_stamp);
	}
	
	public TouchPoint(float pos_x, float pos_y) {
		this(new PointF(pos_x, pos_y), 1, System.currentTimeMillis());
	}
	
	public TouchPoint(Parcel source) {
		readFromParcel(source);
	}
	
	public Object clone() throws CloneNotSupportedException {
		TouchPoint clone = (TouchPoint) super.clone();
		clone.strength  = strength;
		clone.timeStamp = timeStamp;
		return clone;
	}
	
	public static final Parcelable.Creator<TouchPoint> CREATOR = new Parcelable.Creator<TouchPoint>() {
		public TouchPoint createFromParcel(Parcel in) {
			return new TouchPoint(in);
		}

		public TouchPoint[] newArray(int size) {
			return new TouchPoint[size];
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
		dest.writeFloat(x);
		dest.writeFloat(y);
		dest.writeFloat(strength);
		dest.writeLong(timeStamp);
	}
	
	public void readFromParcel(Parcel source) {
		x = source.readFloat();
		y = source.readFloat();
		strength  = source.readFloat();
		timeStamp = source.readLong();
	}
	

	//---------------------------------------------------------------------------
	// Getter-/Setter-methods
	//---------------------------------------------------------------------------

	
	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}
	

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		result.append("[x=");
		result.append(x);
		result.append(",y=");
		result.append(y);
		result.append(",ts=");
		result.append(format.format(new Date(timeStamp)));
		result.append(",str=");
		result.append(strength);
		result.append("]");
		return result.toString();
	}


	//---------------------------------------------------------------------------
	// General purpose methods
	//---------------------------------------------------------------------------

	
	/**
	 * Calculate the distance to another {@link TouchPoint}
	 * 
	 * @param other
	 * @return
	 */
	public float distanceTo(TouchPoint other) {
		float a = (float) Math.pow(Math.abs(x - other.x), 2);
		float b = (float) Math.pow(Math.abs(y - other.y), 2);
		float c = a + b;
		return ((float) Math.sqrt(c));
	}
}

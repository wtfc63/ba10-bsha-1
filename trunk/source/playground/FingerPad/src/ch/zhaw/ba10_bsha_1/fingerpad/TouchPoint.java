package ch.zhaw.ba10_bsha_1.fingerpad;


import android.graphics.PointF;


public class TouchPoint extends PointF {

	private float strength;
	private long timeStamp;
	
	
	public TouchPoint(PointF position, float strength, long time_stamp) {
		super(position.x, position.y);
		this.strength = strength;
		this.timeStamp = time_stamp;
	}
	
	public TouchPoint(float pos_x, float pos_y, long time_stamp) {
		this(new PointF(pos_x, pos_y), 1, time_stamp);
	}
	
	public TouchPoint(float pos_x, float pos_y) {
		this(new PointF(pos_x, pos_y), 1, System.currentTimeMillis());
	}

	

	public float getStrength() {
		return strength;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public float distanceTo(TouchPoint second) {
		float a = (float) Math.pow(Math.abs(x - second.x), 2);
		float b = (float) Math.pow(Math.abs(y - second.y), 2);
		float c = a + b;
		return ((float) Math.sqrt(c));
	}
}

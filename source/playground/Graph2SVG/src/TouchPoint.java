import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Represents one input point from a touchscreen event. Implements {@link Parcelable} and 
 * can therefore be send to and from a service.
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class TouchPoint extends Point implements Cloneable {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private float strength;
	private long timeStamp;


	//---------------------------------------------------------------------------
	// Constructors and Creators
	//---------------------------------------------------------------------------

	
	public TouchPoint(Point position, float strength, long time_stamp) {
		super(position.x, position.y);
		this.strength = strength;
		this.timeStamp = time_stamp;
	}
	
	public TouchPoint(int pos_x, int pos_y, float strength) {
		this(new Point(pos_x, pos_y), strength, System.currentTimeMillis());
	}
	
	public TouchPoint(int pos_x, int pos_y, long time_stamp) {
		this(new Point(pos_x, pos_y), 1, time_stamp);
	}
	
	public TouchPoint(int pos_x, int pos_y) {
		this(new Point(pos_x, pos_y), 1, System.currentTimeMillis());
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

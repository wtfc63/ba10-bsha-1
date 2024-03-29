package ch.zhaw.ba10_bsha_1.ime;


import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.strategies.IPreprocessingStrategy;


/**
 * A collection of {@link TouchPoint}s and the {@link Path} connecting them
 */
public class TouchInput {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	
	private ArrayList<TouchPoint> points;
	private Path  path;
	private float upperMax;
	private float lowerMax;
	private float leftBoundary;
	private float rightBoundary;


	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------

	
	public TouchInput() {
		points = new ArrayList<TouchPoint>();
		path   = new Path();
		upperMax = 0;
		lowerMax = 0;
		leftBoundary  = 0;
		rightBoundary = 0;
	}


	//---------------------------------------------------------------------------
	// Getter-methods
	//---------------------------------------------------------------------------

	
	public Collection<TouchPoint> getPoints() {
		return points;
	}
	
	public Path getPath() {
		return path;
	}
	
	/**
	 * Determine whether this TouchInput started from the right and goes to the left
	 * 
	 * @return
	 */
	public boolean isRightToLeft() {
		boolean result = false;
		if (points.size() > 1) {
			TouchPoint first = points.get(0);
			result = (first.x > leftBoundary);
		}
		return result;
	}
	
	/**
	 * Get the bounding rectangle of this TouchInput
	 * 
	 * @return
	 */
	public RectF getDimensions() {
		RectF dimensions = new RectF(leftBoundary, upperMax, rightBoundary, lowerMax);
		return dimensions;
	}
	
	/**
	 * Determine whether this TouchInput crosses out another one
	 * 
	 * @param other
	 * @return
	 */
	public boolean crosses(TouchInput other) {
		boolean result = false;
		RectF this_dim = getDimensions();
		RectF othr_dim = other.getDimensions();
		result = (((this_dim.top > othr_dim.top) && (this_dim.bottom < othr_dim.bottom)) 
				&& ((this_dim.left < othr_dim.left) && (this_dim.right > othr_dim.right)));
		return result;
	}

	
	//---------------------------------------------------------------------------
	// Adding TouchPoints
	//---------------------------------------------------------------------------

	
	/**
	 * Adds a {@link TouchPoint} to the input, updating its {@link Path} and boundaries while doing so
	 */
	public void add(TouchPoint point) {
		//Add point and extend Path
		if (points.size() > 0) {
			TouchPoint last = points.get(points.size() - 1);
			path.quadTo(last.x, last.y, ((point.x + last.x) / 2), ((point.y + last.y) / 2));
		} else {
	        path.reset();
	        path.moveTo(point.x, point.y);
		}
		points.add(point);
		
		//Update boundaries
		if ((point.y < upperMax) || (points.size() == 1)) {
			upperMax = point.y;
		}
		if (point.y > lowerMax) {
			lowerMax = point.y;
		}
		if ((point.x < leftBoundary) || (points.size() == 1)) {
			leftBoundary = point.x;
		}
		if (point.x > rightBoundary) {
			rightBoundary = point.x;
		}
	}


	//---------------------------------------------------------------------------
	// Other methods
	//---------------------------------------------------------------------------

	
	/**
	 * Stretch the input's points to a given field height
	 * (Deprecated because an {@link IPreprocessingStrategy} would 
	 * probably be better suited for that task)
	 * 
	 * @param field_height
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void stretchToField(float field_height) {
		float real_height = lowerMax - upperMax;
		float corr = 0;
		if (upperMax != lowerMax) { 
			if (real_height > field_height) {
				corr = field_height / real_height;
			} else if ((real_height > (field_height * 2.0/3)) 
					&& (real_height < (field_height * 5.0/6))) {
				corr = (float) (field_height * 2.0/3) / real_height;
			}
		}
		if (corr != 0) {
			for (TouchPoint point : points) {
				point.y = corr * point.y;
			}
		}
	}
}
package ch.zhaw.ba10_bsha_1.ime;


import java.util.*;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.
import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.IDetectionService;
import ch.zhaw.ba10_bsha_1.service.IReturnRecognisedCharacters;


public class PadView extends View /*implements IObservable*/ {
	
    
    private static final float TOUCH_TOLERANCE = 4;
    
    private ArrayList<TouchPoint> points;
    
    private Bitmap          bitmap;
    private Canvas          bmCanvas;
    private ArrayList<Path> paths;
    private Paint           bmPaint;
    private Paint           paint;
    
    private float lastX;
    private float lastY;
    
    //private ArrayList<IObserver> observers;
    //private boolean log;
    //private boolean autoClear;
    
    //private ArrayList<TouchInput> inputs;
   // private TouchInput currentInput;
    //private IMicroGestureDetectionStrategy mgDetectionStrategy;
    //private ICharacterDetectionStrategy charDetectionStrategy;
    

	public PadView(Context context) {
		super(context);
		init();
	}
	
	public PadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		//observers = new ArrayList<IObserver>();
		//inputs = new ArrayList<TouchInput>();
		//currentInput = null;
		//mgDetectionStrategy = new MicroGestureDetectionStrategyAwesome();
		//charDetectionStrategy = TouchInput.CHAR_DETECTION_STRATEGY_CUSTOM_GRAPH;
		//charDetectionStrategy = TouchInput.CHAR_DETECTION_STRATEGY_GRAPH;
		
        paths  = new ArrayList<Path>();
        //points = new ArrayList<TouchPoint>();
        bitmap = Bitmap.createBitmap(320, 240, Bitmap.Config.ARGB_8888);
        bmCanvas = new Canvas(bitmap);
        bmPaint  = new Paint(Paint.DITHER_FLAG);
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xCCFAFAC8);
        canvas.drawBitmap(bitmap, 0, 0, bmPaint);
        paintBaseLines(canvas);
        for (Path path : paths) {
        	canvas.drawPath(path, paint);
        }
        /*if (showPoints) {
        	Paint p_paint = new Paint();
            p_paint.setColor(Color.YELLOW);
        	p_paint.setStyle(Paint.Style.STROKE);
            p_paint.setStrokeCap(Paint.Cap.ROUND);
            p_paint.setStrokeWidth(8);
        	for (Point p : points) {
        		canvas.drawPoint(p.x, p.y, p_paint);
        	}
        }
        if ((inputs != null) && (inputs.size() > 0) 
        		&& (inputs.get(inputs.size() - 1) != null)) {
        	currColor = 0;
	        for (MicroGesture mg : inputs.get(inputs.size() - 1).getMicroGestures()) {
				mg.paintPath(canvas, colors[currColor++ % 4]);
			}
        }*/
    }
    
    private void paintBaseLines(Canvas canvas) {
    	Paint bl_paint = new Paint();
    	bl_paint.setAntiAlias(true);
    	bl_paint.setDither(true);
    	bl_paint.setColor(Color.BLACK);
    	bl_paint.setStyle(Paint.Style.STROKE);
    	bl_paint.setStrokeJoin(Paint.Join.ROUND);
    	bl_paint.setStrokeCap(Paint.Cap.ROUND);
    	bl_paint.setStrokeWidth(4);
        
    	float right_X = 20;
        float left_X = this.getWidth() - right_X;
        float mid_Y = this.getHeight() / 2;
        float line_height = (this.getHeight() / 2) / 3;
        canvas.drawLine(20, mid_Y, left_X, mid_Y, bl_paint);
        
        bl_paint.setStrokeWidth(2);
        bl_paint.setPathEffect(new DashPathEffect(new float[] {8, 4}, 0));
        canvas.drawLine(right_X, (mid_Y - (2 * line_height)), left_X, (mid_Y - (2 * line_height)), bl_paint);
        canvas.drawLine(right_X, (mid_Y - line_height), left_X, (mid_Y - line_height), bl_paint);
        canvas.drawLine(right_X, (mid_Y + line_height), left_X, (mid_Y + line_height), bl_paint);
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float pressure = event.getPressure();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y, pressure);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y, pressure);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
    
    private void touchStart(float x, float y, float pressure) {
		//if (autoClear) {
			clear();
		//}
		
    	//currentInput = new TouchInput(mgDetectionStrategy, (this.getHeight() / 2), charDetectionStrategy);
    	//currentInput.add(new TouchPoint(x, y));
    	//currentInput.enableSmoothing(smoothPoints);
    	
    	Path path = new Path();
        path.reset();
        path.moveTo(x, y);
        lastX = x;
        lastY = y;
        paths.add(path);
        //points.add(new TouchPoint(x, y, pressure));
    }
    
    private void touchMove(float x, float y, float pressure) {
    	//currentInput.add(new TouchPoint(x, y));
    	
        float dx = Math.abs(x - lastX);
        float dy = Math.abs(y - lastY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            paths.get(paths.size() - 1).quadTo(lastX, lastY, ((x + lastX) / 2), ((y + lastY) / 2));
            lastX = x;
            lastY = y;
            //points.add(new TouchPoint(x, y, pressure));
        }
    }
    
    private void touchUp() {
    	//currentInput.setToStretch((this.getHeight() / 2));
    	//currentInput.startDetection();
    	//inputs.add(currentInput);
    	//currentInput = null;
    	//notifyObservers();
    	
    	Path path = paths.get(paths.size() - 1);
    	path.lineTo(lastX, lastY);
        //points.add(new TouchPoint((int) lastX, (int) lastY));
        // commit the path to our offscreen
        //bmCanvas.drawPath(path, paint);
        // kill this so we don't double draw
        //mPath.reset();
    }
    
    
    /*public float getStrokeWidth() {
    	return paint.getStrokeWidth();
    }
    
    public void incStrokeWidth(float delta) {
    	paint.setStrokeWidth(paint.getStrokeWidth() + delta);
    	invalidate();
    }*/
    
    /*public boolean showsPoints() {
    	return showPoints;
    }*/
    
    /*public void setToShowPoints(boolean show_points) {
    	showPoints = show_points;
    	invalidate();
    }*/
    
    /*public boolean smoothsPoints() {
    	return smoothPoints;
    }*/
    
    /*public void setToSmoothPoints(boolean smooth_points) {
    	smoothPoints = smooth_points;
    }*/
    
    public void clear() {
    	paths.clear();
    	points.clear();
    	//inputs.clear();
    	invalidate();
    }
    
    /*public IMicroGestureDetectionStrategy getMicroGestureDetectionStrategy() {
    	return mgDetectionStrategy;
    }*/
    
    /*public ICharacterDetectionStrategy getCharacterDetectionStrategy() {
    	return charDetectionStrategy;
    }*/
    
    /*public void setDetectionStrategies(
    		IMicroGestureDetectionStrategy mg_strategy, 
    		ICharacterDetectionStrategy char_strategy, 
    		boolean redetect) {
    	mgDetectionStrategy = mg_strategy;
    	mgDetectionStrategy.setFieldHeight(this.getHeight() / 2);
    	charDetectionStrategy = char_strategy;
    	if (redetect && (inputs.size() > 0)) {
    		TouchInput last = inputs.get(inputs.size() - 1);
    		last.setMicroGestureDetectionStrategy(mgDetectionStrategy);
    		last.setCharacterDetectionStrategy(char_strategy);
    		last.startDetection();
    		notifyObservers();
    	}
    }*/
    
    /*public String getLastMicroGestureDetectionReport() {
    	String result = "none";
    	if (inputs.size() > 0) {
    		result = inputs.get(inputs.size() - 1).getMicroGestureDetectionReport();
    	}
    	return result;
    }*/
    
    /*public String getLastCharacterDetectionReport() {
    	String result = "none";
    	if (inputs.size() > 0) {
    		result = inputs.get(inputs.size() - 1).getCharacterDetectionReport();
    	}
    	return result;
    }*/
    
    /*public boolean logIsEnabled() {
    	return log;
    }*/
    
    /*public void enableLog(boolean enable_log) {
    	log = enable_log;
    }*/
    
    /*public boolean isToAutoClear() {
    	return autoClear;
    }
    
    public void enableAutoClear(boolean auto_clear) {
    	autoClear = auto_clear;
    }*/

	/*@Override
	public void attachObserver(IObserver observer) {
		observers.add(observer);
	}

	@Override
	public void detachObserver(IObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		for (IObserver observer : observers) {
			observer.update(this);
		}
		if (log && (inputs != null) && (inputs.size() > 0)) {
			DetectionLogger.getInstance().log(inputs.get(inputs.size() - 1).getCharacters());
		}
	}*/
}

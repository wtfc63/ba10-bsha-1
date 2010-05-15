package ch.zhaw.ba10_bsha_1.ime;


import java.util.*;

import android.content.Context;
import android.graphics.*;
import android.inputmethodservice.KeyboardView;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.
import ch.zhaw.ba10_bsha_1.Character;
import ch.zhaw.ba10_bsha_1.R;
import ch.zhaw.ba10_bsha_1.TouchInput;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.service.IDetectionService;
import ch.zhaw.ba10_bsha_1.service.IReturnRecognisedCharacters;


public class PadView extends KeyboardView implements IObservable {
	
    
    public static final int EVENT_TYPE_NONE = 0;
    public static final int EVENT_TYPE_NEW_POINTS = 1;
    public static final int EVENT_TYPE_TOUCH_UP = 2;
    public static final int EVENT_TYPE_SPACE = 3;
    public static final int EVENT_TYPE_BACKSPACE = 4;
    
    private static final float TOUCH_TOLERANCE = 4;
    
    private float lastX;
    private float lastY;
    
    private ArrayList<TouchInput> inputs;
    private TouchInput currentInput;
    private int oldPathColor;
    private int curPathColor;
    
    private Bitmap bitmap;
    private Paint  bmPaint;
    private Paint  paint;
    private float  margin;
    
    private ArrayList<IObserver> observers;
    private int eventType;
    
	
	public PadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
    public PadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		init();
    }
	
	private void init() {
		observers = new ArrayList<IObserver>();
		inputs = new ArrayList<TouchInput>();
		eventType = EVENT_TYPE_NONE;
		currentInput = null;
		oldPathColor = 0x44FF0000;
		curPathColor = 0xCCFF0000;
		
        bitmap  = Bitmap.createBitmap(320, 240, Bitmap.Config.ARGB_8888);
        bmPaint = new Paint(Paint.DITHER_FLAG);
        margin  = 10;
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);
	}

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((w > 0) && (h > 0)) {
        	bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        }
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFAFAC8);
        canvas.drawBitmap(bitmap, 0, 0, bmPaint);
        paintBaseLines(canvas);
        paint.setColor(oldPathColor);
        for (TouchInput input : inputs) {
        	canvas.drawPath(input.getPath(), paint);
        }
        if (currentInput != null) {
        	paint.setColor(curPathColor);
        	canvas.drawPath(currentInput.getPath(), paint);
        }
    }
    
    private void paintBaseLines(Canvas canvas) {
    	Paint bl_paint = new Paint();
    	bl_paint.setAntiAlias(true);
    	bl_paint.setDither(true);
    	bl_paint.setColor(0xAAA08200);
    	bl_paint.setStyle(Paint.Style.STROKE);
    	bl_paint.setStrokeJoin(Paint.Join.ROUND);
    	bl_paint.setStrokeCap(Paint.Cap.ROUND);
    	bl_paint.setStrokeWidth(2);
        
    	float right_X = margin;
        float left_X = this.getWidth() - margin;
        float line_height = (this.getHeight() - (2 * margin)) / 3;
        float mid_Y = (2 * line_height) + margin;
        canvas.drawLine(right_X, mid_Y, left_X, mid_Y, bl_paint);
        
        bl_paint.setStrokeWidth(1);
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
    	if (inputs.size() > 0) {
    		RectF last = inputs.get(inputs.size() - 1).getDimensions();
    		if (x > (last.right + last.width())) {
    			notifyObservers(EVENT_TYPE_SPACE);
    		}
    	}
    	
    	currentInput = new TouchInput(this.getHeight() - (2 * margin));
    	currentInput.add(new TouchPoint(x, y, pressure));
    	
        lastX = x;
        lastY = y;
    }
    
    private void touchMove(float x, float y, float pressure) {
        float dx = Math.abs(x - lastX);
        float dy = Math.abs(y - lastY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        	currentInput.add(new TouchPoint(x, y, pressure));
            lastX = x;
            lastY = y;
        }
    }
    
    private void touchUp() {
    	if (inputs.size() > 0) {
    		TouchInput last = inputs.get(inputs.size() - 1);
    		if (currentInput.crosses(last)) {
    			inputs.remove(last);
    			notifyObservers(EVENT_TYPE_BACKSPACE);
    		} else {
    			inputs.add(currentInput);
        		notifyObservers(EVENT_TYPE_TOUCH_UP);
    		}
    		currentInput = null;
    	} else {
    		inputs.add(currentInput);
    		currentInput = null;
    		notifyObservers(EVENT_TYPE_TOUCH_UP);
    	}
    }
    
    public void clear() {
    	inputs.clear();
    	invalidate();
    }
    
    public Collection<TouchPoint> getLastPoints() {
    	TouchInput last = (inputs.size() > 0) ? inputs.get(inputs.size() - 1) : null; 
    	return (last != null) ? last.getPoints() : null;
    }

	@Override
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
	}
	
	public void notifyObservers(int event_type) {
		eventType = event_type;
		notifyObservers();
		eventType = EVENT_TYPE_NONE;
	}
	
	public int getEventType() {
		return eventType;
	}
}

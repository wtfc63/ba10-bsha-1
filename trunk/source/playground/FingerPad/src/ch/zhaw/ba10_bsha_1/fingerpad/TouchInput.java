package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;

import android.R.string;


public class TouchInput {
	

	public static final IMicroGestureDetectionStrategy DETECTION_STRATEGY_NONE = new MicroGestureDetectionStrategyNone();
	public static final IMicroGestureDetectionStrategy DETECTION_STRATEGY_PREDICTION = new MicroGestureDetectionStrategyPreditction();
	public static final IMicroGestureDetectionStrategy DETECTION_STRATEGY_CURVATURE = new MicroGestureDetectionStrategyCurvature();
	
	private ArrayList<TouchPoint> points;
	private Collection<MicroGesture> microGestures;
	private IMicroGestureDetectionStrategy detectionStrategy;
	
	
	public TouchInput() {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		detectionStrategy = DETECTION_STRATEGY_NONE;
	}
	
	public TouchInput(IMicroGestureDetectionStrategy detection_strategy) {
		this();
		detectionStrategy = detection_strategy;
	}
	
	
	public void add(TouchPoint point) {
		points.add(point);
	}
	
	public IMicroGestureDetectionStrategy getDetectionStrategy() {
		return detectionStrategy;
	}

	public void setDetectionStrategy(IMicroGestureDetectionStrategy detectionStrategy) {
		this.detectionStrategy = detectionStrategy;
	}

	public void startDetection() {
		microGestures = detectMicroGestures(detectionStrategy);
	}
	
	public Collection<MicroGesture> detectMicroGestures(IMicroGestureDetectionStrategy strategy) {
		return (new ArrayList<MicroGesture>(strategy.detectMicroGestures(points)));
	}
	
	public Collection<MicroGesture> getMicroGestures() {
		return microGestures;
	}
	
	public String getDetectionReport() {
		StringBuffer result = new StringBuffer();
		if (microGestures != null) {
			result.append("Strategy: ");
			result.append(detectionStrategy);
			result.append(", MicroGestures: ");
			for (MicroGesture mg : microGestures) {
				result.append(mg);
				result.append(", ");
			}
			result.delete(result.length() - 2, result.length());
		} else {
			result.append("No Detection done");
		}
		return result.toString();
	}
}

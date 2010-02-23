package ch.zhaw.ba10_bsha_1.fingerpad;

import java.util.ArrayList;
import java.util.Collection;

import android.R.string;

public class TouchInput {
	
	
	public static final int DETECTION_STRATEGY_NONE = 0;
	public static final IMicroGestureDetectionStrategy[] DETECTION_STRATEGIES = new IMicroGestureDetectionStrategy[] {
		new MicroGestureDetectionStrategyNone()
	};
	
	private ArrayList<TouchPoint> points;
	private Collection<MicroGesture> microGestures;
	private IMicroGestureDetectionStrategy detectionStrategy;
	
	
	public TouchInput() {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		detectionStrategy = DETECTION_STRATEGIES[DETECTION_STRATEGY_NONE];
	}
	
	public TouchInput(IMicroGestureDetectionStrategy detection_strategy) {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		detectionStrategy = detection_strategy;
	}
	
	
	public void add(TouchPoint point) {
		points.add(point);
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

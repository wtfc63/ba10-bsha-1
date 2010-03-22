package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.ArrayList;
import java.util.Collection;

import android.R.string;


public class TouchInput {
	

	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_NONE = new MicroGestureDetectionStrategyNone();
	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_PREDICTION = new MicroGestureDetectionStrategyPreditction();
	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_CURVATURE = new MicroGestureDetectionStrategyCurvature();
	
	public static final ICharacterDetectionStrategy CHAR_DETECTION_STRATEGY_NONE = new CharacterDetectionStrategyNone();
	public static final ICharacterDetectionStrategy CHAR_DETECTION_STRATEGY_GRAPH = new CharacterDetectionStrategyGraph();
	public static final ICharacterDetectionStrategy CHAR_DETECTION_STRATEGY_CUSTOM_GRAPH = new CharacterDetectionStrategyCustomGraph("");
	
	private ArrayList<TouchPoint> points;
	private boolean stretch = true;
	private boolean smooth = false;
	private float letterHeight;
	private float upperMax;
	private float lowerMax;
	private Collection<MicroGesture> microGestures;
	private Collection<Character> characters;
	private IMicroGestureDetectionStrategy mgDetectionStrategy;
	private ICharacterDetectionStrategy charDetectionStrategy;
	private IPathSmoothingStrategy smoothingStrategy;
	
	
	public TouchInput(float field_height) {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		mgDetectionStrategy = MG_DETECTION_STRATEGY_NONE;
		mgDetectionStrategy.setFieldHeight(field_height);
		charDetectionStrategy = CHAR_DETECTION_STRATEGY_NONE;
		smoothingStrategy = new PathSmoothingStrategyBezier();
	}
	
	public TouchInput(IMicroGestureDetectionStrategy mg_strategy, float field_height, ICharacterDetectionStrategy char_strategy) {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		mgDetectionStrategy = mg_strategy;
		mgDetectionStrategy.setFieldHeight(field_height);
		charDetectionStrategy = char_strategy;
		smoothingStrategy = new PathSmoothingStrategyBezier();
	}
	
	public boolean isToStretch() {
		return stretch;
	}
	
	public void enableStretching(boolean stretch) {
		this.stretch = stretch;
	}
	
	public void setToStretch(float letter_height) {
		stretch = true;
		letterHeight = letter_height;
	}
	
	public boolean isToSmooth() {
		return smooth;
	}
	
	public void enableSmoothing(boolean smooth) {
		this.smooth  = smooth;
	}
	
	public void add(TouchPoint point) {
		points.add(point);
		if ((point.y < upperMax) || (points.size() == 1)) {
			upperMax = point.y;
		}
		if (point.y > lowerMax) {
			lowerMax = point.y;
		}
	}
	
	public IMicroGestureDetectionStrategy getMicroGestureDetectionStrategy() {
		return mgDetectionStrategy;
	}

	public void setMicroGestureDetectionStrategy(IMicroGestureDetectionStrategy detectionStrategy) {
		this.mgDetectionStrategy = detectionStrategy;
	}
	
	public ICharacterDetectionStrategy getCharacterDetectionStrategy() {
		return charDetectionStrategy;
	}

	public void setCharacterDetectionStrategy(ICharacterDetectionStrategy detectionStrategy) {
		this.charDetectionStrategy = detectionStrategy;
	}
	
	public Collection<MicroGesture> detectMicroGestures(IMicroGestureDetectionStrategy strategy) {
		return (new ArrayList<MicroGesture>(strategy.detectMicroGestures(points)));
	}
	
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
	
	public Collection<MicroGesture> getMicroGestures() {
		return microGestures;
	}
	
	public String getMicroGestureDetectionReport() {
		StringBuffer result = new StringBuffer();
		if (microGestures != null) {
			result.append("Strategy: ");
			result.append(mgDetectionStrategy);
			result.append(", MicroGestures: \n");
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
	
	public Collection<Character> detectCharacters(ICharacterDetectionStrategy strategy) {
		return (new ArrayList<Character>(strategy.detectCharacter(microGestures)));
	}
	
	public Collection<Character> getCharacters() {
		return characters;
	}
	
	public String getCharacterDetectionReport() {
		StringBuffer result = new StringBuffer();
		if (characters != null) {
			result.append("Strategy: ");
			result.append(charDetectionStrategy);
			result.append(", Characters: \n");
			for (Character chr : characters) {
				result.append(chr);
				result.append(", ");
			}
			result.delete(result.length() - 2, result.length());
		} else {
			result.append("No Detection done");
		}
		return result.toString();
	}

	public void startDetection() {
		if (smooth) {
			points = new ArrayList<TouchPoint>(smoothingStrategy.smoothePath(points));
		}
		if (stretch) {
			stretchToField(letterHeight);
		}
		microGestures = detectMicroGestures(mgDetectionStrategy);
		characters = detectCharacters(charDetectionStrategy);
	}
}
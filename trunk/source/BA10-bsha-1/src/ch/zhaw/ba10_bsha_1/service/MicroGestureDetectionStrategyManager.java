package ch.zhaw.ba10_bsha_1.service;


public class MicroGestureDetectionStrategyManager extends StrategyManager<IMicroGestureDetectionStrategy> {

	
	private static MicroGestureDetectionStrategyManager instance;
	
	public static MicroGestureDetectionStrategyManager getInstance() {
		if (instance == null) {
			instance = new MicroGestureDetectionStrategyManager();
		}
		return instance;
	}
	
	
	protected void initManager() {
		addStrategy(new MicroGestureDetectionStrategyNone());
		addStrategy(new MicroGestureDetectionStrategyPrediction());
		addStrategy(new MicroGestureDetectionStrategyCurvature());
	}
}

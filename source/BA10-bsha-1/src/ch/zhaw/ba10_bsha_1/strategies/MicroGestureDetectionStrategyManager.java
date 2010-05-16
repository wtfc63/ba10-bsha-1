package ch.zhaw.ba10_bsha_1.strategies;


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
		addStrategy(new MicroGestureDetectionStrategyEdges());
		addStrategy(new MicroGestureDetectionStrategyCircles());
		addStrategy(new MicroGestureDetectionStrategyRemoveTiny());
		addStrategy(new MicroGestureDetectionStrategyLines());
		addStrategy(new MicroGestureDetectionStrategyHalfCircle());
		addStrategy(new MicroGestureDetectionStrategyPrediction());
		addStrategy(new MicroGestureDetectionStrategyCurvature());
		addStrategy(new MicroGestureDetectionStrategySmoothing());
	}
}

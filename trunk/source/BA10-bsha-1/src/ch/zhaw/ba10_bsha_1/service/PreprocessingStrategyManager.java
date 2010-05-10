package ch.zhaw.ba10_bsha_1.service;


public class PreprocessingStrategyManager extends StrategyManager<IPreprocessingStrategy> {
	
	
	private static PreprocessingStrategyManager instance;
	
	public static PreprocessingStrategyManager getInstance() {
		if (instance == null) {
			instance = new PreprocessingStrategyManager();
		}
		return instance;
	}
	

	protected void initManager() {
		addStrategy(new PathSmoothingStrategySpline());
		addStrategy(new PathSmoothingStrategyAverage());
	}
}

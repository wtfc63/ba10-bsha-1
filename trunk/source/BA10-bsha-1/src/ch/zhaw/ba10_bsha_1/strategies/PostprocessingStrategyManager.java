package ch.zhaw.ba10_bsha_1.strategies;


public class PostprocessingStrategyManager extends StrategyManager<IPostprocessingStrategy> {

	
	private static PostprocessingStrategyManager instance;
	
	public static PostprocessingStrategyManager getInstance() {
		if (instance == null) {
			instance = new PostprocessingStrategyManager();
		}
		return instance;
	}
	
	
	protected void initManager() {
		addStrategy(new PostprocessingStrategyNone());
	}
}

package ch.zhaw.ba10_bsha_1.service;


public interface IPreprocessingStrategy {
	public MicroGesture process(MicroGesture micro_gesture);
	public String toString();
}

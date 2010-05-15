package ch.zhaw.ba10_bsha_1.ime;


/**
 * Interface to be implemented for an Observer 
 * according to the Observer pattern.
 */
public interface IObserver {
	
	/**
	 * React on changes or events in an observed object (AKA Subject)  
	 * 
	 * @param updater
	 */
	public void update(IObservable updater);
}

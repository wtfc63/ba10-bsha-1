package ch.zhaw.ba10_bsha_1.ime;


/**
 * Interface to be implemented for an observable object (AKA Subject) 
 * according to the Observer pattern.
 */
public interface IObservable {
	
	/**
     * Attach an Observer to the observable Object/Subject
     */
	public void attachObserver(IObserver observer);

    /**
     * Detach an Observer from the observable Object/Subject
     */
	public void detachObserver(IObserver observer);
	
	/**
	 * Notify all attached Observers of changes in the observable Object/Subject
	 */
	public void notifyObservers();
}

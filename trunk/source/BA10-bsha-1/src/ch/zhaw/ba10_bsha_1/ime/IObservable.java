package ch.zhaw.ba10_bsha_1.ime;

public interface IObservable {
	public void attachObserver(IObserver observer);
	public void detachObserver(IObserver observer);
	public void notifyObservers();
}

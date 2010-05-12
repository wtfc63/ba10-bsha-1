package ch.zhaw.ba10_bsha_1.service;


public interface PriorityQueue<Type> {
	
	/**
	 * Add Element to prioritized queue
	 * 
	 * @param element
	 * @param priority
	 */
	public void enqueue(Type element, int priority);	

	/**
	 * Removes and returns first element of Queue
	 * 
	 * @return
	 */
	public Type dequeue();			

	/**
	 * Get first element of the queue
	 * 
	 * @return
	 */
	public Type getFront();	

	/**
	 * Returns whether queue can take more elements or not
	 * 
	 * @return
	 */
	public boolean isFull();

	/**
	 * Returns whether there are elements in the queue or not
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * Removes all elements from the queue
	 */
	public void makeEmpty();	
}
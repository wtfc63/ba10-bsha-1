package ch.zhaw.ba10_bsha_1.service;


/**
 * Interface defining a priority queue
 * 
 * @author Julian Hanhart, Dominik Giger
 * @param <Type>
 */
public interface IPriorityQueue<Type> {
	
	/**
	 * Adds an element to prioritized queue
	 * 
	 * @param element
	 * @param priority
	 */
	public void enqueue(Type element, int priority);	

	/**
	 * Removes and returns the first element of the queue
	 * 
	 * @return
	 */
	public Type dequeue();			

	/**
	 * Get the first element of the queue
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
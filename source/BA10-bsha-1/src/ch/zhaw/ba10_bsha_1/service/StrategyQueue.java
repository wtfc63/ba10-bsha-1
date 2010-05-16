package ch.zhaw.ba10_bsha_1.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.zhaw.ba10_bsha_1.strategies.IStrategy;


/**
 * Implements a PriorityQueue for IStrategy elements. Also acts as an Iterator over its queue
 * 
 * @author Julian Hanhart, Dominik Giger
 * @param <Strategy>
 */
public class StrategyQueue<Strategy extends IStrategy> implements IPriorityQueue<Strategy>, Iterator<Strategy>, Iterable<Strategy> {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------
	
	
	private ArrayList<Prioritized<Strategy>> queue;
	private int currentElement;


	//---------------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------------
	
	
	public StrategyQueue() {
		queue = new ArrayList<Prioritized<Strategy>>();
		currentElement = 0;
	}
	
	public StrategyQueue(Collection<Strategy> strategies) {
		this();
		for (Strategy strategy : strategies) {
			queue.add(new Prioritized<Strategy>(strategy, currentElement++));
		}
		currentElement = 0;
	}
	
	public StrategyQueue(Collection<Strategy> strategies, int initial_priority) {
		this();
		for (Strategy strategy : strategies) {
			queue.add(new Prioritized<Strategy>(strategy, initial_priority));
		}
		currentElement = 0;
	}
	
	public StrategyQueue(Collection<Strategy> strategies, int[] priorities) {
		this();
		for (Strategy strategy : strategies) {
			queue.add(new Prioritized<Strategy>(strategy, priorities[currentElement]));
			if ((currentElement + 1) < priorities.length) {
				currentElement++;
			}
		}
		currentElement = 0;
	}


	//---------------------------------------------------------------------------
	// Implementation of the IPriorityQueue interface
	//---------------------------------------------------------------------------
	

	/**
	 * Adds a Strategy to the prioritized queue
	 * 
	 * @param element
	 * @param priority
	 */
	@Override
	public void enqueue(Strategy element, int priority) {
		boolean added = false;
		Prioritized<Strategy> tmp = new Prioritized<Strategy>(element, priority);
		for (int i = 0; (!added && (i < queue.size())); i++) {
			if (tmp.getPriority() < queue.get(i).getPriority()) {
				queue.add(i, tmp);
				added = true;
			}
		}
		if (!added) {
			queue.add(tmp);
		}
	}

	/**
	 * Removes and returns the first Strategy from the queue
	 * 
	 * @return
	 */
	@Override
	public Strategy dequeue() {
		return (queue.size() > 0) ? queue.remove(0).getData() : null;
	}

	/**
	 * Get the first Strategy of the queue
	 * 
	 * @return
	 */
	@Override
	public Strategy getFront() {
		return (queue.size() > 0) ? queue.get(0).getData() : null;
	}

	/**
	 * Returns whether queue can take more Strategies or not
	 * 
	 * @return
	 */
	@Override
	public boolean isFull() {
		return false;
	}

	/**
	 * Returns whether there are Strategies in the queue or not
	 * 
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Removes all Strategies from the queue
	 */
	@Override
	public void makeEmpty() {
		queue.clear();
		currentElement = 0;
	}


	//---------------------------------------------------------------------------
	// Implementation of the Iterable interface
	//---------------------------------------------------------------------------
	
	
	/**
	 * Resets the Iterator and return reference to self
	 */
	public Iterator<Strategy> iterator() {
		currentElement = 0;
		return (this);
	}

	
	//---------------------------------------------------------------------------
	// Implementation of the Iterator interface
	//---------------------------------------------------------------------------
	
	
	
	/**
	 * Returns whether there are more Strategies in the queue to iterate over
	 */
	@Override
	public boolean hasNext() {
		return (currentElement < queue.size());
	}

	/**
	 * Get next Strategy in queue
	 */
	@Override
	public Strategy next() {
		Strategy result = queue.get(currentElement).getData();
		currentElement++;
		return result;
	}

	/**
	 * Removes the Strategy return last from the queue
	 */
	@Override
	public void remove() {
		queue.remove(currentElement - 1);
	}
}

package ch.zhaw.ba10_bsha_1.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ch.zhaw.ba10_bsha_1.strategies.IStrategy;


public class StrategyQueue<Strategy extends IStrategy> implements PriorityQueue<Strategy>, Iterator<Strategy> {

	
	private ArrayList<Prioritized<Strategy>> queue;
	private int currentElement;
	
	
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
	

	@Override
	public void enqueue(Strategy element, int priority) {
		boolean added = false;
		Prioritized<Strategy> tmp = new Prioritized<Strategy>(element, priority);
		for (int i = 0; i < queue.size(); i++) {
			if (queue.get(i).getPriority() < tmp.getPriority()) {
				queue.add(i, tmp);
				added = true;
			}
		}
		if (!added) {
			queue.add(tmp);
		}
	}
	
	@Override
	public Strategy dequeue() {
		return (queue.size() > 0) ? queue.remove(0).getData() : null;
	}

	@Override
	public Strategy getFront() {
		return (queue.size() > 0) ? queue.get(0).getData() : null;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public void makeEmpty() {
		queue.clear();
		currentElement = 0;
	}

	
	@Override
	public boolean hasNext() {
		return (currentElement < (queue.size() - 1));
	}

	@Override
	public Strategy next() {
		Strategy result = queue.get(currentElement).getData();
		currentElement = (currentElement + 1) % queue.size();
		return result;
	}

	@Override
	public void remove() {
		queue.remove((currentElement - 1) % queue.size());
	}

}

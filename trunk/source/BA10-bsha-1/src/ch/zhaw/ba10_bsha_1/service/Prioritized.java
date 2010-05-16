package ch.zhaw.ba10_bsha_1.service;


/**
 * Prioritize a data object to be used in a {@link IPriorityQueue}
 * 
 * @see ch.zhaw.ba10_bsha_1.service.IPriorityQueue
 * 
 * @author Julian Hanhart, Dominik Giger
 */
public class Prioritized<Type> implements Comparable<Prioritized<Type>> {


	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------


	private Type data;
	private int priority;


	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------


	public Prioritized(Type data, int priority) {
		this.data = data;
		this.priority = priority;
	}
	
	
	//----------------------------------------------------------------------------
	// Implementation of the Comparable interface
	//----------------------------------------------------------------------------
	

	public int compareTo(Prioritized<Type> other) {
		int result = 0;
		if (this.priority != other.getPriority()) {
			result = (this.priority < other.getPriority()) ? 1 : -1;
		} else {
			result = 0;
		}
		return result;
	}


	//----------------------------------------------------------------------------
	// Getter-/Setter-methods
	//----------------------------------------------------------------------------


	public Type getData() {
		return data;
	}

	public void setData(Type data) {
		this.data = data;
	}

	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return data.toString() + "\t(Priority: " + priority + ")";
	}
}
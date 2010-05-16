package ch.zhaw.ba10_bsha_1;


/**
 * Implementation of a ring buffer.
 *
 * @author Julian Hanhart, Dominik Giger
 * @param <Type>
 */
public class RingBuffer<Type> {

	
	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	
	private Object[] slots;
	private int elementCount;
	private int readCursor;
	private int writeCursor;

	
	//---------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------

	
	public RingBuffer(int size) {
		if (size > 0) {
			slots = new Object[size];
			elementCount = 0;
			readCursor   = 0;
			writeCursor  = 0;
		} else {
			slots = null;
			elementCount = -1;
			readCursor   = -1;
			writeCursor  = -1;
		}
	}
	

	//---------------------------------------------------------------------------
	// Getter-methods
	//---------------------------------------------------------------------------

	
	public boolean isEmpty() {
		return (elementCount < 1);
	}
	
	public boolean isFull() {
		return (elementCount == slots.length);
	}
	
	public int getElementCount() {
		return elementCount;
	}


	//---------------------------------------------------------------------------
	// Buffer-management methods
	//---------------------------------------------------------------------------

	
	/**
	 * Get next element in buffer
	 */
	@SuppressWarnings("unchecked")
	public Type get() {
		Type result = (Type) slots[readCursor];
		if (slots[readCursor] != null) {
			slots[readCursor] = null;
			readCursor = (readCursor + 1) % slots.length;
			elementCount--;
		}
		return result;
	}
	
	/**
	 * Add object to the buffer (is not added if buffer is full)
	 * 
	 * @param object
	 */
	public void add(Type object) {
		if (elementCount != slots.length) {
			slots[writeCursor] = object;
			writeCursor = (writeCursor + 1) % slots.length;
			elementCount++;
		}
	}

	/**
	 * Clear all elements from buffer
	 */
	public void clear() {
		for (int i = 0; i < slots.length; i++) {
			slots[i] = null;
		}
		elementCount = 0;
		readCursor   = 0;
		writeCursor  = 0;
	}
}

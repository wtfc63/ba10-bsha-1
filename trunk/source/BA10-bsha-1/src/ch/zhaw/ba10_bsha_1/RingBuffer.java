package ch.zhaw.ba10_bsha_1;


public class RingBuffer<E> {
	
	
	private Object[] slots;
	private int elementCount;
	private int readCursor;
	private int writeCursor;
	
	
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

	public void add(E object) {
		if (elementCount != slots.length) {
			slots[writeCursor] = object;
			writeCursor = (writeCursor + 1) % slots.length;
			elementCount++;
		}
	}
	
	public E get() {
		E result = (E) slots[readCursor];
		if (slots[readCursor] != null) {
			slots[readCursor] = null;
			readCursor = (readCursor + 1) % slots.length;
			elementCount--;
		}
		return result;
	}
	
	public boolean isEmpty() {
		return (elementCount < 1);
	}
	
	public boolean isFull() {
		return (elementCount == slots.length);
	}
	
	public int getElementCount() {
		return elementCount;
	}

	public void clear() {
		for (int i = 0; i < slots.length; i++) {
			slots[i] = null;
		}
		elementCount = 0;
		readCursor   = 0;
		writeCursor  = 0;
	}
}

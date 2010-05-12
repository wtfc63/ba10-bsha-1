package ch.zhaw.ba10_bsha_1.strategies;


import java.util.Collection;

import ch.zhaw.ba10_bsha_1.TouchPoint;


public interface IPathSmoothingStrategy {
	public Collection<TouchPoint> smoothePath(Collection<TouchPoint> points);
	public String toString();
}
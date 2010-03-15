package ch.zhaw.ba10_bsha_1.fingerpad;


import java.util.Collection;


public interface IPathSmoothingStrategy {
	public Collection<TouchPoint> smoothePath(Collection<TouchPoint> points);
	public String toString();
}
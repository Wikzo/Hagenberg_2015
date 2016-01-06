package net.gustavdahl.compositeiterator;

public class NullIterator implements Iterator {
	public boolean hasNext() { return false; }
	public Component next() { return null; }
	public Component current()	{return null;}
}
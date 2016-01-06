package net.gustavdahl.compositeiterator;

public interface Iterator
{
	boolean hasNext(); 
	Component next();
	Component current();
}

package net.gustavdahl.compositeiterator;

import java.util.ArrayList;

public class ArrayListIterator implements Iterator
{
	ArrayList<Component> list;
	int pos;

	public ArrayListIterator(ArrayList<Component> list)
	{
		this.list = list;
	}

	public boolean hasNext()
	{ // *\label{al_hasNext}
		return pos < list.size();
	}

	public Component next()
	{ // *\label{al_next}
		if (!hasNext())
			return null;
		return list.get(pos++);
	}

	@Override
	public Component current()
	{
		return list.get(pos);
	}
}

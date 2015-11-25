package net.gustavdahl.compositeiterator;

import java.util.ArrayList;

public class Leaf extends Component implements Iterator
{
	public Leaf(String name)
	{
		super(name);
	}

	@Override
	public void printInternal(int indent)
	{
		printIndent(indent);
		printName();
	}

	@Override
	public boolean hasNext()
	{
		return false;
	}

	@Override
	public Component next()
	{
		return null;
	}

	@Override
	protected void printName()
	{
		System.out.println("[" + this.getName() + "]");
	}

	@Override
	public net.gustavdahl.compositeiterator.Iterator Iterator()
	{
		NullIterator a = new NullIterator();
		return a;
	}

}

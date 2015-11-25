package net.gustavdahl.compositeiterator;

import java.util.ArrayList;
import java.util.List;

public class Composite extends Component
{
	private ArrayList<Component> _componentList = new ArrayList<Component>();

	public Composite(String name)
	{
		super(name);
	}

	// Internal Iteration
	public void printInternal(int indent)
	{
		printIndent(indent);
		printName();
		
		for (Component c : _componentList)
		{
			c.printInternal(indent + 1);
		}
	}

	// External iteration
	public Iterator Iterator()
	{
		ArrayListIterator a = new ArrayListIterator((ArrayList<Component>) _componentList);
		return a;
	}
	
	@Override
	protected void printName()
	{
		System.out.println(this.getName() + ":");
	}

	public void add(Component leaf)
	{
		_componentList.add(leaf);
	}
}

package net.gustavdahl.compositeiterator;

import java.util.ArrayList;
import java.util.List;

public class Composite extends Component
{
	private List<Component> _componentList = new ArrayList<Component>();

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
			int extra = 0;
			if (c instanceof Composite)
				extra = 1;

			c.printInternal(indent + 1);
		}
	}
	
	void ExternalPrint()
	{
		/*
		 * ArrayListIterator a = ArrayIterator();
		 * 
		 * printIndent(indent); System.out.println(this.getName());
		 * 
		 * 
		 * for (Component c : a.list) { printIndent(indent);
		 * System.out.println(c.getName()); }
		 * 
		 * if (a.hasNext()) { int extraIndent = (a.next() instanceof Composite)
		 * ? 1 : 0; System.out.println("HasNext: TRUE: " + extraIndent);
		 * 
		 * Component next = a.next(); for (Component c : next._componentList) {
		 * c.printIndent(1); System.out.println(c.getName()); }
		 * 
		 * a.next().print(indent + extraIndent); }
		 */

		/*
		 * if (this.hasNext()) { System.out.println("has next: TRUE");
		 * 
		 * int extraIndent = (this.next() instanceof Composite) ? 1 : 0;
		 * //System.out.println("Extra: " + extraIndent);
		 * 
		 * this.next().print(indent + extraIndent); }
		 */

		/*
		 * if (this.hasNext()) { int extraIndent = (this.next() instanceof
		 * Composite) ? 1 : 0; this.next().print(indent + extraIndent);
		 * 
		 * } else if (this.hasNext() == false) { printIndent(indent); for
		 * (Component c : list) System.out.println(c.getName()); }
		 */
	}

	@Override
	protected void printName()
	{
		System.out.println(this.getName() + ":");
	}

	// External iterator

	public void add(Component leaf1)
	{
		_componentList.add(leaf1);
	}

	public ArrayListIterator ArrayIterator()
	{
		return new ArrayListIterator((ArrayList<Component>) _componentList);
	}
}

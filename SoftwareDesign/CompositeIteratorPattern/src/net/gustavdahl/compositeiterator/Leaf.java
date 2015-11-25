package net.gustavdahl.compositeiterator;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void printName()
	{
		System.out.println("[" + this.getName() + "]");
	}

}

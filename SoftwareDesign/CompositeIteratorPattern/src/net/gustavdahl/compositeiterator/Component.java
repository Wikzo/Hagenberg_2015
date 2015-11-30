package net.gustavdahl.compositeiterator;

public abstract class Component
{
	String name;

	public Component(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public abstract void printInternal(int indent);

	public abstract Iterator Iterator();

	protected abstract void printName();

	void printIndent(int n)
	{
		for (int i = 0; i < n; i++)
			System.out.print(" ");
	}

}

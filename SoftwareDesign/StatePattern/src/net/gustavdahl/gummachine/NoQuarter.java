package net.gustavdahl.gummachine;

public class NoQuarter implements IGumState
{

	public NoQuarter()
	{
		System.out.println("\nState is now: NO QUARTER");
	}

	@Override
	public IGumState HandleInput(Input input, GumMachine machine)
	{
		switch (input)
		{
		case InsertQuarter:
			return new HasQuarter();
		default:
			System.out.println("\nERROR - you need to insert a quarter to continue!");
			return this;
		}
	}

}

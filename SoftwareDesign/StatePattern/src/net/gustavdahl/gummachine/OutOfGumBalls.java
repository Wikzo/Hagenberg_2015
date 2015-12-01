package net.gustavdahl.gummachine;

public class OutOfGumBalls implements IGumState
{

	public OutOfGumBalls()
	{
		System.out.println("\nState is now: OUT OF GUMBALLS!");
	}

	@Override
	public IGumState HandleInput(Input input, GumMachine machine)
	{
		switch (input)
		{
		case Refill:
			machine.Refill(5);
			return new NoQuarter();
		default:
			System.out.println("\nERROR - you need to refill the machine!");
			return this;
		}
	}

}

package net.gustavdahl.gummachine;

public class GumBallSold implements IGumState
{

	
	public GumBallSold(GumMachine machine)
	{
		machine._gumLeft--;
			
		System.out.println("\nState is now: GUMBALL SOLD!");
		System.out.println("Amount of gums left: " + machine._gumLeft);
	}

	@Override
	public IGumState HandleInput(Input input, GumMachine machine)
	{
		switch (input)
		{
		case Dispense:
			if (machine._gumLeft - 1 <= 0)
				return new OutOfGumBalls();
			else
			{
				machine._gumLeft -= 1;
				return new NoQuarter();
			}

		default:
			System.out.println("\nERROR - you need to dispense the machine!");
			return this;
		}
	}

}

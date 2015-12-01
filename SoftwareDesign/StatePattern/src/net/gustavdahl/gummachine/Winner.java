package net.gustavdahl.gummachine;

public class Winner implements IGumState
{

	public Winner(GumMachine machine)
	{
		System.out.println("WE HAVE A WINNER!!!");
		
		if (machine._gumLeft - 2 > 0)
		{
			System.out.println("Dispense to get your extra gum ball :D");
		}
		else
			System.out.println("... but there are no more gum balls left. Sorry ...");
		
	}

	@Override
	public IGumState HandleInput(Input input, GumMachine machine)
	{
		switch (input)
		{
		case Dispense:
			if (machine._gumLeft <= 0)
				return new OutOfGumBalls();
			else
			{
				machine._gumLeft -= 2;
				return new NoQuarter();
			}
			
		default:
			System.out.println("\nERROR - you need to dispense the machine!");
			return this;
		}
	}

}

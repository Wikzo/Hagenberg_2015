package net.gustavdahl.gummachine;

import java.util.Random;

public class HasQuarter implements IGumState
{

	public HasQuarter()
	{
		System.out.println("\nState is now: HAS QUARTER");
	}

	@Override
	public IGumState HandleInput(Input input, GumMachine machine)
	{
		switch (input)
		{
		case EjectQuarter:
			return new NoQuarter();
		case TurnCrank:
			
			Random rn = new Random();
			int number = rn.nextInt(11);
			
			System.out.println("Random number: " + number);
			if (number == 10)
				return new Winner(machine);
			else
				return new GumBallSold(machine);
		default:
			System.out.println("\nERROR - you need to either turn the crank or eject the quarter to continue!");
				return this;
		}
	}

}

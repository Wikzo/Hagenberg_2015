package net.gustavdahl.gummachine;

import java.util.Random;
import java.util.Scanner;

public class Main
{

	public Main()
	{
	}

	public static void main(String[] args)
	{
		GumMachine machine = new GumMachine(5);
		
		Scanner keyboard = new Scanner(System.in);
		
		String possibleCommands = "--------POSSIBLE COMMANDS:-------\ninsert, turn, eject, dispense, refill\n\n";
		System.out.println(possibleCommands);
		
		boolean exit = false;
		while (!exit)
		{
			
			System.out.println("Enter command: ");
			
			String input = keyboard.nextLine();
			
			if (input.equals("exit"))
				exit = true;
			
			machine.GetInput(input);
			
		}

	}

}

package net.gustavdahl.gummachine;

enum Input
{
	InsertQuarter,
	EjectQuarter,
	TurnCrank,
	Dispense,
	Refill
}

public class GumMachine
{

	private IGumState _myState;
	protected int _gumLeft;
	
	public GumMachine(int gums)
	{
		Refill(gums);
		System.out.println("Gum Machine created! Has " + _gumLeft + " gums at the moment.");
		_myState = new NoQuarter();
	}
	
	void GetInput(String input)
	{
		String i = input.toLowerCase();
		Input inputCommand = null;
		
		if (i.equals("insert"))
			inputCommand = Input.InsertQuarter;
		else if (i.equals("eject"))
			inputCommand = Input.EjectQuarter;
		else if (i.equals("turn"))
			inputCommand = Input.TurnCrank;
		else if (i.equals("dispense"))
			inputCommand = Input.Dispense;
		else if (i.equals("refill"))
			inputCommand = Input.Refill;
		
		if (inputCommand == null)
			System.out.println("ERROR: invalid input command. Please try again!\n\n");
		else
			HandleInput(inputCommand);
	}
	
	void HandleInput(Input input)
	{
		//IGumState temp = _myState;
		
		_myState = _myState.HandleInput(input, this);
	}
	
	protected void Refill(int number)
	{
		this._gumLeft = number;
	}
	
	
	

}

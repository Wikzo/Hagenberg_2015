
public class FordFactory implements ICarFactory
{
	@Override
	public Car CreateCarA(String color)
	{
		Car ford = new FordCar();
		ford.Color = color;
		return ford;
	}

	@Override
	public Car CreateCarB(String color)
	{
		return null;
	}

}

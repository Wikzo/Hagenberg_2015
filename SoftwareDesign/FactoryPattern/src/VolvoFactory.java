
public class VolvoFactory implements ICarFactory
{

	@Override
	public Car CreateCarA(String color)
	{
		VolvoCar volvo = new VolvoCar();
		volvo.Color = color;
		return volvo;
	}

	@Override
	public Car CreateCarB(String color)
	{
		return null;
	}

}

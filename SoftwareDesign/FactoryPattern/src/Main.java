
public class Main
{
	// Design Patterns - Factory Method in 5 minutes
	// https://youtu.be/L7MRQIVl3M0
	public static void main(String[] args)
	{
		ICarFactory fordFactory = new FordFactory();
		ICarFactory volvoFactory = new VolvoFactory();

		Car fordCar = fordFactory.CreateCarA("Red");
		Car volvoCar = volvoFactory.CreateCarA("Black");
		
		System.out.println("Maker: " + fordCar.Maker + ", model: " + fordCar.Model + ", color: " + fordCar.Color);
		System.out.println("Maker: " + volvoCar.Maker + ", model: " + volvoCar.Model + ", color: " + volvoCar.Color);
		
	}

}


public class Main
{

	// Design Patterns - The Builder Pattern in 5 minutes
	// https://youtu.be/0th9gyP3s-I

	public static void main(String[] args)
	{
		Car fordFiesta = new FordFiestaBuilder()
				.AddDoors(5)
				.AddGPS()
				.BuildCar();
		
		Car fordFiesta2 = new FordFiestaBuilder()
				.BuildCar();
		
		System.out.println(fordFiesta);
		System.out.println(fordFiesta2);

	}

}


public class Car
{
	
	String _maker, _model, _color, _engineSize;
	int _doors;
	boolean _hasGPS;

	public Car(String maker, String model, String color, String engineSize, int doors, boolean hasGPS)
	{
		_maker = maker;
		_model = model;
		_color = color;
		_engineSize = engineSize;
		_doors = doors;
		_hasGPS = hasGPS;
	}
	
	@Override
	public String toString()
	{
		String s = "Maker: " + _maker + ", model: " + _model + ", color: " + _color + ", engine size: " + _engineSize
				+ ", doors: " + _doors + ", has GPS: " + _hasGPS;
		
		return s;
	}

}

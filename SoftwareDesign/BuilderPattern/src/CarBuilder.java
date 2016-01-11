
public class CarBuilder
{
	String _maker, _model, _color, _engineSize;
	int _doors;
	boolean _hasGPS;
	
	public CarBuilder Maker(String maker)
	{
		_maker = maker;
		return this;
	}
	
	public CarBuilder Model(String model)
	{
		_model = model;
		return this;
	}
	
	public CarBuilder Color(String color)
	{
		_color = color;
		return this;
	}
	
	public CarBuilder EngineSize(String engineSize)
	{
		_engineSize = engineSize;
		return this;
	}
	
	public CarBuilder AddDoors(int doors)
	{
		_doors = doors;
		return this;
	}
	
	public CarBuilder AddGPS()
	{
		_hasGPS = true;
		return this;
	}
	
	public Car BuildCar()
	{
		return new Car(_maker, _model, _color, _engineSize, _doors, _hasGPS);
	}
	
	
	
	

}

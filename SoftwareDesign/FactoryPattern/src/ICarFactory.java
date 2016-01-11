
public interface ICarFactory
{
	Car CreateCarA(String color);
	Car CreateCarB(String color);
	
	// single "products" = factory
	// multiple "products" = abstract factory
}

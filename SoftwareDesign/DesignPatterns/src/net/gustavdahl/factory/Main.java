package net.gustavdahl.factory;

public class Main
{
	public static void main(String[] args)
	{
		NewYorkFactory newYorkFactory = new NewYorkFactory();
		ChicagoFactory chicagoFactory = new ChicagoFactory();
		
		// cheese
		BasePizza newYorkCheese = newYorkFactory.CreateCheesePizza();
		BasePizza chicagoCheese = chicagoFactory.CreateCheesePizza();
		
		// veggie
		BasePizza newYorkVeggie = newYorkFactory.CreateVeggiePizza();
		BasePizza chicagoVeggie = chicagoFactory.CreateVeggiePizza();
		
		// pepporoni
		BasePizza newYorkPepporoni = newYorkFactory.CreatePepporoniPizza();
		BasePizza chicagoPepporoni = chicagoFactory.CreatePepporoniPizza();
	}

}

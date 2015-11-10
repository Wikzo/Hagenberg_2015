package net.gustavdahl.factory;

public class Main
{
	public static void main(String[] args)
	{
		NewYorkFactory newYorkFactory = new NewYorkFactory();
		ChicagoFactory chicagoFactory = new ChicagoFactory();
		
		// cheese
		CheesePizza newYorkCheese = (CheesePizza) newYorkFactory.CreateCheesePizza();
		CheesePizza chicagoCheese = (CheesePizza) chicagoFactory.CreateCheesePizza();
		
		// veggie
		VeggiePizza newYorkVeggie = (VeggiePizza) newYorkFactory.CreateVeggiePizza();
		VeggiePizza chicagoVeggie = (VeggiePizza) chicagoFactory.CreateVeggiePizza();
		
		// pepporoni
		PepporoniPizza newYorkPepporoni = (PepporoniPizza) newYorkFactory.CreatePepporoniPizza();
		PepporoniPizza chicagoPepporoni = (PepporoniPizza) chicagoFactory.CreatePepporoniPizza();
	}

}

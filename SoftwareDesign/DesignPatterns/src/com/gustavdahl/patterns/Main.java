package com.gustavdahl.patterns;

public class Main
{

	public Main()
	{
		
	}

	public static void main(String[] args)
	{
		NewYorkFactory newYorkFactory = new NewYorkFactory();
		ChicagoFactory chicagoFactory = new ChicagoFactory();
		
		CheesePizza newYorkCheese = (CheesePizza) newYorkFactory.CreateCheesePizza();
		
		System.out.println("----\n");
		
		//CheesePizza chicagoCheese = (CheesePizza) chicagoFactory.CreateCheesePizza();
		
		VeggiePizza newYorkVeggie = (VeggiePizza) newYorkFactory.CreateVeggiePizza();
		
		System.out.println("----\n");
		
		
		VeggiePizza chicagoVeggie = (VeggiePizza) chicagoFactory.CreateVeggiePizza();
	}

}

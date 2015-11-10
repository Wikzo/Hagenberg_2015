package com.gustavdahl.patterns;

public class ChicagoFactory implements IAbstractFactory
{

	public ChicagoFactory()
	{
	}

	@Override
	public BasePizza CreateCheesePizza()
	{
		
		BasePizza p = new CheesePizza("NewYork Cheese", 5);
		p.SetIngredient(" Plum Tomato Sauce").SetIngredient("Mozzarella Cheese");
		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
		
	}

	@Override
	public BasePizza CreateVeggiePizza()
	{		
		BasePizza p = new VeggiePizza("NewYork Veggie", 12);
		p.SetIngredient("Plum Tomato Sauce").SetIngredient("Mozzarella Cheese").SetIngredient("Eggplan").SetIngredient("Spinach").SetIngredient("Black Olives");
		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
	}

	@Override
	public BasePizza CreatePepporoniPizza()
	{		
		BasePizza p = new PepporoniPizza("NewYork Veggie", 23);
		p.SetIngredient("Plum Tomato Sauce").SetIngredient("Mozzarella Cheese")
		.SetIngredient("Eggplan").SetIngredient("Spinach").SetIngredient("Black Olives").SetIngredient("Pepporoni");
		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
	}

}

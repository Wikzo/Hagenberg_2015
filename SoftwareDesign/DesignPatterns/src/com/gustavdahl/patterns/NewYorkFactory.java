package com.gustavdahl.patterns;

public class NewYorkFactory implements IAbstractFactory
{

	public NewYorkFactory()
	{
	}

	@Override
	public BasePizza CreateCheesePizza()
	{
		BasePizza p = new CheesePizza("Chicago Cheese", 10);
		p.SetIngredient("Marinara Sauce").SetIngredient("Reggiano Cheese");
		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
		
	}

	@Override
	public BasePizza CreateVeggiePizza()
	{
		BasePizza p = new VeggiePizza("Chicago Veggie", 30);
		p.SetIngredient("Marinara Sauce").SetIngredient("Reggiano Cheese").SetIngredient("Mushrooms").SetIngredient("Onions").SetIngredient("Red Peppers");

		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
	}

	@Override
	public BasePizza CreatePepporoniPizza()
	{
		BasePizza p = new PepporoniPizza("Chicago Veggie", 40);
		p.SetIngredient("/Marinara Sauce").SetIngredient("Reggiano Cheese").SetIngredient("Mushrooms").SetIngredient("Onions").SetIngredient("Pepperoni");
		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
	}

}

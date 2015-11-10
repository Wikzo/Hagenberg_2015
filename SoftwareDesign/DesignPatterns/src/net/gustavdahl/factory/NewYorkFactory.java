package net.gustavdahl.factory;

public class NewYorkFactory implements IAbstractFactory
{

	public NewYorkFactory()
	{
	}

	@Override
	public BasePizza CreateCheesePizza()
	{
		BasePizza p = new CheesePizza("CHICAGO CHEESE PIZZA", 10);
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
		BasePizza p = new VeggiePizza("CHICAGO VEGGIE PIZZA", 30);
		p.SetIngredient("Marinara Sauce").SetIngredient("Reggiano Cheese").SetIngredient("Mushrooms")
				.SetIngredient("Onions").SetIngredient("Red Peppers");

		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
	}

	@Override
	public BasePizza CreatePepporoniPizza()
	{
		BasePizza p = new PepporoniPizza("CHICAGO PEPPORONI PIZZA", 40);
		p.SetIngredient("Marinara Sauce").SetIngredient("Reggiano Cheese").SetIngredient("Mushrooms")
				.SetIngredient("Onions").SetIngredient("Pepperoni");
		p.Prepare();
		p.Bake();
		p.Cut();
		p.Box();
		return p;
	}

}

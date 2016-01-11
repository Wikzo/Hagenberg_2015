package net.gustavdahl.factory;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePizza implements IMakePizza
{

	public List<String> Ingredients; 
	public String Name;
	public int Price;
	
	public BasePizza(String name, int price)
	{
		this.Name = name;
		this.Price = price;
		
		Ingredients = new ArrayList<>();
	}

	@Override
	public void Prepare()
	{
		System.out.println("----------------------------");
		System.out.println("Preparing the " + Name);
		System.out.println("Ingredients: " + Ingredients.toString());		
	}
	
	@Override
	public BasePizza SetIngredient(String ingredient)
	{
		Ingredients.add(ingredient);
		
		return this;
	}

	@Override
	public void Bake()
	{
		System.out.println("Baking the " + Name + "...");
	}

	@Override
	public void Cut()
	{
		System.out.println("Cutting the " + Name + "...");
	}

	@Override
	public void Box()
	{
		System.out.println("Delivering the " + Name);
		System.out.println("Price: " + Price);
		
		System.out.println("----------------------------\n");
	}

	

}

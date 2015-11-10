package net.gustavdahl.factory;

public interface IMakePizza
{
	void Prepare();
	void Bake();
	void Cut();
	void Box();
	BasePizza SetIngredient(String ingredient);
}

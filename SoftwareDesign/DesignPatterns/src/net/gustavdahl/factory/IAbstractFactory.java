package net.gustavdahl.factory;

public interface IAbstractFactory
{
	BasePizza CreateCheesePizza();
	BasePizza CreateVeggiePizza();
	BasePizza CreatePepporoniPizza();
}

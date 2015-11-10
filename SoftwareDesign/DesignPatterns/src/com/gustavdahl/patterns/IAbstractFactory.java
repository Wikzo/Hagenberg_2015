package com.gustavdahl.patterns;

public interface IAbstractFactory
{
	BasePizza CreateCheesePizza();
	BasePizza CreateVeggiePizza();
	BasePizza CreatePepporoniPizza();
}

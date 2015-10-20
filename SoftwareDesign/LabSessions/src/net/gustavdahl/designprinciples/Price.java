package net.gustavdahl.designprinciples;

abstract class Price
{
	public abstract int GetChargeInCents(int days);

	public int GetBonus(int days)
	{
		return 1;
	}
}

class RegularPrice extends Price
{

	@Override
	public int GetChargeInCents(int days)
	{
		return 200 + (days > 2 ? (days - 2) * 150 : 0);
	}

}

class NewPrice extends Price
{

	@Override
	public int GetChargeInCents(int days)
	{
		return days * 300;
	}
	
	@Override
	public int GetBonus(int days)
	{
		return days > 1 ? 2 : 1;
	}
	
}

class KidsPrice extends Price
{

	@Override
	public int GetChargeInCents(int days)
	{
		return 150 + (days > 3 ? (days - 3) * 150 : 0);
	}
	
}
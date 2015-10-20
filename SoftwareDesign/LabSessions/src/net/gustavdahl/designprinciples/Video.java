package net.gustavdahl.designprinciples;

public class Video
{
	private String _title;
	private Price _price;

	public Video(String title, Price price)
	{
		this._title = title;
		this._price = price;
	}

	public String GetTitle()
	{
		return this._title;
	}

	public void SetPrice(Price price)
	{
		this._price = price;
	}


	public Price GetPrice()
	{
		return this._price;
	}

	public int GetChargeInCents(int days)
	{
		return _price.GetChargeInCents(days);
	}

	public int GetBonus(int days)
	{
		return _price.GetBonus(days);
	}

}

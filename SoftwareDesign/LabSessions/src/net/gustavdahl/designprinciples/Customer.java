package net.gustavdahl.designprinciples;

import java.util.ArrayList;
import java.util.List;

import com.sun.beans.util.Cache.Kind;

public class Customer
{

	private String _name;
	private List<Rental> _rentals = new ArrayList<Rental>();

	public Customer(String name)
	{
		this._name = name;
	}

	public String GetName()
	{
		return this._name;
	}

	public void AddRental(Rental rental)
	{
		this._rentals.add(rental);
	}

	public String MakeInvoice()
	{
		int priceInCents = 0;
		int totalInCents = 0;
		int bonus = 0;

		StringBuffer sb = new StringBuffer("Customer: " + _name + "\n");

		for (Rental r : _rentals)
		{	
			priceInCents = r.GetChargeInCents();
			bonus += r.GetBonus();

			sb.append(r.GetVideo().GetTitle() + "\t");
			sb.append(centToPriceString(priceInCents) + "\t");
			totalInCents += priceInCents;
		}
		sb.append("\nTotal: " + centToPriceString(totalInCents));
		sb.append("\nBonus: " + bonus);

		return sb.toString();
	}

	private String centToPriceString(int cent)
	{
		return String.format("%1$d.%2$02d", cent / 100, cent % 100);
	}

	
	

}

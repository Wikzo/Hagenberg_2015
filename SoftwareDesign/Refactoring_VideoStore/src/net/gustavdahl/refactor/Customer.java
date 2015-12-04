package net.gustavdahl.refactor;

import java.util.LinkedList;
import java.util.List;

public class Customer
{
	private String name;
	private List<Rental> rentals = new LinkedList<Rental>();

	public Customer(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void addRental(Rental r)
	{
		rentals.add(r);
	}

	String showFiguresForRental(Rental r)
	{
		return r.getVideo().getTitle() + "\t" + centsToPriceString(r.computeChargeInCents()) + "\n";
	}

	public String makeInvoice()
	{
		int totalInCents = 0;
		int bonus = 0;
		StringBuffer sb = new StringBuffer("Customer: " + name + "\n");
		for (Rental r : rentals)
		{
			bonus += r.addBonus();

			// show figures for this rental
			sb.append(showFiguresForRental(r));
			totalInCents += r.computeChargeInCents();
		}

		// add footer lines
		sb.append("\nTotal:\t" + centsToPriceString(totalInCents));
		sb.append("\nBonus:\t" + bonus);

		return sb.toString();
	}

	private String centsToPriceString(int cents)
	{
		return String.format("%1$d.%2$02d", cents / 100, cents % 100);
	}
}

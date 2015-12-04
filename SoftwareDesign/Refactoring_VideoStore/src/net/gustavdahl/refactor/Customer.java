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

	public String makeInvoice()
	{
		int totalInCents = 0;
		int bonus = 0;
		StringBuffer sb = new StringBuffer("Customer: " + name + "\n");
		for (Rental r : rentals)
		{
			int priceInCents = computeChargeInCents(r);

			// add bonus
			bonus++;
			// add bonus for a two day new release rental
			if (r.getVideo().getKind() == Video.NEW_RELEASE && r.getDays() > 1)
				bonus++;

			// show figures for this rental
			sb.append(r.getVideo().getTitle() + "\t");
			sb.append(centsToPriceString(priceInCents) + "\n");
			totalInCents += priceInCents;
		}

		// add footer lines
		sb.append("\nTotal:\t" + centsToPriceString(totalInCents));
		sb.append("\nBonus:\t" + bonus);

		return sb.toString();
	}

	private int computeChargeInCents(Rental r)
	{
		// determine amounts for each line
		int result = 0;
		int days = r.getDays();
		switch (r.getVideo().getKind())
		{
		case Video.REGULAR:
			result += 200;
			if (days > 2)
				result += (days - 2) * 150;
			break;
		case Video.NEW_RELEASE:
			result += days * 300;
			break;
		case Video.CHILDRENS:
			result += 150;
			if (days > 3)
				result += (days - 3) * 150;
			break;
		}
		return result;
	}

	private String centsToPriceString(int cents)
	{
		return String.format("%1$d.%2$02d", cents / 100, cents % 100);
	}
}

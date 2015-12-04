package videorentalstore.orig;

import java.util.LinkedList;
import java.util.List;

class Customer {
	private String name;
	private List<Rental> rentals = new LinkedList<Rental>();
	
	public Customer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addRental(Rental r) {
		rentals.add(r);
	}
	
	public String makeInvoice() {
		int total = 0;  // prices in Euro cent
		int bonus = 0;
		StringBuffer sb = new StringBuffer("Customer: " + name + "\n");
		for (Rental r : rentals) {
			// determine amounts for each line
			int price = 0;
			int kind = r.getVideo().getKind();
			int days = r.getDays();
			switch (kind) {
				case Video.REGULAR:
					price += 200;
					if (days > 2) price += (days - 2) * 150;
					break;
				case Video.NEW_RELEASE:
					price += days * 300;
					break;
				case Video.CHILDRENS:
					price += 150;
					if (days > 3) price += (days - 3) * 150;
					break;
			}
			
			// add bonus
			bonus++;
			// add bonus for a two day new release rental
			if (kind == Video.NEW_RELEASE && days > 1) bonus++;
			
			// show figures for this rental
			sb.append(r.getVideo().getTitle() + "\t");
			sb.append(centsToPriceString(price) + "\n");
			total += price;
		}
	
		// add footer lines
		sb.append("\nTotal:\t" + centsToPriceString(total));
		sb.append("\nBonus:\t" + bonus);
		
		return sb.toString();
	}
	
	private String centsToPriceString(int cents) {
		return String.format("%1$d.%2$02d", cents / 100, cents % 100);
	}
}

package net.gustavdahl.refactor.unittest;

import java.io.Console;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.gustavdahl.refactor.Customer;
import net.gustavdahl.refactor.Rental;
import net.gustavdahl.refactor.Video;

@SuppressWarnings("unused")
public class CustomerTests
{
	final String CUSTOMER_NAME = "John Doe";
	final String INVOICE_HEADER = "Customer: " + CUSTOMER_NAME + "\n";

	private Customer _customer;

	@Before
	public void Setup()
	{
		_customer = new Customer(CUSTOMER_NAME);
	}

	@Test
	public void getName()
	{
		String expectedName = CUSTOMER_NAME;
		String actualName = _customer.getName();

		Assert.assertEquals(expectedName, actualName);
	}

	@Test
	public void emptyInvoice()
	{
		String expectedInvoice = INVOICE_HEADER + "\nTotal:\t0.00" + "\nBonus:\t0";
		String actualInvoice = _customer.makeInvoice();

		Assert.assertEquals(expectedInvoice, actualInvoice);
	}

	@Test
	public void invoiceWithOneRental()
	{
		addRental("Lion King", Video.NEW_RELEASE, 3);

		String expectedInvoice = INVOICE_HEADER + "Lion King\t9.00\n" + "\nTotal:\t9.00" + "\nBonus:\t2";
		String actualStatement = _customer.makeInvoice();

		Assert.assertEquals(expectedInvoice, actualStatement);
	}

	private void addRental(String videoTitle, int kind, int days)
	{
		// just a small helper function instead of manually creating new rentals many times
		
		_customer.addRental(new Rental(new Video(videoTitle, kind), days));
	}

	@Test
	public void invoiceWithManyRentals()
	{
		addRental("Regular 1", Video.REGULAR, 2);
		addRental("New Release 2", Video.NEW_RELEASE, 1);
		addRental("Childrens 3", Video.CHILDRENS, 3);

		String expectedInvoice = INVOICE_HEADER + "Regular 1\t2.00\n" + "New Release 2\t3.00\n" + "Childrens 3\t1.50\n"
				+ "\nTotal:\t6.50" + "\nBonus:\t3";
		String actualInvoice = _customer.makeInvoice();

		Assert.assertEquals(expectedInvoice, actualInvoice);
	}
}
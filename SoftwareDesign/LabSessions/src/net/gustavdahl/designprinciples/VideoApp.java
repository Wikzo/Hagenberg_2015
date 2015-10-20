package net.gustavdahl.designprinciples;

import java.util.HashMap;

public class VideoApp
{
	public static final Price Regular = new RegularPrice();
	public static final Price NewRelease = new NewPrice();
	public static final Price Kids = new KidsPrice();

	private HashMap<String, Customer> customers = new HashMap<String, Customer>();
	private HashMap<String, Video> videos = new HashMap<String, Video>();

	private void addVideo(Video video)
	{
		videos.put(video.GetTitle(), video);
	}

	private void addCustomer(Customer customer)
	{
		customers.put(customer.GetName(), customer);
	}

	public static void main(String[] args)
	{
		VideoApp vStore = new VideoApp();
		
		Customer customer = new Customer("John Doe");
		vStore.addCustomer(customer);
		
		Video v1 = new Video("Star Trek", new RegularPrice());
		Video v2 = new Video("Terminator IX", new NewPrice());
		Video v3 = new Video("The Lion King", new KidsPrice());
		
		vStore.addVideo(v1);
		vStore.addVideo(v2);
		vStore.addVideo(v3);
		
		customer.AddRental(new Rental(v1, 3));
		customer.AddRental(new Rental(v2, 2));
		customer.AddRental(new Rental(v3, 4));
		
		System.out.println(customer.MakeInvoice());
	}
}
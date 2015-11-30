
public class Main {

	public static void main(String[] args) {
		//COFFEE1
		Coffee coffee1 = new HouseBlend();
		System.out.println(coffee1.getCost());
		
		coffee1 = new SteamedMilk(coffee1);
		System.out.println(coffee1.getCost());
		
		coffee1 = new SteamedMilk(coffee1);
		System.out.println(coffee1.getCost());
		System.out.println(coffee1.getDescription());
		
		//COFFEE2
		Coffee coffee2 = new Espresso();
		System.out.println(coffee2.getCost());
		
		coffee2 = new SteamedMilk(coffee2);
		System.out.println(coffee2.getCost());
		System.out.println(coffee2.getDescription());
	}

}


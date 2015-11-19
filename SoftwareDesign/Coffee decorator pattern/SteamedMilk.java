
public class SteamedMilk extends CondimentDecorator {

	SteamedMilk(Coffee c) {
		super(c);
	}
	
	@Override
	int getCost(){
		return this.myCoffee.getCost() + 1;
	}

	@Override
	public String getDescription() {
		return this.myCoffee.getDescription() + ", SteamedMilk";
	}
}

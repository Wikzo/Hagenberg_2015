
public abstract class CondimentDecorator extends Coffee {

	//Association (diamond) START
	Coffee myCoffee;
	
	CondimentDecorator(Coffee c){
		this.myCoffee = c;
	}
	//Association (diamond)	END
	
	
	int getCost(){
		return myCoffee.getCost();
		
	}
}


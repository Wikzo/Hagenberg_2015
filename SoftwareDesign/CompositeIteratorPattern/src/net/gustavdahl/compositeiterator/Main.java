package net.gustavdahl.compositeiterator;

public class Main
{
	Composite composite1 = new Composite("Composite 1");
	Composite composite2 = new Composite("Composite 2");
	Composite composite3 = new Composite("Composite 3");
	Composite composite4 = new Composite("Composite 4");

	Component leaf1 = new Leaf("Leaf 1");
	Component leaf2 = new Leaf("Leaf 2");
	Component leaf3 = new Leaf("Leaf 3");
	Component leaf4 = new Leaf("Leaf 4");
	Component leaf5 = new Leaf("Leaf 5");
	Component leaf6 = new Leaf("Leaf 6");
	Component leaf7 = new Leaf("Leaf 7");
	Component leaf8 = new Leaf("Leaf 8");
	
	public static void main(String[] args)
	{
		Composite root = new Composite("Root");

		Composite composite1 = new Composite("Composite 1");
		Composite composite2 = new Composite("Composite 2");
		Composite composite3 = new Composite("Composite 3");
		Composite composite4 = new Composite("Composite 4");

		Component leaf1 = new Leaf("Leaf 1");
		Component leaf2 = new Leaf("Leaf 2");
		Component leaf3 = new Leaf("Leaf 3");
		Component leaf4 = new Leaf("Leaf 4");
		Component leaf5 = new Leaf("Leaf 5");
		Component leaf6 = new Leaf("Leaf 6");
		Component leaf7 = new Leaf("Leaf 7");
		Component leaf8 = new Leaf("Leaf 8");

		root.add(composite1);
		root.add(leaf1);
		root.add(composite2);

		composite1.add(leaf2);
		composite1.add(composite3);
		composite1.add(leaf3);

		composite2.add(leaf4);
		composite2.add(leaf5);

		composite3.add(leaf6);
		composite3.add(composite4);

		composite4.add(leaf7);
		composite4.add(leaf8);

		boolean useInternal = false;

		if (useInternal)
		{
			// INTERNAL
			System.out.println();
			System.out.println("+++++ Internal Iteration: printInternal +++++");

			composite4.printInternal(0);
			System.out.println("----------");
			composite3.printInternal(0);
			System.out.println("----------");
			composite2.printInternal(0);
			System.out.println("----------");
			composite1.printInternal(0);
			System.out.println("----------");

			root.printInternal(0);
		}

		else
		{
			// EXTERNAL
			System.out.println();
			System.out.println("+++++ External Iterator: printName +++++");

			// printName(composite4);
			// printName(composite3);
			// printName(composite2);
			// printName(composite1);

			printName(composite3);

		}

	}

	private static void printName(Composite root)
	{
		System.out.print(root.getName() + ":\n");
		Iterator iterator = root.Iterator();
		Component component = null;

		component = GetNextChild(root);
		component = GetNextChild(component);
		component = GetNextChild(component);



		/*
		 * while (iterator.hasNext()) { component = iterator.next();
		 * System.out.print("\n-" + component.getName() + ":\n"); }
		 */
		System.out.println();
		System.out.println();
	}

	private static Component GetNextChild(Component c)
	{		
		if (c == null)
			return null;
		
		Iterator iterator = c.Iterator();
		
		if (!iterator.hasNext())
		{
			System.out.println("No more childs!");
			return null;
		}

		Component component;
		component = iterator.next();
		System.out.print("-" + component.getName() + ":\n");
		
		return component;
	}
}

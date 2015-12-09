package net.gustavdahl.prime;

public class PrintPrimes
{

	public static void main(String[] args)
	{
		final int numberOfPrimes = 1000;
		final int rowsPerPage = 50;
		final int numberOfColumns = 4;
		final int tabWidth = 10;
		int primeNumbers[] = new int[numberOfPrimes];
		int PAGENUMBER = 1;
		int PAGEOFFSET = 0;
		int ROWOFFSET;
		
		primeNumbers[0] = 2;
		int primeCount = 1;
		int candidate = 1;
				
		for (int i = 1; i < numberOfPrimes; i++)
		{
			candidate = SetPrimeNumbers(primeNumbers, candidate, i);
		}

		while (PAGEOFFSET < numberOfPrimes)
		{
			System.out.println("The First " + numberOfPrimes + " Prime Numbers --- Page " + PAGENUMBER);
			System.out.println("");
			for (ROWOFFSET = PAGEOFFSET; ROWOFFSET < PAGEOFFSET + rowsPerPage; ROWOFFSET++)
			{
				for (int C = 0; C < numberOfColumns; C++)
					if (ROWOFFSET + C * rowsPerPage < numberOfPrimes)
						System.out.format("%" + tabWidth + "d", primeNumbers[ROWOFFSET + C * rowsPerPage]);
				System.out.println("");
			}
			System.out.println("\f");
			PAGENUMBER = PAGENUMBER + 1;
			PAGEOFFSET = PAGEOFFSET + rowsPerPage * numberOfColumns;
		}
	}

	private static int SetPrimeNumbers(int[] primeNumbers, int candidate, int i)
	{
		candidate = GetNextPrime(primeNumbers, i, candidate);
		primeNumbers[i] = candidate;
		return candidate;
	}

	private static int GetNextPrime(int[] primeNumbers, int primeCount, int candidate)
	{
		boolean isPrime;
		do
		{
			candidate = candidate + 2;
			int index = 0;
			while (index < primeCount && candidate % primeNumbers[index] > 0)
				index++;
			isPrime = index == primeCount;
		} while (!isPrime);
		return candidate;
	}
}
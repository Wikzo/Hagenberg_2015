
public class PrintPrimes
{

	public static void main(String[] args)
	{
		final int M = 1000;
		final int RR = 50;
		final int CC = 4;
		final int WW = 10;
		int P[] = new int[M];
		int PAGENUMBER;
		int PAGEOFFSET;
		int ROWOFFSET;
		int C;
		int I;
		int J;
		int K;
		boolean JPRIME;

		P[0] = 2;
		K = 1;
		J = 1;
		while (K < M)
		{
			do
			{
				J = J + 2;
				I = 0;
				while (I < K && J % P[I] > 0)
					I++;
				JPRIME = I == K;
			} while (!JPRIME);
			P[K] = J;
			K = K + 1;
		}

		PAGENUMBER = 1;
		PAGEOFFSET = 0;
		while (PAGEOFFSET < M)
		{
			System.out.println("The First " + M + " Prime Numbers --- Page " + PAGENUMBER);
			System.out.println("");
			for (ROWOFFSET = PAGEOFFSET; ROWOFFSET < PAGEOFFSET + RR; ROWOFFSET++)
			{
				for (C = 0; C < CC; C++)
					if (ROWOFFSET + C * RR < M)
						System.out.format("%" + WW + "d", P[ROWOFFSET + C * RR]);
				System.out.println("");
			}
			System.out.println("\f");
			PAGENUMBER = PAGENUMBER + 1;
			PAGEOFFSET = PAGEOFFSET + RR * CC;
		}
	}
}
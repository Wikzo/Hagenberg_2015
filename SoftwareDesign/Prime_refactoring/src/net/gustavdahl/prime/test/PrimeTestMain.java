package net.gustavdahl.prime.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.gustavdahl.prime.PrintPrimes;

public class PrimeTestMain
{
	@Test
	public void testMain() throws Exception
	{
		String master = "C:/Users/Wikzo/Documents/Hagenberg_2015/SoftwareDesign/Prime_refactoring/master.log";
		String output = "C:/Users/Wikzo/Documents/Hagenberg_2015/SoftwareDesign/Prime_refactoring/sysout.log";
		File file = new File(output);
		PrintStream printStream;
		try
		{
			printStream = new PrintStream(new FileOutputStream(file));
			System.setOut(printStream);
			PrintPrimes.main(null);

			Assert.assertEquals("The files differ!", FileUtils.readFileToString(new File(master), "utf-8"),
					FileUtils.readFileToString(file, "utf-8"));

			// Assert.assertTrue(FileUtils.contentEquals(new File (master),
			// file));

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw e;

		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}
package com.l2.software;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite.SuiteClasses;

public class Main
{

	public static void main(String[] args)
	{
		// TestingTimers();

		Result result = JUnitCore.runClasses(MyTimerTest.class);
		for (Failure failure : result.getFailures())
		{
			System.out.println(failure.toString());
		}

	}
}

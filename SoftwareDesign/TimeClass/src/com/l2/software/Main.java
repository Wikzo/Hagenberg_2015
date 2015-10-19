package com.l2.software;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite.SuiteClasses;

public class Main
{

	// by Gustav Dahl (IM 2015)
	// (Originally pair-programmed together with Anne Juhler Hansen, but later the code projects deviated quite a bit)
	
	public static void main(String[] args)
	{
		Result result = JUnitCore.runClasses(MyTimerTest.class);
		for (Failure failure : result.getFailures())
		{
			System.out.println(failure.toString());
		}

	}
}

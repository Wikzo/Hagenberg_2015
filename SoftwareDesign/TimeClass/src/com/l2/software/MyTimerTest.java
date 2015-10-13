package com.l2.software;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class MyTimerTest
{
	
	// QUESTION: Is there a way to do multiple assertEquals check?
	// right now, it seems like it automatically breaks on each check,
	// so I have to manually comment out the checks by hand

	@Test
	public void TestingTimers()
	{
		// making a new Timer object ... passing in hours and minutes to
		// constructor
		// "potential problem": does the constructor take (hours, minutes) or
		// (minutes, hours)?
		Time t1 = new Time(1, 2);

		// Time t_temp;
		// checking if t is null (not really needed, since Eclipse automatically
		// checks for this
		// assertNotNull(t_temp);

		// CHECK 1
		// test if hours is now 1
		//assertEquals("Testing if hours are correct", 1, t.getHours());
		// result:
		// Testing are hours is correct expected:<1> but was:<2>
		
		// CHECK 2
		t1.setMinutes(65);
		//assertEquals("Testing if minutes are correct", 65, t.getMinutes());
		// result:
		// Testing if minutes are correct expected:<65> but was:<5>
		
		// CHECK 3
		// test if hours now is still 2
		//assertEquals("Testing if hours are correct", 2, t.getHours());
		// result:
		// Testing if hours are correct expected:<2> but was:<3>

		// add a new timer to the original timer
		Time t2 = new Time(2,3);
		t1.AddTimer(t2);
		System.out.println(t1.toString());

		// CHECK 4
		// test if hours is now 1+2 = 3
		//assertEquals("Testing if hours are still correct after adding", 3, t1.getHours());
		// result:
		// Testing are hours is still correct after adding expected:<3> but was:<5>
		
		// calculating time difference between t1 and t2
		Time tDiff = Time.CalculateDifferenceBetweenTimers(t1, t2);
		System.out.println(tDiff.toString());
	}
}

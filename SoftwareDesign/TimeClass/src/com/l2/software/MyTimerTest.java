package com.l2.software;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.sun.glass.ui.Timer;

public class MyTimerTest
{

	// QUESTION: Is there a way to do multiple assertEquals checks without breaking each time?
	// Right now I manually have to comment/outcomment each of the checks.
	// Can you automatically jump to the next error in Eclipse or something?

	@Test
	public void TestingTimers()
	{
		// NOTE: manually comment out each assertEquals test
		// indicated with an arrow like this <--
		
		Time t_temp;

		// TEST 0
		//assertNotNull(t_temp); // <-- (Eclipse won't allow me to do this ...)
		// Checking if t is null (not really needed, since Eclipse automatically checks for this anyway

		// TEST 1
		// Making a new Timer object ... passing in hours and minutes to the constructor
		Time t1 = new Time(1, 2);
		assertEquals("t1 minutes", 2, t1.getMinutes()); // <--
		// Assumption: the first parameter is hours, the second parameter is minutes
		// Results:
		// TestingTimers(com.l2.software.MyTimerTest): t1 minutes expected:<2> but was:<1>

		// TEST 2
		assertEquals("Testing if hours are correct", 1, t1.getHours()); // <--
		// Results:
		// Testing if hours are correct expected:<1> but was:<2>
		
		// TEST 3
		Time tModulusCheckApproach1 = new Time(550, 0, 1); // 1 = modulus approach 1
		assertEquals("Testing if minutes over 60 increases hours", 10, tModulusCheckApproach1.getHours()); // <--
		// Assumption: For each 60'th minute, an hour will be added. 120 minutes = 2 hours
		// Results:
		//Testing if minutes over 60 increases hours expected:<2> but was:<11>
		
		// TEST 4
		tModulusCheckApproach1 = new Time(550, 0, 2); // 2 = modulus approach 2
		assertEquals("Testing if minutes over 60 increases hours", 10, tModulusCheckApproach1.getHours()); // <--
		// Assumption: For each 60'th minute, an hour will be added. 120 minutes = 2 hours
		// Results:
		// Testing if minutes over 60 increases hours expected:<10> but was:<9>
		
		// TEST 5
		t1.setMinutes(-1);
		t1.setHours(-2);
		assertEquals("t1 minutes negative", -1, t1.getMinutes()); // <--		
		assertEquals("t1 hours negative", -2, t1.getHours()); // <--
		// Assumption: Setting negative minutes should decrease in hours
		// Results:
		// t1 minutes negative expected:<-1> but was:<1>
		// t1 hours negative expected:<-2> but was:<2>
		
		// TEST 6
		assertEquals("Testing the toString() method of t1", "2:1", t1.toString()); // <--
		// Assumption: toString() prints out the time separated by a dot (.) without further modifications
		// Results:
		// TestingTimers(com.l2.software.MyTimerTest): Testing the toString() method of t1 expected:<[2:]1> but was:<[02:0]1>
		
		// TEST 7
		t1.setMinutes(65);
		assertEquals("Testing if minutes are correct", 65, t1.getMinutes()); // <--
		// Assumption: minutes are 65, since this is what it was set to
		// Results:
		// Testing if minutes are correct expected:<65> but was:<5>

		// TEST 8
		assertEquals("Testing if hours are correct", 2, t1.getHours()); // <--
		// Assumption: t1's hours are still 2
		// Results:
		// Testing if hours are correct expected:<2> but was:<3>

		// TEST 9
		// add a new timer to the original timer
		Time t2 = new Time(2, 13);
		t1.AddTimer(t2);
		assertEquals("Adding timer t2 to t1, testing t1 hours:", 15, t1.getHours()); // <--
		// Results:
		// TestingTimers(com.l2.software.MyTimerTest): Adding timer t2 to t1, testing t1 hours: expected:<15> but was:<16>

		// TEST 10
		assertEquals("Testing hour-to-minutes calculation", 700, t2.GetTotalTimeInMinutes()); // <--
		// Results:
		// TestingTimers(com.l2.software.MyTimerTest): Testing hour-to-minutes calculation expected:<700> but was:<782>

		// TEST 11
		assertEquals("Calculating minute differences between t1 and t2", 250, Time.CalculateMinuteDifference(t1, t2)); // <--
		// Results:
		// TestingTimers(com.l2.software.MyTimerTest): Calculating minute differences between t1 and t2 expected:<250> but was:<185>

		// TEST 12
		assertEquals("t1 time since midnight", 900, t1.TimeInMinutesSinceMidnight()); // <--
		// Results:
		// TestingTimers(com.l2.software.MyTimerTest): t1 time since midnight expected:<900> but was:<967>

	}
}

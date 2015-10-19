package com.l2.software;

import java.io.Console;

import org.junit.Test;

import junit.framework.Assert;

public class Time
{
	private int minutes;
	private int hours;

	public Time(int minutes, int hours)
	{
		setMinutes(minutes);
		setHours(hours);
		ModulusSafeCheck2(); // this is the fool-proof version that can handle multiple additional hours
	}

	public Time(int minutes, int hours, int modulusCheck)
	{
		setMinutes(minutes);
		setHours(hours);

		switch (modulusCheck)
		{
			case 1:
				ModulusSafeCheck1();
				break;
			case 2:
				ModulusSafeCheck2();
				break;
			default:
				System.out.println("ERROR - please use '1' or '2' for modulus safe check!");
		}
	}

	public void ModulusSafeCheck1() // initial approach [not working with more than 199 minutes]
	{
		// I originally made this simple modulus check
		// Then I realized that it only works for 1 additional hour, not multiple hours
		// That's why I also created ModulusSafeCheck2()
		
		// add one hour for each 60 minutes
		if (this.minutes % 60 != this.minutes)
			this.hours++;

		this.minutes %= 60;
		this.hours %= 24;
	}

	public void ModulusSafeCheck2() // second approach [working]
	{
		if (this.minutes > 119)
			this.hours = RecursivelyCheckMinutes(this.minutes, this.hours);

		this.minutes %= 60;
		this.hours %= 24;
	}

	private int RecursivelyCheckMinutes(int minutesLeft, int hoursToAdd)
	{
		minutesLeft -= 60;
		hoursToAdd++;

		if (minutesLeft < 119) // only check if there are more than 2 hours (120 minutes)
		{
			hoursToAdd++; // extra hour (when less than 119 minutes)
			return hoursToAdd;
		}
		return RecursivelyCheckMinutes(minutesLeft, hoursToAdd);
	}

	public int getMinutes()
	{
		return minutes;
	}

	public void setMinutes(int minutes)
	{
		// You could probably do some fancy subtraction of the hours when negative minutes are provided,
		// but I decided just to use the absolute value and give an error warning instead.
		if (minutes < 0)
			System.out.println("WARNING! Cannot use negative minutes. Will use the absolute minute value instead");
		
		this.minutes = Math.abs(minutes);
	}

	public int getHours()
	{
		return hours;
	}

	public void setHours(int hours)
	{
		// Uses absolute value
		if (hours < 0)
			System.out.println("WARNING! Cannot use negative hours. Will use the absolute hour value instead");
		
		this.hours = Math.abs(hours);
	}

	public void PrintTime()
	{
		String s = "Hours: " + Integer.toString(hours) + ", Minutes: " + Integer.toString(minutes);
		System.out.println(s);
	}

	@Override
	public String toString()
	{
		String extraZeroMinutes = ""; // adds extra zero for 24-hour style (eg. 10:04)
		String extraZeroHours = ""; // adds extra zero for 24-hour style (eg. 02:14)

		if (this.minutes < 10)
			extraZeroMinutes = "0";

		if (this.hours < 10)
			extraZeroHours = "0";

		return String.format(extraZeroHours + "%d:" + extraZeroMinutes + "%d", this.hours, this.minutes);
	}

	public int GetTotalTimeInMinutes()
	{
		int minutes = this.getHours() * 60 + this.getMinutes();
		return minutes;
	}

	public void AddTimer(Time t)
	{
		this.hours += t.hours;
		this.minutes += t.minutes;
	}

	public static int CalculateMinuteDifference(Time t1, Time t2)
	{
		return Math.abs(t1.GetTotalTimeInMinutes() - t2.GetTotalTimeInMinutes());
	}

	public int TimeInMinutesSinceMidnight()
	{
		return this.GetTotalTimeInMinutes();
	}

}

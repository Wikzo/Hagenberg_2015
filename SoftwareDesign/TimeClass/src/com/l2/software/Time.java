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
	}

	private void ModulusSafeCheck()
	{
		// add one hour for each 60 minutes
		if (this.minutes % 60 != this.minutes)
			this.hours++;

		this.minutes %= 60;
		this.hours %= 24;
	}

	public int getMinutes()
	{
		return minutes;
	}

	public void setMinutes(int minutes)
	{
		this.minutes = minutes;

		ModulusSafeCheck();
	}

	public int getHours()
	{
		return hours;
	}

	public void setHours(int hours)
	{
		this.hours = hours;

		ModulusSafeCheck();
	}

	public void PrintTime()
	{
		String s = "Hours: " + Integer.toString(hours) + ", Minutes: " + Integer.toString(minutes);
		System.out.println(s);
	}

	@Override
	public String toString()
	{
		String extraZeroMinutes = ""; // add extra zero for 24-hour style (eg.
										// 10:04)
		String extraZeroHours = ""; // add extra zero for 24-hour style (eg.
									// 02:14)

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
		// not sure if there is a difference between converting time into total minutes
		// or calculating time in minutes since midnight?
		return this.GetTotalTimeInMinutes();
	}

}

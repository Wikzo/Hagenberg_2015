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
	
	public void GetTime()
	{
		String s = "Hours: " + Integer.toString(hours) + ", Minutes: " + Integer.toString(minutes);
		System.out.println(s);
	}
	
  @Override public String toString()
  {
	  String extraZero = "";
	  
	  if (this.minutes < 10)
		  extraZero = "0";
	  
	  return String.format(extraZero + "%d:%d", this.hours, this.minutes);
  }

	public void AddTimer(Time t)
	{
		this.hours += t.hours;
		this.minutes += t.minutes;
	}
	
	public static Time CalculateDifferenceBetweenTimers(Time t1, Time t2)
	{
		int minutesDiff = Math.abs(t1.minutes - t2.minutes);
		int hoursDiff = Math.abs(t1.minutes - t2.minutes);

		Time difference = new Time(minutesDiff, hoursDiff);
		
		return difference;
	}

}

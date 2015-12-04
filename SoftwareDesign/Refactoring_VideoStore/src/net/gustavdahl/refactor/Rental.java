package net.gustavdahl.refactor;

public class Rental
{
	private Video video;
	private int days;

	public Rental(Video video, int days)
	{
		this.video = video;
		this.days = days;
	}

	public int getDays()
	{
		return days;
	}

	public Video getVideo()
	{
		return video;
	}
}

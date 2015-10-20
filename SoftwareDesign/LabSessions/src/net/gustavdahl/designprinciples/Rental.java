package net.gustavdahl.designprinciples;

public class Rental
{
	private Video _video;
	private int _days;

	public Rental(Video video, int days)
	{
		this._video = video;
		this._days = days;
	}

	public Video GetVideo()
	{
		return this._video;
	}

	
	public int GetDays()
	{
		return this._days;
	}
	
	public int GetChargeInCents()
	{
		return _video.GetChargeInCents(_days);
	}
	
	public int GetBonus()
	{
		return _video.GetBonus(_days);
	}

}

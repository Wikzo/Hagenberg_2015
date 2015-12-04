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

	int computeChargeInCents()
	{
		// determine amounts for each line
		int result = 0;
		int days = getDays();
		switch (getVideo().getKind())
		{
		case Video.REGULAR:
			result += 200;
			if (days > 2)
				result += (days - 2) * 150;
			break;
		case Video.NEW_RELEASE:
			result += days * 300;
			break;
		case Video.CHILDRENS:
			result += 150;
			if (days > 3)
				result += (days - 3) * 150;
			break;
		}
		return result;
	}

	int addBonus()
	{
		int bonus = 1;

		// add bonus for a two day new release rental
		if (getVideo().getKind() == Video.NEW_RELEASE && getDays() > 1)
			bonus++;
		return bonus;
	}
}

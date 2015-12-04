package net.gustavdahl.refactor;

public class Video
{
	public static final int REGULAR = 0;
	public static final int NEW_RELEASE = 1;
	public static final int CHILDRENS = 2;

	private String title;
	private int kind;

	public Video(String title, int kind)
	{
		this.title = title;
		this.kind = kind;
	}

	public int getKind()
	{
		return kind;
	}

	public void setKind(int kind)
	{
		this.kind = kind;
	}

	public String getTitle()
	{
		return title;
	}
}

package net.gustavdahl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Vector;

import javax.xml.crypto.Data;

import org.w3c.dom.css.Rect;

public class DrawingPanel extends Panel
{

	Random random = new Random(System.currentTimeMillis());

	private int x = -1;
	private int y = -1;
	private int radius = -1;
	private Polygon house;

	private GeoTransformationMatrix matrix = null;
	
	private Vector<Polygon>data = new Vector<Polygon>(); 

	@Override
	public void paint(Graphics graphics)
	{
		if (house != null)
		{
			graphics.setColor(Color.green);

			for (Polygon p : data)
			{
				Polygon temp = matrix.multiply(p);
				graphics.drawPolygon(temp);
			}
			

		}

		else if (radius >= 0)
		{
			graphics.setColor(Color.red);
			graphics.drawOval(x, y, radius, radius);
		}
	}

	public void DrawRandomCircle()
	{
		house = null;

		radius = random.nextInt(200);

		x = random.nextInt(getWidth());
		y = random.nextInt(getHeight());
		radius = random.nextInt(getWidth());

		repaint();
		
		

	}

	public void DrawHouse()
	{
		radius = -1;

		int deltaX = random.nextInt(getWidth());
		int deltaY = random.nextInt(getHeight());

		int f = 10000; // scaling factor

		house = new Polygon();
		house.addPoint(f * (10 + deltaX), f * (10 + deltaY));
		house.addPoint(f * (40 + deltaX), f * (10 + deltaY));
		house.addPoint(f * (40 + deltaX), f * (30 + deltaY));
		house.addPoint(f * (25 + deltaX), f * (50 + deltaY));
		house.addPoint(f * (10 + deltaX), f * (30 + deltaY));

		data.add(house);

	}

	public void zoomToFit()
	{
		Rectangle win = new Rectangle(0, 0, getWidth(), getHeight());
		Rectangle map = getBoundingBox(data);

		matrix = GeoTransformationMatrix.zoomToFit(map, win);

		repaint();
	}

	private Rectangle getBoundingBox(Vector<Polygon> data )
	{
		Rectangle r = null;
		
		if (data != null && data.size() > 0)
		{
			r = data.elementAt(0).getBounds();
			
			for (int i = 1; i < data.size(); i++)
			{
				r = r.union(data.elementAt(1).getBounds()); // combine
			}
		}
		
		return r;
	}
	
	public void Zoom(double d)
	{
		Point center = new Point(getWidth() / 2, getHeight() / 2);
		
		matrix = GeoTransformationMatrix.zoomToPoint(matrix, center, d);
		
		repaint(); // TODO: move to command control instead
	}
	
	public void Translate(int i)
	{
		matrix = GeoTransformationMatrix.translate(0, i).multiply(matrix);
		
		repaint(); // TODO: move to command control instead
	}
}

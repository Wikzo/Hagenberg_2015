package net.gustavdahl;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DrawingTest implements WindowListener, ActionListener
{

	private DrawingPanel drawingPanel;

	public DrawingTest()
	{
		Frame frame = new Frame();

		frame.setSize(640, 480);
		frame.setLayout(new BorderLayout());

		Panel buttonBar = new Panel();

		// circle (zoom to fit)
		Button draw = new Button("ZoomToFit");
		draw.addActionListener(this);

		// house
		Button house = new Button("house");
		house.addActionListener(this);

		// zoom in
		Button zoomIn = new Button("+");
		zoomIn.addActionListener(this);
		buttonBar.add(zoomIn);

		// zoom out
		Button zoomOut = new Button("-");
		zoomOut.addActionListener(this);
		buttonBar.add(zoomOut);

		// zoom north
		Button zoomNorth = new Button("N");
		zoomNorth.addActionListener(this);
		buttonBar.add(zoomNorth);

		buttonBar.add(draw);
		buttonBar.add(house);

		frame.add(buttonBar, BorderLayout.SOUTH);

		drawingPanel = new DrawingPanel();
		frame.add(drawingPanel, BorderLayout.CENTER);

		frame.addWindowListener(this); // makes it able to close
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		DrawingTest t = new DrawingTest();
	}

	@Override
	public void windowActivated(WindowEvent e)
	{

	}

	@Override
	public void windowClosed(WindowEvent e)
	{

	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		System.out.println("Closing");
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{

	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{

	}

	@Override
	public void windowIconified(WindowEvent e)
	{

	}

	@Override
	public void windowOpened(WindowEvent e)
	{

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();

		switch (actionCommand)
		{
		case "ZoomToFit":
		{
			// System.out.println("Draw circle");
			// drawingPanel.DrawRandomCircle();
			// drawingPanel.DrawRandomCircle();

			drawingPanel.zoomToFit();

		}
			break;

		case "house":
		{
			System.out.println("Draw house");
			drawingPanel.DrawHouse();
		}
			break;

		case "+":
		{
			drawingPanel.Zoom(1.3);
		}
			break;
		case "-":
		{
			drawingPanel.Zoom(1/1.3);
		}
			break;
		case "N":
		{
			drawingPanel.Translate(20);
		}
			break;
		default:
			System.err.println("Unknown action command");
		}
	}

}

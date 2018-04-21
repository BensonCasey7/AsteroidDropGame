package Games;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class PowerUp
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int windowWidth = (int) screenSize.getWidth();
	int windowHeight = (int) screenSize.getHeight();
	private int xCord, yCord, size = (int)(windowWidth*.03);
	Ellipse2D.Double body = new Ellipse2D.Double();
	
	public PowerUp(int x, int y)
	{
		body = new Ellipse2D.Double(x, y, size, size);
	}
	
	public void setX(int x)
	{
		body.x = x;
	}
	
	public void setY(int y)
	{
		body.y = y;
	}
	
	public int getX()
	{
		return (int) body.x;
	}
	
	public int getY()
	{
		return (int) body.y;
	}
	
	public int getSize()
	{
		return size;
	}

	public Ellipse2D.Double getBody()
	{
		return body;
	}
}
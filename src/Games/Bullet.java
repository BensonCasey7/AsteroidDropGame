package Games;

import java.awt.geom.Rectangle2D;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;

public class Bullet
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int windowWidth = (int) screenSize.getWidth();
	int windowHeight = (int) screenSize.getHeight();
	private int xCord, yCord, width = 3, height = 10, speed = (int) (windowHeight*.04), stray = (int) (windowWidth*.01);
	Rectangle2D.Double body = new Rectangle2D.Double();
	
	public Bullet(int x, int y)
	{
		body = new Rectangle2D.Double(x, y, width, height);
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
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public int getStray()
	{
		return stray;
	}
	
	public Rectangle2D.Double getBody()
	{
		return body;
	}
}

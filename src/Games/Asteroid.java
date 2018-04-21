package Games;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Asteroid
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int windowWidth = (int) screenSize.getWidth();
	int windowHeight = (int) screenSize.getHeight();
	private int size = (int)(windowWidth*.06);
	private double speed;
	Ellipse2D.Double body = new Ellipse2D.Double();
	Random random = new Random();
	
	public Asteroid(int x, int y, double s)
	{
		size+=(random.nextInt((int) (windowWidth*.1)));
		speed = s;
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
	
	public int getAstSize()
	{
		return size;
	}

	public double getSpeed()
	{
		return speed;
	}

	public Ellipse2D.Double getBody()
	{
		return body;
	}
}

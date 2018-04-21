package Games;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class AsteroidDropGame extends GameEngine {

	static final int UP = 0;
	static final int RIGHT = 1;
	static final int DOWN = 2;
	static final int LEFT = 3;
	int size = 20;
	int dir = -1;
	int x = windowWidth/2;
	int y = windowHeight-30;
	int playerSpeed = 10;
	int score = 0;
	int lives = 3;
	int reload = 0;
	int astFreq = 60, astCountdown = 15;
	double astSpeed = 5;
	double pUpFreq = 1200, pUpCountdown = 150, pUpChance = 300, pUpAmmo = 0, pUpMaxAmmo = 50;
	double difFreq = .8, difSpeed = 1.1, difPowUpFreq = .8;
	int quitTimer = 0;
	
	Random random = new Random();
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Bullet> pUpBulletsLeft = new ArrayList<Bullet>();
	ArrayList<Bullet> pUpBulletsRight = new ArrayList<Bullet>();
	ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

	public static void main(String[] args)
	{
		AsteroidDropGame g = new AsteroidDropGame();
		g.run();
		System.exit(0);
	}

	void update() {
		if (quitTimer == 150)
			isRunning = false;
		
		if (lives <=0)
		{
			quitTimer++;
			return;
		}
		
		if (input.isKeyDown(KeyEvent.VK_RIGHT))
		{
			x += playerSpeed;
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT))
		{
			x -= playerSpeed;
		}
		
		if (input.isKeyDown(KeyEvent.VK_ESCAPE))
		{
			isRunning = false;
		}
		

		if (input.isKeyDown(KeyEvent.VK_X))
		{
			lives = 0;
		}
		
		if (x<7) //Prevent ship from leaving the screen
			x = 7;
		if (x>windowWidth-8)
			x = windowWidth-8;
		
		
		if (input.isKeyDown(KeyEvent.VK_SPACE))
		{
			if (reload == 0)
			{
				bullets.add(new Bullet(x-1,y-5));
				reload = 10;
				if (pUpAmmo != 0)
				{
					pUpBulletsLeft.add(new Bullet(x-3,y-5));
					pUpBulletsRight.add(new Bullet(x+2,y-5));
					reload = 3;
					pUpAmmo--;
				}
			}
		}
		
		if (reload > 0)
			reload--;
		
		for (Bullet b : bullets) //Moves bullets
		{
			b.setY(b.getY()-b.getSpeed());
		}
		
		for (Bullet bL : pUpBulletsLeft)
		{
			bL.setY(bL.getY()-bL.getSpeed());
			bL.setX(bL.getX()-bL.getStray());
		}
		
		for (Bullet bR : pUpBulletsRight)
		{
			bR.setY(bR.getY()-bR.getSpeed());
			bR.setX(bR.getX()+bR.getStray());
		}
		
		for (Asteroid a : asteroids) //Moves asteroids
		{
			a.setY(a.getY()+(int)a.getSpeed());
		}
		
		if (pUpCountdown == 0)
		{
			if (random.nextInt((int) pUpChance) == 1)
			{
				powerUps.add(new PowerUp(random.nextInt(windowWidth-25), random.nextInt(windowHeight-300)));
				pUpCountdown = pUpFreq;
			}
		}
		else
			pUpCountdown--;
		
		/*if (pUpAmmo != 0)
			pUpAmmo--;*/
		
		if (astCountdown-- == 0) //Creates new asteroid
		{
			asteroids.add(new Asteroid(random.nextInt(windowWidth-45), 0, astSpeed));
			astCountdown = astFreq;
		}
		
		for (Bullet b : new ArrayList<Bullet>(bullets)) //Collision for bullets and asteroids
		{
			for (Asteroid a : new ArrayList<Asteroid>(asteroids))
			{
				if (a.getBody().intersects(b.getBody()))
				{
					bullets.remove(b);
					asteroids.remove(a);
					score++;
					if (score%10 == 0) //Increases difficulty every 10 points
					{
						astFreq *= difFreq;
						astSpeed *= difSpeed;
						pUpFreq *= difPowUpFreq;
					}
				}
			}
		}
		
		for (Bullet bL : new ArrayList<Bullet>(pUpBulletsLeft)) //Collision for power up bullets
		{
			for (Asteroid a : new ArrayList<Asteroid>(asteroids))
			{
				if (a.getBody().intersects(bL.getBody()))
				{
					bullets.remove(bL);
					asteroids.remove(a);
					score++;
					if (score%10 == 0)
					{
						astFreq *= difFreq;
						astSpeed *= difSpeed;
						pUpFreq *= difPowUpFreq;
						lives++;
					}
				}
			}
		}
		
		for (Bullet bR : new ArrayList<Bullet>(pUpBulletsRight)) //Collision for power up bullets
		{
			for (Asteroid a : new ArrayList<Asteroid>(asteroids))
			{
				if (a.getBody().intersects(bR.getBody()))
				{
					bullets.remove(bR);
					asteroids.remove(a);
					score++;
					if (score%10 == 0)
					{
						astFreq *= difFreq;
						astSpeed *= difSpeed;
						pUpFreq *= difPowUpFreq;
						lives++;
					}
				}
			}
		}
		
		for (Bullet b : new ArrayList<Bullet>(bullets)) //Collision for bullets and Power ups
		{
			for (PowerUp p : new ArrayList<PowerUp>(powerUps))
			{
				if (p.getBody().intersects(b.getBody()))
				{
					bullets.remove(b);
					powerUps.remove(p);
					pUpAmmo = pUpMaxAmmo;
					lives++;
				}
			}
		}
		
		for (Asteroid a : new ArrayList<Asteroid>(asteroids))
		{
			if (a.getY() >= windowHeight)
			{
				asteroids.remove(a);
				lives--;
			}
		}
	}

	void draw(Graphics g) {
		g = (Graphics2D) g;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		g.setColor(Color.WHITE);
		int[] xPts = {x, x+7, x , x-7};
		int[] yPts = {y-15, y+5, y , y+5};
		g.drawPolygon(xPts, yPts, xPts.length);
		
		
		g.setColor(Color.CYAN);
		for (Bullet b : bullets){
			g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
		for (Bullet bL : pUpBulletsLeft){
			g.fillRect(bL.getX(), bL.getY(), bL.getWidth(), bL.getHeight());
		}
		for (Bullet bR : pUpBulletsRight){
			g.fillRect(bR.getX(), bR.getY(), bR.getWidth(), bR.getHeight());
		}
		
		g.setColor(Color.WHITE);
		for (Asteroid a : asteroids){
			g.fillOval(a.getX(), a.getY(), a.getAstSize(), a.getAstSize());
		}
		
		g.setColor(Color.RED);
		for (PowerUp p : powerUps){
			g.fillOval(p.getX(), p.getY(), p.getSize(), p.getSize());
		}
		g.fillRect(10, 45, (int) (pUpAmmo/pUpMaxAmmo*80), 15);
		
		
		g.setColor(Color.WHITE);
		g.drawString("Score:\t" + score, 10, 15);
		g.drawString("Lives:\t" + lives, 10, 30);
		
		if (lives <= 0)
		{
			g.drawString("Game Over", windowWidth/2-32, windowHeight/2);
		}
	}


}
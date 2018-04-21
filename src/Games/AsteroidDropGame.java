package Games;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
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
	static int x = windowWidth/2;
	static int y = windowHeight-30;
	static int playerSpeed = (int) (windowWidth*.05);
	int score = 0;
	int lives = 3;
	double fireRate = 0, maxAmmo = 20, ammo = maxAmmo, reloadSpeed = 45, reloadTimer = reloadSpeed;
	int boxWidth = 150, boxHeight = 25;
	boolean canShoot = true;
	int astFreq = 60, astCountdown = 15;
	double astSpeed = 5;
	double pUpFreq = 600, pUpCountdown = 150, pUpChance = 300, pUpTimer = 0, pUpMaxTime = 300;
	int exLifeFreq = 120, exLifeCountdown = 150, exLifeChance = 300;
	double difAstFreq = .9, difAstSpeed = 1.1, difPowUpFreq = .7;
	int quitTimer = 0;
	
	Random random = new Random();
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Bullet> pUpBulletsLeft = new ArrayList<Bullet>();
	ArrayList<Bullet> pUpBulletsRight = new ArrayList<Bullet>();
	ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	ArrayList<ExtraLife> extraLives = new ArrayList<ExtraLife>();

	public static void main(String[] args)
	{
		AsteroidDropGame g = new AsteroidDropGame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		windowWidth = (int) screenSize.getWidth();
		windowHeight = (int) screenSize.getHeight();
		g.setExtendedState(MAXIMIZED_BOTH);
		g.setUndecorated(true);
		g.setVisible(true);
		y = (int) (windowHeight*.9);
		x = (int) windowWidth/2;
		playerSpeed = (int) (windowWidth*.025);
		//g.init();
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
		
		if (input.isKeyDown(KeyEvent.VK_SPACE))
		{
			if (canShoot && fireRate == 0)
			{
				bullets.add(new Bullet(x-1,y-5));
				fireRate = 5;
				if (pUpTimer != 0)
				{
					pUpBulletsLeft.add(new Bullet(x-3,y-5));
					pUpBulletsRight.add(new Bullet(x+2,y-5));
					fireRate = 2;
					ammo++;
				}
				ammo--;
			}
		}
		
		if (fireRate > 0)
			fireRate--;
		
		if (ammo == 0)
		{
			canShoot = false;
			reloadTimer--;
			if (reloadTimer == 0)
			{
				ammo = maxAmmo;
				reloadTimer = reloadSpeed;
				canShoot = true;
			}
		}
		
		if (x<7) //Prevent ship from leaving the screen
			x = 7;
		if (x>windowWidth-8)
			x = windowWidth-8;
		
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
		
		if (pUpCountdown == 0) //Creates new power up
		{
			if (random.nextInt((int) pUpChance) == 1)
			{
				powerUps.add(new PowerUp(random.nextInt((int) (windowWidth*.97)), random.nextInt(windowHeight-300)));
				pUpCountdown = pUpFreq;
			}
		}
		else
			pUpCountdown--;
		
		if (pUpTimer != 0)
			pUpTimer--;
		
		if (random.nextInt((int) exLifeChance) == 1 && exLifeCountdown < 0) //Creates new extra life
		{
			extraLives.add(new ExtraLife(random.nextInt((int) (windowWidth*.95)), random.nextInt(windowHeight-300)));
			exLifeCountdown = exLifeFreq;
		}
		exLifeCountdown--;
		
		if (astCountdown-- == 0) //Creates new asteroid
		{
			asteroids.add(new Asteroid(random.nextInt(windowWidth-(int)(windowWidth*.06+windowWidth*.01)), -(int)(windowWidth*.06+windowWidth*.01), astSpeed));
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
						astFreq *= difAstFreq;
						astSpeed *= difAstSpeed;
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
						astFreq *= difAstFreq;
						astSpeed *= difAstSpeed;
						pUpFreq *= difPowUpFreq;
					}
				}
			}
			
			for (ExtraLife e : new ArrayList<ExtraLife>(extraLives))
			{
				if (e.getBody().intersects(bL.getBody()))
				{
					bullets.remove(bL);
					extraLives.remove(e);
					lives+=1;
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
						astFreq *= difAstFreq;
						astSpeed *= difAstSpeed;
						pUpFreq *= difPowUpFreq;
					}
				}
			}
			
			for (ExtraLife e : new ArrayList<ExtraLife>(extraLives))
			{
				if (e.getBody().intersects(bR.getBody()))
				{
					bullets.remove(bR);
					extraLives.remove(e);
					lives+=1;
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
					pUpTimer = pUpMaxTime;
				}
			}
		}
		
		for (Bullet b : new ArrayList<Bullet>(bullets)) //Collision for bullets and extra lives
		{
			for (ExtraLife e : new ArrayList<ExtraLife>(extraLives))
			{
				if (e.getBody().intersects(b.getBody()))
				{
					bullets.remove(b);
					extraLives.remove(e);
					lives+=1;
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
		g.setFont(new Font("Verdana", Font.PLAIN, 40)); 
		
		g.setColor(Color.WHITE);
		int[] xPts = {x, x+7, x , x-7};
		int[] yPts = {y-15, y+5, y , y+5};
		g.drawPolygon(xPts, yPts, xPts.length);
		
		g.setColor(Color.GRAY);
		g.fillRect(10, 110, boxWidth, boxHeight);
		
		g.setColor(Color.CYAN);
		for (Bullet b : bullets)
		{
			g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
		for (Bullet bL : pUpBulletsLeft)
		{
			g.fillRect(bL.getX(), bL.getY(), bL.getWidth(), bL.getHeight());
		}
		for (Bullet bR : pUpBulletsRight)
		{
			g.fillRect(bR.getX(), bR.getY(), bR.getWidth(), bR.getHeight());
		}
		if (pUpTimer == 0)
			g.fillRect(10, 110, (int) (ammo/maxAmmo*boxWidth), boxHeight);
		if (canShoot == false)
			g.fillRect(10, 110, (int) (boxWidth - (reloadTimer/reloadSpeed)*boxWidth), boxHeight);
		
		g.setColor(Color.WHITE);
		for (Asteroid a : asteroids)
		{
			g.drawOval(a.getX(), a.getY(), a.getAstSize(), a.getAstSize());
		}
		
		g.setColor(Color.RED);
		for (PowerUp p : powerUps)
		{
			g.fillOval(p.getX(), p.getY(), p.getSize(), p.getSize());
		}
		g.fillRect(10, 110, (int) (pUpTimer/pUpMaxTime*boxWidth), boxHeight);
		
		g.setColor(Color.GREEN);
		for (ExtraLife e : extraLives)
		{
			g.fillOval(e.getX(), e.getY(), e.getSize(), e.getSize());
		}
		if (lives < 10)
		for (int i = 0; i < lives; i++)
		{
			g.fillOval(10+i*35, 65, 25, 25);
		}
		else
		{
			g.fillOval(10, 65, 25, 25);
			g.setColor(Color.WHITE);
			g.drawString("x" + lives, 45, 90);
		}
		
		g.setColor(Color.WHITE);
		g.drawString("Score:\t" + score, 10, 45);
	
		if (lives <= 0)
		{
			g.drawString("Game Over", windowWidth/2-100, windowHeight/2);
		}
	}
}
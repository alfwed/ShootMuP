package shootemup.enemy;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import shootemup.Board;
import shootemup.Entity;
import shootemup.bullet.Bullet;
import shootemup.sprite.LinearAnimatedSprite;

public abstract class Enemy extends Entity {

	final public static double speed = 0.2 * Board.DELAY;
	
	protected double hp;
	protected boolean alive = true;
	protected boolean visible = true;
	
	protected LinearAnimatedSprite currentSprite;
	protected LinearAnimatedSprite las;
	protected LinearAnimatedSprite spriteHit;
	protected LinearAnimatedSprite spriteExplosion;

	protected Timer timerHit = new Timer();
	protected Timer timerExplosion = new Timer();
	
	protected int collisionDamage;

	protected int scorePoints = 0;
	
	public Enemy(int x, int y) {
		super(x, y);
	}
	
	public void draw(Graphics g) {
		currentSprite.draw(g);
	}

	public void hit(double damage) {
		if (hp - damage > 0) {
			hp -= damage;
			changeSprite(spriteHit);
			timerHit.schedule(new TimerTask() {
				public void run() {
					if (alive)
						changeSprite(las);
				}
			}, 500);
		} else {
			hp = 0;
			explode();
		}
	}

	abstract public void fire(ArrayList<Bullet> bullets, double playerX, double playerY);

	public boolean alive() {
		return alive;
	}
	
	public boolean visible() {
		return visible;
	}
	
	protected void explode() {
		alive = false;
		collisionDamage = 0;
		changeSprite(spriteExplosion);
		
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
					this.getClass().getResourceAsStream("/explosion2.wav"));
			clip.open(inputStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-15f);
			clip.start(); 
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        }
		
		timerExplosion.schedule(new TimerTask() {
			public void run() {
				visible = false;
			}
		}, 1000);
	}
	
	public void setX(double x) {
		super.setX(x);

		if (null != currentSprite)
			currentSprite.setX(x-width/2);
	}

	public void setY(double y) {
		super.setY(y);

		if (null != currentSprite)
			currentSprite.setY(y-height/2);
	}

	public Rectangle getHitBox() {
		return new Rectangle((int)(getX()-(width)/2), (int)(getY()-(height)/2), (int)(width), (int)(height*0.8));
	}
	
	public void changeSprite(LinearAnimatedSprite s) {
		currentSprite = s;
		width = currentSprite.getWidth();
		height = currentSprite.getHeight();
		setX(getX());
		setY(getY());
	}

	public int getCollisionDamage() {
		return collisionDamage;
	}
	
	public double getHp() {
		return hp;
	}

	public int getScorePoints() {
		// TODO Auto-generated method stub
		return scorePoints ;
	}
}

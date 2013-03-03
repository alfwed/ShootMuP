package shootemup;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import shootemup.bullet.Bullet;
import shootemup.bullet.BulletPlayer;
import shootemup.bullet.BulletPlayer2;
import shootemup.factory.ImageFactory;
import shootemup.sprite.LinearAnimatedSprite;
import shootemup.util.ImageUtil;

public class Player extends Entity {

	private double speed = 0.7 * Board.DELAY;
	private double speedLeft, speedRight, speedUp, speedDown;

	private double fireInterval = 40 / Board.DELAY;
	private boolean firing = false;
	private int fireProgress = 0;

	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private double health, hp;
	private boolean alive = true;
	private boolean visible = true;

	private LinearAnimatedSprite currentSprite;
	private LinearAnimatedSprite las;
	private LinearAnimatedSprite spriteHit;
	private LinearAnimatedSprite spriteExplosion;

	private Timer timerHit = new Timer();
	private Timer timerExplosion = new Timer();

	private Clip fireClip;
	
	public Player(int x, int y) {
		super(x, y);

		ImageFactory fact = ImageFactory.getInstance();
		BufferedImage[] images = new BufferedImage[3];
		BufferedImage[] imagesHit = new BufferedImage[3];
		BufferedImage[] imageExplosion = null;
		
		try {
			BufferedImage player = fact.getImage("/player_sprite.png");
			images = ImageUtil.splitImage(player, 3, 1);

			BufferedImage playerHit = fact.getImage("/player_sprite_hit.png");
			imagesHit = ImageUtil.splitImage(playerHit, 3, 1);
			
			BufferedImage explosion = fact.getImage("/BlueExplosion.png");
			imageExplosion = ImageUtil.splitImage(explosion, 3, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}

		las = new LinearAnimatedSprite(images);
		las.addNewAnimation("left", 0);
		las.addNewAnimation("right", 2);
		las.addNewAnimation("center", 1);

		spriteHit = new LinearAnimatedSprite(imagesHit);
		spriteHit.addNewAnimation("left", 0);
		spriteHit.addNewAnimation("right", 2);
		spriteHit.addNewAnimation("center", 1);

		spriteExplosion = new LinearAnimatedSprite(imageExplosion);
		spriteExplosion.addNewAnimation("center", 11, 0, 70/Board.DELAY);
		
		currentSprite = las;

		width = images[0].getWidth(null);
		height = images[0].getHeight(null);

		setX(getX());
		setY(getY());

		health = hp = 100;
		
		initFireClip();
	}

	public void draw(Graphics g) {
		if (getSpeedx() > 0) {
			currentSprite.setAnimation("right");
		} else if (getSpeedx() < 0) {
			currentSprite.setAnimation("left");
		} else {
			currentSprite.setAnimation("center");
		}

		currentSprite.draw(g);
	}

	public void move(int boardWidth, int boardHeight) {
		if (!alive) return;
		
		setSpeedx(speedRight - speedLeft);
		setSpeedy(speedDown - speedUp);
		super.move(boardWidth, boardHeight);

		keepOnScreen(boardWidth, boardHeight);
		fireIfReady();
	}

	private void keepOnScreen(int boardWidth, int boardHeight) {
		if (getX() < width/2)
			setX(width/2);
		else if (getX() > (boardWidth - width/2))
			setX(boardWidth - width/2);

		if (getY() < height/2)
			setY(height/2);
		else if (getY() > (boardHeight - height/2))
			setY(boardHeight - height/2);
	}

	private void fireIfReady() {
		if (firing) {
			fireProgress++;

			if (fireProgress == fireInterval) {
				fire();
				fireProgress = 0;
			}
		}
	}

	public void fire() {
		if (!alive) return;
		
		Bullet bullet1 = new BulletPlayer((int) getX() - 7,
				(int) getY() - 10);
		Bullet bullet2 = new BulletPlayer((int) getX() + 7,
				(int) getY() - 10);
		bullets.add(bullet1);
		bullets.add(bullet2);
		
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
					this.getClass().getResourceAsStream("/Laser_Shoot25.wav"));
			clip.open(inputStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-10f);
			clip.start();
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        }
//		
		Bullet bullet3 = new BulletPlayer2((int)getX() - width/2, (int)getY()+10);
		Bullet bullet4 = new BulletPlayer2((int)getX() + width/2, (int)getY()+10); 
		bullets.add(bullet3); 
		bullets.add(bullet4);
	}

	public void setFiring(boolean b) {
		firing = b;

		if (!b)
			fireProgress = 0;
	}
	
	private void initFireClip() {
		try {
			fireClip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
					this.getClass().getResourceAsStream("/Laser_Shoot7.wav"));
			fireClip.open(inputStream);
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        }
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Bullet> getBullets() {
		return (ArrayList<Bullet>) bullets.clone();
	}

	public void removeBullet(Bullet b) {
		bullets.remove(b);
	}

	public void hit(double damage) {
		if (hp - damage > 0) {
			hp -= damage;
			
			currentSprite = spriteHit;
			timerHit.schedule(new TimerTask() {
				public void run() {
					if (alive)
						currentSprite = las;
				}
			}, 500);
		} else {
			hp = 0;
			explode();
		}
	}

	private void explode() {
		alive = false;
		changeSprite(spriteExplosion);
		timerExplosion.schedule(new TimerTask() {
			public void run() {
				visible = false;
			}
		}, 2000);
	}
	
	public void changeSprite(LinearAnimatedSprite s) {
		currentSprite = s;
		width = currentSprite.getWidth();
		height = currentSprite.getHeight();
		setX(getX());
		setY(getY());
	}

	public boolean alive() {
		return alive;
	}

	public double getHp() {
		return hp;
	}

	public void resetHp() {
		hp = health;
	}
	
	public double getHealth() {
		return health;
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
		return new Rectangle((int)(getX()-(width*0.9)/2), (int)(getY()-(height*0.9)/2), (int)(width*0.8), (int)(height*0.8));
	}
	
	public void reset() {
		speedLeft = speedRight = speedUp = speedDown = 0;
		firing = false;
		fireProgress = 0;
		resetHp();
		bullets = new ArrayList<Bullet>();
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			speedLeft = speed;
		} else if (key == KeyEvent.VK_RIGHT) {
			speedRight = speed;
		} else if (key == KeyEvent.VK_UP) {
			speedUp = speed;
		} else if (key == KeyEvent.VK_DOWN) {
			speedDown = speed;
		} else if (key == KeyEvent.VK_SPACE) {
			setFiring(true);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			speedLeft = 0;
		} else if (key == KeyEvent.VK_RIGHT) {
			speedRight = 0;
		} else if (key == KeyEvent.VK_UP) {
			speedUp = 0;
		} else if (key == KeyEvent.VK_DOWN) {
			speedDown = 0;
		} else if (key == KeyEvent.VK_SPACE) {
			setFiring(false);
		}
	}

	public boolean visible() {
		return visible;
	}

}

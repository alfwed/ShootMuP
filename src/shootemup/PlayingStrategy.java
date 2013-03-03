package shootemup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import shootemup.bullet.Bullet;
import shootemup.enemy.Enemy;
import shootemup.factory.ImageFactory;
import shootemup.util.ImageUtil;

public class PlayingStrategy implements AppStrategy {

	private Dimension screen;
	private Board board;
	
	private Background background;

	private Player player;
	private UnitFrame unitFrame;
	private ArrayList<Bullet> enemyBullets;
	
	private int currentLevel = 1;
	private Level level;
	
	private boolean paused = true;
	
	private Clip backgroundClip;
	
	public PlayingStrategy(Dimension screen, Board board) {
		this.screen = screen;
		this.board = board;
	}
	
	private void init() {
		initBackgroundClip();
		initImages();
		initBackground();
		initLevel();
		
		player = new Player((int)Math.round(getWidth()/2), getHeight()-100);
		unitFrame = new UnitFrame(getWidth(), getHeight());
		enemyBullets = new ArrayList<Bullet>();
	}
	
	public void nextLevel() {
		currentLevel++;
		initBackground();
		initLevel();
		player.reset();
		enemyBullets = new ArrayList<Bullet>();
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public void restart() {
		currentLevel = 1;
		init();
	}
	
	public void pause() {
		paused = true;
		stopBackgroundClip();
	}
	
	public void resume() {
		paused = false;
		startBackgroundClip();
	}
	
	public void paint(Graphics g) {
		drawBackground(g);
		unitFrame.draw(g, player);
		drawPlayer(g);
		drawEnemies(g);
		drawPlayerBullets(g);
		drawEnemyBullets(g);
		drawScore(g);
	}

	private void drawScore(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(String.valueOf(level.getScore()), 10, 20);
	}

	public void update() {
		if (!paused) {
			if (level.complete()) {
				pause();
				board.levelComplete();
			}
			
			checkForHits();
			movePlayer();
			moveEnemies();
			moveBullets();
			
			level.update();
			
			if (!player.alive() && !player.visible()) 
				gameOver();
		}
	}

	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}

	public void keyPressed(KeyEvent e) {
		player.keyPressed(e);
		
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_P) {
			if (paused)
				resume();
			else 
				pause();
		}
	}
	
	private int getWidth() {
		return (int)screen.getWidth();
	}
	
	private int getHeight() {
		return (int)screen.getHeight();
	}
	
	private void gameOver() {
		stopBackgroundClip();
		board.gameOver(level.getScore());
	}

	private void initBackgroundClip() {
		try {
			backgroundClip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(
					this.getClass().getResourceAsStream("/background.wav"));
			backgroundClip.open(inputStream);
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        }
	}
	
	private void initImages() {
		ImageFactory fact = ImageFactory.getInstance();
		
		try {
			BufferedImage explosion = fact.getImage("/explosion.png");
			BufferedImage[] imageExplosion = ImageUtil.splitImage(explosion , 4, 4);
			fact.addImageArray("explosion", imageExplosion);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void initBackground() {
		background = new Background(getWidth(), getHeight());
		background.initBackground();
	}
	
	private void initLevel() {
		InputStream in = this.getClass().getResourceAsStream("/level"+String.valueOf(currentLevel)+".map");
		
		// Fin du jeu quand le joueur a fini tous les niveaux
		if (null == in) {
			gameOver();
			return;
		}
		
		level = Level.create(in, getWidth());
	}

	private void startBackgroundClip() {
		backgroundClip.loop(-1);
	}
	
	private void stopBackgroundClip() {
		backgroundClip.stop();
	}
	
	private void drawPlayerBullets(Graphics g) {
		drawEntities(g, player.getBullets());
	}

	private void drawEnemyBullets(Graphics g) {
		drawEntities(g, getEnemyBullets());
	}

	private void drawEnemies(Graphics g) {
		drawEntities(g, level.getEnemies());
	}

	private void drawEntities(Graphics g, ArrayList<? extends Entity> entities) {
		if (!entities.isEmpty()) {
			for (Entity entity : entities) {
				entity.draw(g);
			}
		}
	}
	
	private void drawPlayer(Graphics g) {
		player.draw(g);
	}

	private void drawBackground(Graphics g) {
		background.draw(g);
	}

	private void movePlayer() {
		player.move(getWidth(), getHeight());
	}

	private void moveBullets() {
		ArrayList<Bullet> bullets = player.getBullets();
		if (!bullets.isEmpty()) {
			for (Entity bullet : bullets) {
				if (bullet.getY() < 0 - bullet.getHeight()) {
					player.removeBullet((Bullet)bullet);
					continue;
				}
				bullet.move(getWidth(), getHeight());
			}
		}
		
		bullets = getEnemyBullets();
		if (!bullets.isEmpty()) {
			for (Bullet bullet : bullets) {
				if (isOutOfScreen(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight())) {
					removeEnemyBullet((Bullet)bullet);
					continue;
				}
				bullet.move(getWidth(), getHeight(), player);
			}
		}
	}

	private boolean isOutOfScreen(double x, double y, int w, int h) {
		return x > getWidth() 
				|| x + w < 0
				|| y > getHeight()
				|| y + h <0;
	}

	private void moveEnemies() {
		Rectangle rPlayer = player.getHitBox();
		
		ArrayList<Enemy> enemies = level.getEnemies();
		if (!enemies.isEmpty()) {
			for (Enemy enemy : enemies) {
				if (enemy.getY() > (getHeight()+enemy.getHeight()/2)) {
					level.removeEnemy(enemy);
					continue;
				}
				
				if (!enemy.alive())
					continue;
				
				if (player.alive()) {
					Rectangle rEnemy = new Rectangle((int)enemy.getX(), (int)enemy.getY(), 
							enemy.getWidth(), enemy.getHeight());
					
					if (rEnemy.intersects(rPlayer)) {
						player.hit(enemy.getCollisionDamage());
						enemy.hit(enemy.getHp());
						continue;
					}
				}
				
				enemy.move(getWidth(), getHeight());
				enemy.fire(enemyBullets, player.getX(), player.getY());
			}
		}
	}
	
	private void checkForHits() {
		
		ArrayList<Enemy> enemies = level.getEnemies();
		ArrayList<Bullet> bullets = player.getBullets();
		
		for (Bullet b : bullets) {
			for (Enemy enemy : enemies) {
				if (!enemy.alive())
					continue;
				
				Rectangle hitBox = enemy.getHitBox();
				
				if (b.getX() > hitBox.x
						&& b.getX() < hitBox.x + hitBox.getWidth() 
						&& b.getY() > hitBox.y
						&& b.getY() < hitBox.y + hitBox.getHeight()) {
					enemy.hit(b.getDamage());
					player.removeBullet(b);
				}
			}
		}
		
		if (!player.alive()) return;
		
		Rectangle playerHitBox = player.getHitBox();
		bullets = getEnemyBullets();
		
		if (!bullets.isEmpty()) {
			for (Bullet b : bullets) {
				if (!b.visible()) {
					removeEnemyBullet(b);
					continue;
				}
				
				if (b.getX() > playerHitBox.x
						&& b.getX() < playerHitBox.x + playerHitBox.getWidth() 
						&& b.getY() > playerHitBox.y
						&& b.getY() < playerHitBox.y + playerHitBox.getHeight()) {
					player.hit(b.getDamage());
					removeEnemyBullet(b);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bullet> getEnemyBullets() {
		return (ArrayList<Bullet>) enemyBullets.clone();
	}
	
	public void removeEnemyBullet(Bullet b) {
		enemyBullets.remove(b);
	}
	
}

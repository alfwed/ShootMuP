package shootemup.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import shootemup.Board;
import shootemup.bullet.Bullet;
import shootemup.bullet.BulletFighter;
import shootemup.enemy.Enemy;
import shootemup.factory.ImageFactory;
import shootemup.sprite.LinearAnimatedSprite;

public class Fighter extends Enemy {

	private int chanceToFire = 5000/Board.DELAY;
	
	public Fighter(int x, int y) {
		super(x, y);
		
		ImageFactory fact = ImageFactory.getInstance();
		BufferedImage[] images = new BufferedImage[1];
		BufferedImage[] imagesHit = new BufferedImage[1];
		BufferedImage[] imageExplosion = null;
		
		try {
			images[0] = fact.getImage("/Fighter.png");

			imagesHit[0] = fact.getImage("/Fighter_hit.png");
			
			imageExplosion = fact.getImageArray("explosion");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		
		las = new LinearAnimatedSprite(images);
		las.addNewAnimation("center", 0);

		spriteHit = new LinearAnimatedSprite(imagesHit);
		spriteHit.addNewAnimation("center", 0);

		spriteExplosion = new LinearAnimatedSprite(imageExplosion);
		spriteExplosion.addNewAnimation("center", 15, 0, 30/Board.DELAY);
		
		changeSprite(las);

		width = images[0].getWidth(null);
		height = images[0].getHeight(null);
				
		setSpeedx(0);
		setSpeedy(speed);
		
		hp = 100;
		collisionDamage = 30;
		scorePoints = 100;
	}

	public void fire(ArrayList<Bullet> bullets, double playerX, double playerY) {
		if (!alive)
			return;
		
		Random generator = new Random();
		int dice = generator.nextInt(chanceToFire);
		
		if (dice == 1) {
			double x = getX();
			double y = getY() ;
			Bullet bullet = new BulletFighter((int)x, (int)y);
			
			double dx = playerX - x;
			double dy = playerY - y;
			double distance = Math.sqrt(dx*dx + dy*dy);
			
			bullet.setSpeedx(dx/distance*BulletFighter.speed);
			bullet.setSpeedy(dy/distance*BulletFighter.speed);
			bullets.add(bullet);
		}
	}

}

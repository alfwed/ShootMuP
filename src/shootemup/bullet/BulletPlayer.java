package shootemup.bullet;

import java.io.IOException;


import shootemup.Board;
import shootemup.factory.ImageFactory;

public class BulletPlayer extends Bullet {

	private double speed = 0.9*Board.DELAY;
	
	public BulletPlayer(int x, int y) {
		super(x, y);
		
		ImageFactory fact = ImageFactory.getInstance();
		try {
			sprite.setImage(fact.getImage("/bulletPlayer.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		width = sprite.getWidth();
		height = sprite.getHeight();
		
		setSpeedx(0);
		setSpeedy(-1 * speed);
		damage = 50;
	}

}

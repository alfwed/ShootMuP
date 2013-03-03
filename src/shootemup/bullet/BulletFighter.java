package shootemup.bullet;

import javax.swing.ImageIcon;

import shootemup.Board;

public class BulletFighter extends Bullet {

	public static double speed = 0.3*Board.DELAY;
	
	public BulletFighter(int x, int y) {
		super(x, y);
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/bulletFighter.png"));
		sprite.setImage(ii.getImage());
		
		width = ii.getImage().getWidth(null);
		height = ii.getImage().getHeight(null);
		
		damage = 10;
	}

}

package shootemup.bullet;

import javax.swing.ImageIcon;

import shootemup.Board;
import shootemup.Player;

public class BulletBomber extends Bullet {

	public static double speed = 0.25*Board.DELAY;
	
	private int fuel = Math.round(10000 / Board.DELAY);
	private double angle = 3*Math.PI/2;
	private double angularSpeed = 0.01;
	
	public BulletBomber(int x, int y) {
		super(x, y);
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/bulletBomber.png"));
		sprite.setImage(ii.getImage());
		
		width = ii.getImage().getWidth(null);
		height = ii.getImage().getHeight(null);
		
		damage = 20;
	}

	public void move(int boardWidth, int boardHeight, Player player) {
		
		if (fuel == 0) {
			explode();
			return;
		}
		
		fuel--;
		
		double dx = player.getX() - getX();
		double dy = player.getY() - getY();
		double distance = Math.sqrt(dx*dx + dy*dy);
		angle = Math.acos(dx/distance);
		if (dy>0)
			angle = -angle;
		
//		double nAngle = Math.acos(dx/distance);
//		if (dy>0)
//			nAngle = 2*Math.PI - nAngle;
//		
//		double da = nAngle - angle;
//		int sign = (int)(da / Math.abs(da));
//		
//		if (Math.abs(da) > angularSpeed)
//			da = sign * angularSpeed;
//		
//		angle = (angle + da) % (2*Math.PI);
		
		speedx = Math.cos(angle) * speed;
		speedy = -Math.sin(angle) * speed;
		super.move(boardWidth, boardHeight, player);
	}

	private void explode() {
		speedx = 0;
		speedy = 0;
		visible = false;
	}
	
}

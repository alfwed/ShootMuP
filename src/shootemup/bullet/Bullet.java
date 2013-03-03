package shootemup.bullet;

import shootemup.Entity;
import shootemup.Player;

abstract public class Bullet extends Entity {

	protected double damage;
	
	protected boolean visible = true;
	
	public Bullet(int x, int y) {
		super(x, y);
	}

	public double getDamage() {
		return damage;
	}

	public void move(int boardWidth, int boardHeight, Player player) {
		super.move(boardWidth, boardHeight);
	}
	
	public boolean visible() {
		return visible;
	}
	
}

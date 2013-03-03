package shootemup;

import java.awt.Graphics;

import shootemup.sprite.Sprite;

public class Entity {
	
	protected double x;
	protected double y;
	protected double speedx;
	protected double speedy;
	protected Sprite sprite = new Sprite();
	protected int width, height;
	
	public Entity(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void draw(Graphics g) {
		g.drawImage(sprite.getImage(), (int)x, (int)y, null);
	}
	
	public void move(int boardWidth, int boardHeight) {
		setX(x+speedx);
		setY(y+speedy);
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getSpeedx() {
		return speedx;
	}
	public void setSpeedx(double speedx) {
		this.speedx = speedx;
	}
	public double getSpeedy() {
		return speedy;
	}
	public void setSpeedy(double speedy) {
		this.speedy = speedy;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}

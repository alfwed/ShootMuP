package shootemup.sprite;

import java.awt.Image;

public class Sprite {

	private Image image;
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image i) {
		image = i;
	}
	
	public int getWidth() {
		return image.getWidth(null);
	}
	
	public int getHeight() {
		return image.getHeight(null);
	}
}

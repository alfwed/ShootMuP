package shootemup.factory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import shootemup.sprite.LinearAnimatedSprite;

public class ImageFactory {
	private static ImageFactory instance;
	
	private HashMap<String, BufferedImage> images;
	private HashMap<String, BufferedImage[]> imageArray;
	private HashMap<String, LinearAnimatedSprite> sprites;
	
	private ImageFactory() {
		images = new HashMap<String, BufferedImage>();
		imageArray = new HashMap<String, BufferedImage[]>();
		sprites = new HashMap<String, LinearAnimatedSprite>();
	}
	
	public static ImageFactory getInstance() {
		if (null == instance) {
			instance = new ImageFactory();
		}
		
		return instance;
	}
	
	public BufferedImage getImage(String name) throws IOException {
		if (!images.containsKey(name)) {
			images.put(name, ImageIO.read(this.getClass().getResource(name)));
		}
		
		return images.get(name);
	}
	
	public void addImage(String name, BufferedImage image) {
		if (!images.containsKey(name)) {
			images.put(name, image);
		}
	}
	
	public BufferedImage[] getImageArray(String name) {
		if (!imageArray.containsKey(name)) {
			return null;
		}
		
		return imageArray.get(name);
	}
	
	public void addImageArray(String name, BufferedImage[] i) {
		if (!imageArray.containsKey(name)) {
			imageArray.put(name, i);
		}
	}
	
	public LinearAnimatedSprite getSprite(String name) {
		if (!sprites.containsKey(name)) {
			return null;
		}
		
		return new LinearAnimatedSprite(sprites.get(name));
	}

	public void addSprite(String name, LinearAnimatedSprite sprite) {
		if (!sprites.containsKey(name)) {
			sprites.put(name, sprite);
		}
	}
	
	
}

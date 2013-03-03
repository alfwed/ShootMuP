package shootemup.sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class LinearAnimatedSprite {
	private double x;
	private double y;

	/**
	 * This holds all the frames, named by a string key; for instance, you could
	 * enter the key 'walk' which would return a frameset, frameset-walk-right,
	 * frameset-walk-left... etc.
	 */
	private HashMap<String, Integer> frames;

	/**
	 * All the images of this AnimatedSprite
	 */
	private BufferedImage[] images;

	/**
	 * The current 'state' or animation that the sprite is in
	 */
	private String currentAnim;

	/**
	 * The current frame being rendered
	 */
	private int currentFrame;
	
	/**
	 * The target frame to move to
	 */
	private int targetFrame;
	
	private int delay = 1;
	private int delayCounter = 0;

	
	public LinearAnimatedSprite(BufferedImage[] images) {
		this.images = images;
		frames = new HashMap<String, Integer>();
	}
	
	public LinearAnimatedSprite(LinearAnimatedSprite sprite) {
		duplicate(sprite);
	}

	private void duplicate(LinearAnimatedSprite sprite) {
		if (null == sprite)
			return;
		
		frames = sprite.getFrames();
		images = sprite.getImages();
		currentAnim = sprite.getCurrentAnim();
		currentFrame = sprite.getCurrentFrame();
		targetFrame = sprite.getTargetFrame();
		delay = sprite.getDelay();
	}

	private int getDelay() {
		return delay;
	}

	private int getTargetFrame() {
		return targetFrame;
	}

	private int getCurrentFrame() {
		return currentFrame;
	}

	private BufferedImage[] getImages() {
		return images;
	}

	private HashMap<String, Integer> getFrames() {
		return frames;
	}

	public void addNewAnimation(String name, int position) {
		frames.put(name, position);
		setAnimation(name);
		currentFrame = position;
	}
	
	public void addNewAnimation(String name, int position, int delay) {
		frames.put(name, position);
		setAnimation(name);
		currentFrame = position;
	}
	
	public void addNewAnimation(String name, int position, int start, int delay) {
		frames.put(name, position);
		setAnimation(name);
		currentFrame = start;
		this.delay = delay;
	}

	/**
	 * Draws the current frame of the sprite
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		delayCounter++;
		
		if (delayCounter == delay) {
			if (currentFrame < targetFrame) {
				currentFrame++;
			} else if (currentFrame > targetFrame) {
				currentFrame--;
			}
			delayCounter = 0;
		}
		
		g.drawImage(images[currentFrame], (int) x, (int) y, null);
	}

	/**
	 * Sets the current animation of the Sprite
	 * 
	 * @param name
	 */
	public void setAnimation(String name) {
		if (frames.containsKey(name)) {
			currentAnim = name;
			targetFrame = frames.get(currentAnim);
		}
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
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

	public String getCurrentAnim() {
		return currentAnim;
	}

	/**
	 * Gets the width of one frame
	 * 
	 * @return
	 */
	public int getWidth() {
		return images[0].getWidth();
	}

	/**
	 * Gets the height of one frame
	 * 
	 * @return
	 */
	public int getHeight() {
		return images[0].getHeight();
	}

}

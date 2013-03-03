package shootemup;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AnimatedSprite {
	/**
	 * This constant is for looping the animation backwards, so it would be
	 * animated, if the frames were "1,2,3" like this, "1,2,3,2,1" etc.
	 */
	public static final int LOOP_REVERSE = 2;
	/**
	 * This constant is for looping the animation to the beggining, so it would
	 * be animated, if the frames were "1,2,3" like this, "1,2,1,2,3" etc.
	 */
	public static final int LOOP_BEGINNING = 3;
	/**
	 * The loop method, will be one of the above
	 */
	private int loopMethod = LOOP_BEGINNING;
	/**
	 * Up is true, Down is false
	 */
	private boolean loopDir = true;

	/**
	 * The coordinates of the sprite, upper left
	 */
	private double x;
	private double y;

	/**
	 * This holds all the frames, named by a string key; for instance, you could
	 * enter the key 'walk' which would return a frameset, frameset-walk-right,
	 * frameset-walk-left... etc.
	 */
	private HashMap<String, int[]> frames;

	/**
	 * All the images of this AnimatedSprite
	 */
	private BufferedImage[] images;

	/**
	 * The current 'state' or animation that the sprite is in
	 */
	private String currentAnim;

	/**
	 * The current frame being rendered, it's not the number of the frame, its
	 * the key for the array as in it's this number
	 * "frameset[currentframe] = 1;" not this number
	 * "frameset[0] = currentframe;"
	 */
	private int currentFrame;

	/**
	 * The current set of frames being used
	 */
	private int[] currentFrameSet;

	public AnimatedSprite(BufferedImage[] images) {
		this.images = images;
		frames = new HashMap<String, int[]>();
	}

	/**
	 * Lets you add a new animation set to the Sprite
	 * 
	 * @param name
	 *            The name of the Sprite, this name will be used in setting the
	 *            animation
	 * @param set
	 *            The frames of the animation
	 */
	public void addNewAnimation(String name, int[] set) {
		frames.put(name, set);
		setAnimation(name);
	}

	/**
	 * Draws the current frame of the sprite
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		// Now we need to get the current frame, from the current frameset
		int imgNum = currentFrameSet[currentFrame];
		g.drawImage(images[imgNum], null, (int) x, (int) y);

		// Increment the current frame
		if (currentFrame == currentFrameSet.length - 1) {
			if (loopMethod == LOOP_BEGINNING) {
				currentFrame = 0;
			} else {
				loopDir = false;
				currentFrame--;
			}
		} else {
			if (loopMethod == LOOP_BEGINNING) {
				currentFrame++;
			} else {
				if (currentFrame == 0) {
					loopDir = true;
				}
				if (loopDir) {
					currentFrame++;
				} else {
					currentFrame--;
				}
			}
		}
	}

	/**
	 * Sets the current animation of the Sprite
	 * 
	 * @param name
	 */
	public void setAnimation(String name) {
		if (frames.containsKey(name)) {
			currentAnim = name;
			currentFrameSet = frames.get(currentAnim);
			currentFrame = 0;
		}
	}

	/**
	 * Sets how the Sprite cycles through the animations.
	 * 
	 * @param method
	 *            One of the constants LOOP_REVERSE or LOOP_BEGINNING
	 *            LOOP_BEGINNING: Will start over when loop reaches the edn
	 *            LOOP_REVERSE: Will start backwards when loop is over
	 */
	public void setLoopMethod(int method) {
		if (method == LOOP_REVERSE || method == LOOP_BEGINNING) {
			this.loopMethod = method;
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
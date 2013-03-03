package shootemup;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

public interface AppStrategy {
	
	public void paint(Graphics g);
	
	public void update();
	
	public void keyReleased(KeyEvent e);
	
	public void keyPressed(KeyEvent e);
}

package shootemup;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ShootGame extends JFrame {

	private Thread gameThread;

	public ShootGame() {
		Board board = new Board();
		
		

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		GraphicsConfiguration gc = device.getDefaultConfiguration();
		setSize(gc.getBounds().width, gc.getBounds().height);
		setUndecorated(true);
		setResizable(false);
		setTitle("Shoot");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(0, 0);
		setAlwaysOnTop(true);
		setContentPane(board);
		setVisible(true);
//		GraphicsEnvironment.getLocalGraphicsEnvironment().
//		getDefaultScreenDevice().setFullScreenWindow(this);
		
		board.init();
		gameThread = new Thread(board);
		gameThread.start();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ShootGame();
	}

}

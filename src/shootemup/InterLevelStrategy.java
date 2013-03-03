package shootemup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class InterLevelStrategy implements AppStrategy {

	private Board board;
	
	private int level;
	
	public InterLevelStrategy(Board board, int level) {
		this.board = board;
		this.level = level;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, board.getWidth(), board.getHeight());
		g.setFont(new Font("Helvetica", Font.PLAIN, 96));
		g.setColor(Color.BLUE);
		g.drawString("LEVEL "+level, board.getWidth()/2 - 180, board.getHeight()/2 - 30);
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 24));
		g.setColor(new Color(60,60,60));
		g.drawString("Hit space to continue", board.getWidth()/2 - 115, board.getHeight() - 100);
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_SPACE) {
			board.play();
		}
	}

}

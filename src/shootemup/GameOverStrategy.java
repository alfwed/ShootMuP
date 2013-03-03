package shootemup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class GameOverStrategy implements AppStrategy { 
	
	private Board board;
	private int score = 0;
	
	public GameOverStrategy(Board board) {
		this.board = board;
	}
	
	public void setScore(int s) {
		score = s;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, board.getWidth(), board.getHeight());
		g.setFont(new Font("Helvetica", Font.PLAIN, 64));
		g.setColor(Color.RED);
		g.drawString("GAME OVER", board.getWidth()/2 - 200, board.getHeight()/2 - 30);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString(String.valueOf(score), 10, 20);
	}
	
	public void update() {
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_SPACE) {
			board.restart();
		}
	}
}

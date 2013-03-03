package shootemup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable {

	final public static int DELAY = 2;
	
	private AppStrategy stateStrategy;
	private HashMap<String, AppStrategy> stateStrategies;
	
	
	public Board() {
		addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);
        
        stateStrategies = new HashMap<String, AppStrategy>();
        IdleStrategy idle = new IdleStrategy();
        stateStrategies.put("idle", idle);
        stateStrategy = idle;
	}

	public void init() {
		PlayingStrategy playingStrategy = new PlayingStrategy(
				new Dimension(getWidth(), getHeight()), this);
		playingStrategy.restart();
		stateStrategies.put("playing", playingStrategy);

		GameOverStrategy gameover = new GameOverStrategy(this);
		stateStrategies.put("gameover", gameover);
		
		stateStrategy = new InterLevelStrategy(this, playingStrategy.getCurrentLevel());
		
	}
	
	public void levelComplete() {
		PlayingStrategy s = (PlayingStrategy)stateStrategy;
		s.nextLevel();
		
		if (stateStrategy instanceof GameOverStrategy)
			return;

		stateStrategy = new InterLevelStrategy(this, s.getCurrentLevel());
	}
	
	public void play() {
		PlayingStrategy s = (PlayingStrategy)(stateStrategies.get("playing"));
		s.resume();
		stateStrategy = s;
	}
	
	public void gameOver(int score) {
		GameOverStrategy s = (GameOverStrategy)(stateStrategies.get("gameover"));
		s.setScore(score);
		stateStrategy = stateStrategies.get("gameover");
	}
	
	public void restart() {
		PlayingStrategy s = (PlayingStrategy)stateStrategies.get("playing");
		s.restart();
		
		stateStrategy = new InterLevelStrategy(this, s.getCurrentLevel());
	}

	public void paint(Graphics g) {
		super.paint(g);
		stateStrategy.paint(g);
	}

	private void gameLoop() {
		stateStrategy.update();
	}
	
	public void run() {
		long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();
        
		while (true) {
			try {
				gameLoop();
				repaint();
				
				timeDiff = System.currentTimeMillis() - beforeTime;
	            sleep = DELAY - timeDiff;

	            if (sleep < 0) {
	            	System.out.println(sleep);
	                sleep = 1;
	            }
	            try {
	                Thread.sleep(sleep);
	            } catch (InterruptedException e) {
	                System.out.println("interrupted");
	            }
	            beforeTime = System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class TAdapter extends KeyAdapter {
		
		public void keyReleased(KeyEvent e) {
			stateStrategy.keyReleased(e);
		}
		
		public void keyPressed(KeyEvent e) {
			stateStrategy.keyPressed(e);
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
		}
	}
	
}

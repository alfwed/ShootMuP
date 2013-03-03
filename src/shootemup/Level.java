package shootemup;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import shootemup.enemy.Bomber;
import shootemup.enemy.Enemy;
import shootemup.enemy.Fighter;

public class Level {

	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	private ArrayList<Wave> waves;
	private Iterator<Wave> waveIterator;
	private Wave nextWave;
	private int progress = 0;
	
	private int score = 0;
	
	private int offsetLeft;
	
	boolean finished = false;
	
	
	public Level(ArrayList<Wave> waves, int boardWidth) {
		this.waves = waves;
		waveIterator = this.waves.iterator();
		if (waveIterator.hasNext())
			nextWave = waveIterator.next();
		else 
			finished = true;
		
		if (boardWidth > 1280) {
			offsetLeft = (int)Math.round((boardWidth - 60*19) / 2)+30; 
		} else {
			offsetLeft = 70+30;
		}
	}
	
	public static Level create(InputStream levelMap, int width) {
		
		int interval;
		ArrayList<Wave> waves = new ArrayList<Wave>();
		
		Scanner scanner;
		scanner = new Scanner(new InputStreamReader(levelMap));
		while (scanner.hasNextLine()) {
		    String line = scanner.nextLine();
		    
		    //System.out.println(line);
		    if (line.isEmpty())
		    	continue;
		    
		    interval = (int)Math.round(Integer.valueOf(line.split(";")[0])/Enemy.speed);
		    String[] ennemis = new String[19];
		    System.arraycopy(line.split(";"), 1, ennemis, 0, 19);
		    
		    Wave w = new Wave(interval, ennemis);
		    waves.add(w);
		}
		
		return new Level(waves, width);
	}
	
	public boolean finished() {
		return finished;
	}
	
	public boolean complete() {
		return finished && enemies.size() == 0;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Enemy> getEnemies() {
		return (ArrayList<Enemy>) enemies.clone();
	}
	
	public void update() {
		
		for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {
			Enemy enemy = it.next();
			if (!enemy.alive() && !enemy.visible()) {
				score += enemy.getScorePoints();
				it.remove();
			}
		}
		
		if (finished)
			return;
		
		progress++;
		
		if (progress == nextWave.getInterval()) {
			progress = 0;
			buildWave();
			if (waveIterator.hasNext())
				nextWave = waveIterator.next();
			else 
				finished = true;
		}
	}
	
	public void removeEnemy(Enemy e) {
		enemies.remove(e);
	}

	private void buildWave() {
		String[] wave = nextWave.getContent();
		for (int i=0; i < wave.length; i++) {
			if (wave[i].equals("F")) {
				Enemy enemy = new Fighter(offsetLeft + i*60, -40);
				enemies.add(enemy);
			} else if (wave[i].equals("B")) {
				Enemy enemy = new Bomber(offsetLeft + i*60, -40);
				enemies.add(enemy);
			}
		}
	}

	public int getScore() {
		return score;
	}
	
}

package shootemup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Background {

	private ArrayList<Image> layers = new ArrayList<Image>();
	final private int nbLayers = 2; 
	private int[] counters;
	
	private int boardWidth, boardHeight;
	
	
	public Background(int w, int h) {
		boardWidth = w;
		boardHeight = h;
		
		counters = new int[nbLayers];
		for(int j=0; j<counters.length; j++) {
			counters[j] = 0;
		}
	}
	
	public void initBackground() {
		
		for (int j=1; j <= nbLayers; j++) {
			BufferedImage image = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D b = (Graphics2D) image.getGraphics();
			
			if (j == 1) {
				b.setColor(new Color(10,10,10));
				b.fillRect(0, 0, boardWidth, boardHeight);
			}
			
	        for (int i=0; i < boardWidth*boardHeight/5000; i++) {
				int size = (int)Math.round(Math.random() + 0.6*j);
				int x = (int)Math.round(Math.random() * boardWidth);
				int y = (int)Math.round(Math.random() * boardHeight);
				
				if (x % 2 == 0)
					b.setColor(Color.white);
				else
					b.setColor(new Color(240,240,240));
				
				b.fillRect(x, y, size, size);
			}
			
			b.dispose();
			layers.add(image);
		}
		
	}

	public void draw(Graphics g) {
		int nbLayers = layers.size();
		for (int i=0; i<nbLayers; i++) {
			Image layer = layers.get(i);

			counters[i] += 1;
		
			int dy = counters[i]/((nbLayers - i)*7/Board.DELAY);
			
			g.drawImage(layer, 0, 0, layer.getWidth(null), dy, 
					0, layer.getHeight(null)-dy, layer.getWidth(null), layer.getHeight(null), null);
			g.drawImage(layer, 0, dy, layer.getWidth(null), layer.getHeight(null), 
					0, 0, layer.getWidth(null), layer.getHeight(null)-dy, null);
			
			if (dy == layer.getHeight(null)) {
				counters[i] = 0;
			}
		}
	}
	
}

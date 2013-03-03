package shootemup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class UnitFrame {
	
	private int x, y, h, w;
	
	public UnitFrame(int w, int h) {
		this.x = w-30;
		this.y = 0;
		this.h = h;
		this.w = 30;
	}
	
	public void draw(Graphics g, Player player) {
		
		g.setFont(new Font("Monospaced", Font.BOLD, 12));
		g.setColor(new Color(0,100,0));
		
		double barPartsLength = (h - (player.getHealth() / 10f - 1f) * 1f) / (player.getHealth() / 10f);
		double partLength = barPartsLength; 
		int i = 0;
		int hpToDraw = (int)player.getHp();
		
		while (hpToDraw > 0) {
			if (hpToDraw < 10)
				partLength = barPartsLength * (float)hpToDraw / 10f;
			
			g.fillRect(x+1, y+h - i*(int)barPartsLength - i*1 - (int)partLength, w-1, (int)partLength);
			i++;
			hpToDraw -= 10;
		}
	}
}

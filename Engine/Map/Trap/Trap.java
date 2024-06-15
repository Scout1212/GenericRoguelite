package Engine.Map.Trap;

import Engine.Parents.Player.Player;

import javax.swing.*;
import java.awt.*;

public class Trap{
    private int x, y, width, height, dmg, dmgTick;
    private Image asset =  new ImageIcon(getClass().getResource("IndividualSpike.png")).getImage();
    public Trap(int leftBound, int rightBound, int topBound, int bottomBound){
        dmg = 1;
        dmgTick = -1;

        width = 100;
        height = 100;

        x = (int)(Math.random() * (rightBound - leftBound) - width) + leftBound;
        y = (int)(Math.random() * ((bottomBound - topBound) - height) + topBound);
    }

    public void collision(Player p, int tick){
        int dmgCooldown = 2;

        if(x < p.getX() + p.getWidth() && x + width > p.getX() && y < p.getY() + p.getHeight() && y + height > p.getY()){
            if(tick > dmgTick){
                p.takeDmg(dmg);
                dmgTick = tick + dmgCooldown;
            }
        }
    }

    public void randomPosition(int leftBound, int rightBound, int topBound, int bottomBound){
        x = (int)(Math.random() * (rightBound - leftBound) - width) + leftBound;
        y = (int)(Math.random() * ((bottomBound - topBound) - height) + topBound);
    }

    public void drawSelf(Graphics g){
        //g.drawImage(asset, x, y, width, height, null);
        g.setColor(Color.black);
        g.fillRect(x, y, width, height);
    }

}
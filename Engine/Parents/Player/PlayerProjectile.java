package Engine.Parents.Player;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;

public class PlayerProjectile{
    private double x, y, vx, vy, speedMult, resistance;
    
    private Color col;
    private int type, diam, dmg;
    private int direction; // 1 is up

    private Image fireball;

    //todo maybe i can do the bomb stuff in here
    public PlayerProjectile(int x, int y, int vx, int vy, double speedMult, double resistance, int dmg){
        diam = 60;
        col = Color.BLACK;

        fireball = new ImageIcon(getClass().getResource("PlayerImages/PlayerProjectile.gif")).getImage();

        this.x = x;
        this.y = y;
        //fix with taking vx and vy
        this.vx = vx;
        this.vy = vy;
        this.speedMult = speedMult;
        this.resistance = resistance;
        this.dmg = dmg;
    }
    
    public void drawSelf(Graphics g){
        g.setColor(col);
        
        
        g.drawImage(fireball, (int)x, (int)y, diam, diam, null);
    }

    public boolean act(int leftBound, int rightBound, int topBound, int bottomBound){
        //returns true if it has to disappear

        x += vx;
        y += vy;

        double nextX = x + vx;
        double nextY = y + vy;

        if(nextX > rightBound)
            return true;
        else if(nextX < leftBound)
            return true;
        else if(nextY > bottomBound)
            return true;
        else if(nextY < topBound)
            return true;

        vx *= speedMult;
        vy *= speedMult;

        //resistance is the counter force to make it slower

        if(vx < 0)
            vx += resistance;
        else if(vx >0)
            vx -= resistance;

        if(vy < 0)
            vy += resistance*speedMult;
        else if(vy > 0)
            vy -= resistance*speedMult;

        if(Math.abs(vx) < resistance*speedMult && Math.abs(vy) < resistance*speedMult)
            return true;

        return false;
    }
    
    public int getDiam(){
        return diam;
    }

    public int getCenterX(){
        return (int)((x*2 + diam)/2 + .5);
    }
    
    public int getCenterY(){
        return (int)((y*2 + diam)/2 + .5);
    }
    
    public int getDmg(){
        return dmg;
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }
}
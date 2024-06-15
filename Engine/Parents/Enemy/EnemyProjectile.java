package Engine.Parents.Enemy;

import javax.swing.*;
import java.awt.*;

public class EnemyProjectile{
    
    //todo fix vx and vy stuff
    
    private double x, y, vx, vy, resistance;
    
    private Color col;
    private int diam, dmg;
    private Image redball;
    
    public EnemyProjectile(int x, int y, int targetX, int targetY, double speed, double resistance, int dmg){
        diam = 30;
        col = Color.RED;
        this.x = x;
        this.y = y;

        redball = new ImageIcon(getClass().getResource("EnemyImages/EnemyProjectile.gif")).getImage();
        //fix with taking vx and vy
        this.resistance = resistance;
        this.dmg = dmg;
        
        //this slope thing needs to be fixed

        double distanceX = targetX - x;
        double distanceY = targetY - y;

        calcVelos(speed, distanceX, distanceY);
    }
    
    public void drawSelf(Graphics g){
        g.setColor(col);
        g.drawImage(redball, (int)x, (int)y, diam, diam, null);
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
        
        //resistance is the counter force to make it slower
        
        if(vx < 0)
            vx += resistance;
        else if(vx >0)
            vx -= resistance;
        
        if(vy < 0)
            vy += resistance;
        else if(vy > 0)
            vy -= resistance;
        
        if(Math.abs(vx) < resistance && Math.abs(vy) < resistance)
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

    public void calcVelos(double speed, double difX, double difY){
        double hypDist = Math.sqrt(difX * difX + difY * difY);
        vx = (difX/hypDist) * speed;
        vy = (difY/hypDist) * speed;
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }
}

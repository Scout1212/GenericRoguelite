package Engine.Parents.Enemy;

import Engine.Parents.Player.PlayerProjectile;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;

public class Enemy{
    private int width, height, direction, maxVelo, shootingTick, shootingCooldown, dmg;
    private double projSpd;
    private double x, y, vx, vy, accel, resistance;
    private boolean scared;
    private AudioClip shoot;
    private int hp;
    private Image walkRight, walkDown, walkUp, walkLeft, idle;
    
    public Enemy(int leftBound, int rightBound, int topBound, int bottomBound, int difficulty){

        double scaler = difficulty * .1 + 1;

        int shrinker = 30;
        this.width = 50;
        this.height = (int)(width * 1.5);

        //what is wrong with this!

        x = (int)(Math.random()* (rightBound - (leftBound + width + shrinker)) + leftBound + shrinker);
        y =(int)(Math.random() * (bottomBound - (topBound + height + shrinker)) + topBound + shrinker);



        walkRight = new ImageIcon(getClass().getResource("EnemyImages/EnemyWalkRight.gif")).getImage();
        walkDown = new ImageIcon(getClass().getResource("EnemyImages/EnemyWalkDown.gif")).getImage();
        walkUp =  new ImageIcon(getClass().getResource("EnemyImages/EnemyWalkUp.gif")).getImage();
        walkLeft = new ImageIcon(getClass().getResource("EnemyImages/EnemyWalkLeft.gif")).getImage();
        idle = new ImageIcon(getClass().getResource("EnemyImages/EnemyIdle.gif")).getImage();

        shoot = Applet.newAudioClip(getClass().getResource("EnemyAudio/Portal.wav"));
        vx = 0;
        vy = 0;

        direction = 1;
        hp = (int)(2 * scaler) + 5;
        scared = (int)(Math.random() * 10) == 1;

        //todo make them get harder but still possible and fun
        maxVelo = (int)((Math.random()*4 + 1) * scaler);

        //something is off witht he shooting ticks
        shootingTick = -1;
        shootingCooldown = (int)(Math.random()*60 +  10 - scaler);
        dmg = 2;
        
        //how fast the bullet decellerates 
        resistance = .4 /(difficulty * 1.01);
        
        //projectile speedMultiplier
        projSpd = 15 + scaler;
        
        //how fast it will accel
        accel = 1;
    }


    //todo change this into checking the sides of the thing
    public boolean projCollision(PlayerProjectile p){

        for(int i =(int)x; i < x + width; i++){
            boolean topSide = distance(i, (int)y, p.getCenterX(), p.getCenterY()) < (double) p.getDiam()/2;
            boolean bottomSide = distance(i, (int)y + height, p.getCenterX(), p.getCenterY()) < (double)p.getDiam()/2;
            if(topSide || bottomSide){
                if(hp > 0){
                    hp -= p.getDmg();
                }
                return true;
            }

        }
        for(int i = (int)y; i< y + height; i++){
            boolean leftSide = distance((int)x, i, p.getCenterX(), p.getCenterY()) < (double) p.getDiam()/2;
            boolean rightSide = distance((int)x + width, i, p.getCenterX(), p.getCenterY()) < (double) p.getDiam()/2;
            if(leftSide || rightSide){
                if(hp > 0){
                    hp -= p.getDmg();
                }
                return true;
            }
        }

        return false;
    } 
    
    public void EnemyCollision(Enemy e, int leftBound, int rightBound, int topBound, int bottomBound){
        if(x < e.getX() + e.getWidth() && x + width > e.getX() && y < e.getY() + e.getHeight() && y + height > e.getY()){
            if(getCenterX() < e.getCenterX()){
                accelLeft(leftBound);
                e.accelRight(rightBound);
            }
            else{
                accelRight(rightBound);
                e.accelLeft(leftBound);
            }
            
            if(getCenterY() < e.getCenterY()){
                accelUp(topBound);
                e.accelDown(bottomBound);
            }
            else{
                accelDown(bottomBound);
                e.accelUp(topBound);
            }     
        }
    }
    public void drawSelf(Graphics g){
        if(direction == 1)
            g.drawImage(walkUp, (int)x, (int)y, width, height, null);
        else if(direction == 2)
            g.drawImage(walkRight, (int)x, (int)y, width, height, null);
        else if(direction == 3)
            g.drawImage(walkDown, (int)x, (int)y, width, height, null);
        else if(direction == 4)
            g.drawImage(walkLeft, (int)x, (int)y, width, height, null);
        else
            g.drawImage(idle, (int)x, (int)y, width, height, null);
    }
    
    //non scared go to player and scared enemies will run away
    public void act(int x, int y, int leftBound, int rightBound, int topBound, int bottomBound, int diam){
        double nextX = this.x + vx;
        double nextY = this.y + vy;
        if(hp < 3)
            scared = true;
        if(scared){
            if(x > this.x)
                accelLeft(leftBound);
            else if( x < this.x)
                accelRight(rightBound);
            
            if(y > this.y)
                accelUp(topBound);
            else if(y < this.y)
                accelDown(bottomBound);
        }
        else{
            if(distance((int)this.x,(int)this.y, x, y) > diam){
                if(x < this.x)
                    accelLeft(leftBound);
                else if(x > this.x)
                    accelRight(rightBound);

                if(y < this.y)
                    accelUp(topBound);
                else if( y > this.y)
                    accelDown(bottomBound);
            }
        }
        
        if(nextX < leftBound || nextX + width > rightBound)
            resetXVelo();
        if(nextY < topBound || nextY + height > bottomBound)
            resetYVelo();
        
        applyVelo();
    }
    
    public EnemyProjectile shoot(int tick, int targetX, int targetY){
        if(!scared){
            if (tick > shootingTick) {
                shoot.play();
                EnemyProjectile output = new EnemyProjectile((int) x, (int) y, targetX, targetY, projSpd, resistance, dmg);


                shootingTick = tick + shootingCooldown;

                return output;
            }else
                return null;
        }
        return null;
    }
    
    public double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    public void applyVelo(){
        x += vx;
        y += vy;
    }
    
    public void resetXVelo(){
        vx = 0;
        direction = 3;
    }
    public void resetYVelo(){
        vy = 0;
        direction = 3;
    }

    public void accelRight(int rightBound){
        if(vx < maxVelo)
            vx += accel;

        int nextX = (int)(x + vx);
        if(nextX + width > rightBound)
            resetXVelo();
    }
    public void accelLeft(int leftBound){
        if(vx > -1 * maxVelo)
            vx -= accel;

        int nextX = (int)(x+vx);
        if(nextX < leftBound)
            resetXVelo();
    }
    public void accelUp(int topBound){
        if(vy > -1 * maxVelo)
            vy -= accel;

        //next Y had a freaking += im angry "nextY = y += vy";
        int nextY = (int)(y + vy);
        if(nextY < topBound)
            resetYVelo();
    }
    public void accelDown(int bottomBound){
        if(vy < maxVelo)
            vy += accel;

        //next Y had a freaking += im angry "nextY = y += vy";
        int nextY = (int)(y + vy);
        if(nextY + height > bottomBound)
            resetYVelo();
    }


    public boolean isAlive(){
        return hp > 0;
    }
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getCenterX(){
        return (x*2 + width)/2;
    }
    
    public double getCenterY(){
        return (y*2 + height)/2;
    }
        
}
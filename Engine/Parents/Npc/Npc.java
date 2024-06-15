package Engine.Parents.Npc;

import Engine.Parents.Player.Player;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;


//im happy to say this whole class was copy and pasted from the other classes
//celebrate WOOOOOO

//todo

public class Npc{
    private int width, height, direction, maxVelo;
    private double x, y, accel, vx, vy;
    private int walkSteps;
    private int talkTicks;
    private boolean touched = false;
    private int displayOpacity;
    private AudioClip noise, thank;
    private Image monkey;

    public Npc(int leftBound, int rightBound, int topBound, int bottomBound){
        x = (int)(leftBound+(rightBound - leftBound)/2);
        y =(int)(topBound+(bottomBound - topBound)/2);

        noise = Applet.newAudioClip(getClass().getResource("NpcSound/MonkeyNoise.wav"));
        thank = Applet.newAudioClip(getClass().getResource("NpcSound/ThankYou.wav"));

        monkey = new ImageIcon(getClass().getResource("NpcImages/BongoMonkey.gif")).getImage();

        vx = 0;
        vy = 0;
        this.width = 50;
        this.height = 50;
        walkSteps = 0;
        talkTicks = 0;

        maxVelo = 3;
        accel = .4;
    }
    public void act(Player p,int leftBound, int rightBound, int topBound, int bottomBound){
        if(walkSteps == 0) {
            walkSteps = 50;
            direction = (int) (Math.random() * 4);
        }

        if (direction == 0)
            accelUp(topBound);
        else if (direction == 2)
            accelDown(bottomBound);
        else
            resetYVelo();

        if (direction == 1)
            accelRight(rightBound);
        else if (direction == 3)
            accelLeft(leftBound);
        else
            resetXVelo();

        playerCollision(p);

        if(Math.random() < .001)
            noise.play();

        applyVelo();
        walkSteps--;
    }
    public void drawSelf(Graphics g, boolean boughtItem, String name){
        g.setColor(Color.BLUE);

        //todo why monkey doing this
        g.drawImage(monkey, (int)x, (int)y, width, height, null);

        if(boughtItem)
            thank.play();


        if(talkTicks != 0){
            if(touched)
                drawText("Hey, I am bongo", g);

            talkTicks --;
        }
        else
            displayOpacity = 0;
    }

    public void drawText(String text, Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        //this could use fixing
        int positionX = (int)((x + width/2) - g.getFontMetrics().stringWidth(text)/2);
        int positionY = (int)(y - 10);

        g.drawString(text, positionX, positionY);
    }

    public void playerCollision(Player p){
        if(x < p.getX() + p.getWidth() && x + width > p.getX() && y < p.getY() + p.getHeight() && y + height > p.getY()){
            talkTicks = 50;
            touched = true;
        }
    }

    public void applyVelo(){
        x += vx;
        y += vy;
    }

    public void resetXVelo(){
        vx = 0;
    }
    public void resetYVelo(){
        vy = 0;
    }

    public void accelRight(int rightBound){
        if(vx < maxVelo)
            vx += accel;

        int nextX = (int)(x + vx);
        if(nextX + width > rightBound)
            resetXVelo();
    }
    public void accelLeft(int leftBound){
        if(vx > -maxVelo)
            vx -= accel;

        int nextX = (int)(x+vx);
        if(nextX < leftBound)
            resetXVelo();
    }
    public void accelUp(int topBound){
        if(vy > -maxVelo)
            vy -= accel;

        int nextY = (int)(y + vy);
        if(nextY < topBound)
            resetYVelo();
    }
    public void accelDown(int bottomBound){
        if(vy < maxVelo)
            vy += accel;

        int nextY = (int)(y + vy);
        if(nextY + height > bottomBound)
            resetYVelo();
    }
}

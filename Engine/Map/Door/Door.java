package Engine.Map.Door;

import Engine.Parents.Player.Player;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;

public class Door{
    private boolean open;
    private int x, y, width, height;
    private int position;
    boolean shop;
    private AudioClip opening;
    private AudioClip doorClose;
    private Image asset;
    //0 is top, 1 is right
    //this class sucks :(
    public Door(int leftBound, int rightBound, int topBound, int bottomBound, boolean shop){
        width = 75;
        height = 75;

        this.shop = shop;
        //todo increase the volume
        opening = Applet.newAudioClip(getClass().getResource("Audio/DoorOpen.wav"));
        doorClose = Applet.newAudioClip(getClass().getResource("Audio/DoorClose.wav"));

        position = (int)(Math.random()*2);

        if(position == 0){
            x = (leftBound+(rightBound - leftBound)/2) - width/2;
            y = topBound;
            if(shop){
                asset = null;
            }
            else
                asset = new ImageIcon(getClass().getResource("Images/TopDoor.png")).getImage();
        }
        else if(position == 1){
            x = rightBound - width;
            y =(topBound+(bottomBound - topBound)/2) - height;
            if(shop){
                asset = new ImageIcon(getClass().getResource("Images/ShopDoorRight.png")).getImage();
            }
            else
                asset = new ImageIcon(getClass().getResource("Images/RightDoor.png")).getImage();
        }
        open = false;
    }

    public void drawSelf(Graphics g){
        if(open)
            g.setColor(Color.green);
        else
            g.setColor(Color.black);

        int rx = 0;
        int ry = 0;
        int rw = 0;
        int rh = 0;

        if(position == 0){
            ry = y + height;
            rx = x;
            rw = width;
            rh = 10;
        }
        else if(position == 1 && shop){
            rx = x + width;
            ry = y;
            rh = height;
            rw = 10;
        }
        else if(position == 1){
            rx = x - 10;
            ry = y;
            rh = height;
            rw = 10;
        }

        g.fillRect(rx, ry, rw, rh);
        g.drawImage(asset, x, y, width, height, null);


    }

    public void randomPosition(int leftBound, int rightBound, int topBound, int bottomBound){
        position = (int)(Math.random()*2);
        if(position == 0){
            x = (leftBound+(rightBound - leftBound)/2) - width/2;
            y = topBound;
            asset = new ImageIcon(getClass().getResource("Images/TopDoor.png")).getImage();
        }
        else if(position == 1){
            x = rightBound - width;
            y =(topBound+(bottomBound - topBound)/2) - height;

            asset = new ImageIcon(getClass().getResource("Images/RightDoor.png")).getImage();
        }

    }


    public void setOpen(boolean a){
        open = a;
        //todo why is !open when the doors open?
        if(!open){
            opening.play();
        }
    }

    public boolean playerCollision(Player p){
        if(open){
            if(x < p.getX() + p.getWidth() && x + width > p.getX() && y < p.getY() + p.getHeight() && y + height > p.getY()) {
                doorClose.play();
                return true;
            }
        }

        return false;
    }

    public void setOnRightWall(int rightBound, int topBound, int bottomBound){
        x = rightBound - width;
        y =(topBound+(bottomBound - topBound)/2) - height/2;
        if(shop){
            asset = new ImageIcon(getClass().getResource("Images/ShopDoorRight.png")).getImage();
        }
        else {
            asset = new ImageIcon(getClass().getResource("Images/RightDoor.png")).getImage();

        }

        position = 1;
    }

    public void setOnLeftWall(int leftBound, int topBound, int bottomBound){
        x = leftBound;
        y = (topBound+(bottomBound - topBound)/2) - height/2;

        asset = new ImageIcon(getClass().getResource("Images/ShopDoorLeft.png")).getImage();

        position = 1;
    }
    public void setOnTopWall(int leftBound, int rightBound, int topBound){
        x = (leftBound+(rightBound - leftBound)/2) - width/2;
        y = topBound;
        asset = new ImageIcon(getClass().getResource("Images/TopDoor.png")).getImage();

        position = 0;
    }

    public int getPosition(){
        return position;
    }

    public int getWidth(){
        return width;
    }

}

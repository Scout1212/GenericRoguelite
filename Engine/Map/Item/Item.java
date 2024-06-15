//todo special items: "purple sprite" - inverses controls, "teleport" - teleports in the general direction of mouse
package Engine.Map.Item;


import Engine.Parents.Player.Player;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;

public class Item{
    private int x, y;
    static private int width = 50;
    static private int height = 50;
    private int heal, dmg, shotCD, stamina, type, price;
    private String name;
    private boolean playSound;
    private double speed, range;
    private AudioClip buy, reject;
    private Image asset;
    public Item(int x, int y,boolean random, boolean debuff){

        playSound = true;
        type = (int)(Math.random()*11 + 1);
        heal = 0;
        dmg = 0;
        shotCD = 0;
        stamina = 0;
        price = (int)(Math.random() *10 + 10);
        name = "unassigned";
        //black is unassigned
        asset = null;
        buy = Applet.newAudioClip(getClass().getResource("Sound/CashRegisterPurchase.wav"));
        reject = Applet.newAudioClip(getClass().getResource("Sound/Broke.wav"));
        
        
        this.x = x - width/2;
        this.y = y + height / 2;

        //todo randomize the stats for each one with math.random
        //todo add subtext
        if(!random || type == 1 || (type == 5 && !debuff)){
            asset = new ImageIcon(getClass().getResource("Image/Steak.png")).getImage();
            name = "Steak";
            price = 5;
            heal = (int)(Math.random()*30 + 20);
        }
        else if(type == 2 || (type == 6 && !debuff)){
            //dmg up
            asset = new ImageIcon(getClass().getResource("Image/Arm.png")).getImage();
            name = "Bongo's sarms";
            dmg = 1;
        }
        else if(type == 3 || (type == 7 && !debuff)){
            //speed up
            asset = new ImageIcon(getClass().getResource("Image/Jordans.png")).getImage();
            name = "Mocha Reps";
            speed += .5;
        }
        else if(type == 4 || (type == 8 && !debuff)){
            //stam up
            asset = new ImageIcon(getClass().getResource("Image/Coke.png")).getImage();
            name = "Stamina Drink";
            stamina = 50;
        }
        else if(type == 5 || (type == 9 && !debuff)){
            name = "Switch";
            asset = new ImageIcon(getClass().getResource("Image/Pistol.png")).getImage();
            shotCD -= 10;
        }
        else if(type == 6 || (type == 11 && !debuff)){
            name = "Awp Scope";
            asset = new ImageIcon(getClass().getResource("Image/Range.jpg")).getImage();
            range = .1;
        }
        else if(type == 7){
            //dmg down
            name = "Bongo arm";
            dmg -= 1;
        }
        else if(type == 8){
            //speed down
            name = "Lead Boots";
            speed -= .5;
        }
        else if(type == 9){
            //stamina down
            name = "No cardio";
            stamina -= 25;
        }
        else if(type == 10){
            //shot cd down
            name = "Sluggish Arm";
            shotCD += 10;
        }
        else if(type == 11){
            //range down
            name = "Bongo's Crippled Scope";
            range = -.1;
        }

        if(debuff || asset == null){
            asset = new ImageIcon(getClass().getResource("Image/Question.png")).getImage();
        }
    }

    public void drawSelf(Graphics g){
        if(asset != null)
            g.drawImage(asset, x, y, width, height, null);

        g.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 20));
        g.setColor(Color.black);
        g.drawString("Chips: " + price, (x + width/2) - g.getFontMetrics().stringWidth("Chips:" + price)/2 ,y - 10) ;
    }

    public boolean touched(Player p){
            if(x < p.getX() + p.getWidth() && x + width > p.getX() && y < p.getY() + p.getHeight() && y + height > p.getY()) {
                if(p.getChip() >= price) {
                    buy.play();
                    p.decreaseChip(price);
                    p.addItem(this);
                    return true;
                }
                else{
                    if(playSound) {
                        reject.play();
                        playSound = false;
                    }
                }
                //todo this is spamming replay :(
            }
            else playSound = true;
        return false;
    }

    //heal, dmg, shotCD, stamina, type, price;
    public int getDmg() {
        return dmg;
    }

    public int getHeal(){
        return heal;
    }

    public int getStamina(){
        return stamina;
    }

    public static int getHeight(){
        return height;
    }

    public double getSpeed(){
        return speed;
    }

    public int getShotCd(){
        return shotCD;
    }
    public double getRange(){
        return range;
    }

    public String getName(){
        return name;
    }

    public double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
package Engine.Parents.Player;

import Engine.Map.Item.Item;
import Engine.Parents.Enemy.Enemy;
import Engine.Parents.Enemy.EnemyProjectile;

import javax.swing.*;
import java.awt.*;
import java.applet.*;


public class Player{
    private int width, height, direction;
    private double x, y, vx, vy, maxVelo;
    private int hp, maxHp, dmg, safeCircle;
    private double projSpdMult, projResistance;
    //direction 1 = up, 2 = right, 3 = down, 4 = left
    private int chips;
    private int projSpeed;
    private int shootingTick, shootingCooldown, dmgTick, dmgCooldown, displayOpacity;
    private double spdMult, stamina, maxStamina;
    private boolean sprinting, showCoin;

    //todo why isnt it working
    private AudioClip moving, shotSound;
    private Image walkRight, walkDown, walkLeft, walkUp, idle;
    private boolean startMovingSound;


    public Player(int leftBound, int rightBound, int topBound, int bottomBound){
        x = (leftBound+(rightBound - leftBound)/2);
        y = (topBound+(bottomBound - topBound)/2);
        vx = 0;
        vy = 0;
        this.width = 50;
        this.height = (int)(width * 1.5);
        direction = 2;

        moving = Applet.newAudioClip(getClass().getResource("PlayerAudio/WalkPlayer.wav"));
        shotSound = Applet.newAudioClip(getClass().getResource("PlayerAudio/Portal.wav"));

        walkRight = new ImageIcon(getClass().getResource("PlayerImages/PlayerWalkRight.gif")).getImage();
        walkDown = new ImageIcon(getClass().getResource("PlayerImages/PlayerWalkDown.gif")).getImage();
        walkUp =  new ImageIcon(getClass().getResource("PlayerImages/PlayerWalkUp.gif")).getImage();
        walkLeft = new ImageIcon(getClass().getResource("PlayerImages/PlayerWalkLeft.gif")).getImage();
        idle = new ImageIcon(getClass().getResource("PlayerImages/PlayerIdle.gif")).getImage();


        startMovingSound = true;

        chips = 100;
        
        projSpeed = 20;
        shootingTick = -1;
        shootingCooldown = 15;
        
        dmgTick = -1;
        dmgCooldown = 2;
        hp = 100;
        maxHp = 100;
        
        projSpdMult = 1;
        projResistance = .6;

        maxVelo = 5;
        
        //change this value
        this.dmg = 2;
        this.safeCircle = 100;

        stamina = 100;
        maxStamina = 100;

        sprinting = false;

        spdMult = 1;
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


        drawStaminaBar(g);
        drawInv(g);
    }

    public void drawStaminaBar(Graphics g){
        if(sprinting || stamina != maxStamina){
            int barWidth = width/2;
            int barHeight = 10;
            double percent = stamina/maxStamina;

            g.setColor(new Color(255, 0, 0, displayOpacity));
            g.fillRect((int)(x + width/4), (int)y - barHeight*2, barWidth,barHeight);

            g.setColor(new Color(0, 0, 255, displayOpacity));
            g.fillRect((int)(x + width/4), (int)(y - barHeight*2), (int)(barWidth * percent),barHeight);

            //todo displayOpacity is exceeding the 255 alpha channel and its breaking sometimes

            if(displayOpacity < 255 && sprinting)
                displayOpacity += 5;
            else if(displayOpacity -15 > 0 && maxStamina *.8 < stamina)
                displayOpacity -= 15;
        }
        else
            displayOpacity = 0;
    }

    public void drawInv(Graphics g){
        if(showCoin){
            //todo add transition

            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 15));

            String text = "chips: "+chips;


            //shootingCooldown, maxvelo, maxStam, dmg
            g.drawString(text, (int)(x + width/2) - g.getFontMetrics().stringWidth(text)/2 ,(int)y + height + g.getFontMetrics().getHeight());
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("dmg: " + dmg, (int)(x + width/2) - g.getFontMetrics().stringWidth("dmg: " + dmg)/2 ,(int)y + height + g.getFontMetrics().getHeight()*2);
            g.drawString("MaxSpd: " + maxVelo, (int)(x + width/2) - g.getFontMetrics().stringWidth("MaxSpd: " + maxVelo)/2 ,(int)y + height + g.getFontMetrics().getHeight()*3);
            g.drawString("MaxStam: " + maxStamina, (int)(x + width/2) - g.getFontMetrics().stringWidth("MaxStam: " + maxStamina)/2 ,(int)y + height + g.getFontMetrics().getHeight()*4);
            g.drawString("SpdMult: " + spdMult, (int)(x + width/2) - g.getFontMetrics().stringWidth("SpdMult: " + spdMult)/2 ,(int)y + height + g.getFontMetrics().getHeight()*5);

        }
    }

    public void sprint(){
        int staminaLoss = 1;

        if(stamina > 50){
            spdMult = 1.75;
            stamina -= staminaLoss;
            sprinting = true;
        }
        else if(stamina > 25){
            spdMult = 1.25;
            stamina -= staminaLoss;
            sprinting = true;
        }
        else if(stamina > 0){
            spdMult = 1.1;
            stamina -= staminaLoss;
            sprinting = true;
        }
        else
            sprinting = false;
    }
    public void sprintRecharge(){
        spdMult = 1;
        if(stamina < maxStamina)
            stamina += 2;
        sprinting = false;
    }
    public PlayerProjectile shootUp(int tick){
        if(tick > shootingTick)
        {
            shotSound.play();
            PlayerProjectile output = new PlayerProjectile((int)x + width/2, (int)y + height/2, (int)vx, (int)vy-projSpeed, projSpdMult, projResistance, dmg);


            shootingTick = tick + shootingCooldown;

            return output;
        }
        else
            return null;
    }
    
    public PlayerProjectile shootRight(int tick){
        if(tick > shootingTick)
        {
            shotSound.play();
            PlayerProjectile output = new PlayerProjectile((int)x + width/2, (int)y + height/2, (int)vx + projSpeed, (int)vy, projSpdMult, projResistance, dmg);


            shootingTick = tick + shootingCooldown;

            return output;
        }
        else
            return null;
    }
    
    public PlayerProjectile shootDown(int tick){
        
        if(tick > shootingTick)
        {
            shotSound.play();
            PlayerProjectile output = new PlayerProjectile((int)x + width/2, (int)y + height/2, (int)vx, (int)vy + projSpeed, projSpdMult, projResistance, dmg);


            shootingTick = tick + shootingCooldown;

            return output;
        }
        else
            return null;
    }
    
    public PlayerProjectile shootLeft(int tick){
        
        if(tick > shootingTick)
        {
            shotSound.play();
            PlayerProjectile output = new PlayerProjectile((int)x + width/2, (int)y + height/2, (int)vx - projSpeed, (int)vy, projSpdMult, projResistance, dmg);


            shootingTick = tick + shootingCooldown;

            return output;
        }
        else
            return null;
    }
    
    public boolean projCollision(EnemyProjectile e){
        for (int i = (int)x; i < x + width; i++) {
            boolean topSide = distance(i, (int) y, e.getCenterX(), e.getCenterY()) < (double) e.getDiam() / 2;
            boolean bottomSide = distance(i, (int) y + height, e.getCenterX(), e.getCenterY()) < (double) e.getDiam() / 2;
            if (topSide || bottomSide) {
                if (hp > 0) {
                    hp -= e.getDmg();
                }
                return true;
            }

        }

        for (int i = (int) y; i < y + height; i++) {
            boolean leftSide = distance((int) x, i, e.getCenterX(), e.getCenterY()) < (double) e.getDiam() / 2;
            boolean rightSide = distance((int) x + width, i, e.getCenterX(), e.getCenterY()) < (double) e.getDiam() / 2;
            if (leftSide || rightSide) {
                if (hp > 0) {
                    hp -= e.getDmg();
                }
                return true;
            }
        }

        return false;
    }
    
    public void EnemyCollision(Enemy e, int tick, int leftBound, int rightBound, int topBound, int bottomBound){
        if(x < e.getX() + e.getWidth() && x + width > e.getX() && y < e.getY() + e.getHeight() && y + height > e.getY()){
            //remove apply velos
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
            //fix this so its based off the total hp of the character
            if(tick > dmgTick){
                dmgTick = tick + dmgCooldown;

                //colliding dmg
                hp--;
            }
        }
    }

    public void addItem(Item i){
        //heal, dmg, shotCD, stamina, type, price;
        if(hp + i.getHeal()> 100){
            hp = 100;
        }
        else
            hp += i.getHeal();

        maxVelo += i.getSpeed();

        dmg += i.getDmg();

        projResistance += -1 * i.getRange();
        //do cooldown conversions
        dmgCooldown += i.getShotCd();

        maxStamina += i.getStamina();
    }


    
    public void applyVelo(){
        //todo make it sound quieter
        if((vy != 0 || vx != 0) && startMovingSound){
            moving.loop();
            startMovingSound = false;
        }
        else if(vy == 0 && vx == 0){
            moving.stop();
            startMovingSound = true;
        }

        x += vx*spdMult;
        y += vy*spdMult;
    }
    
    public void resetXVelo(){
        vx = 0;
    }
    public void resetYVelo(){
        vy = 0;
    }
    
    public void accelRight(int rightBound){
        direction = 2;
        if(vx < maxVelo)
            vx += .5;

        int nextX = (int)(x + vx);
        if(nextX + width > rightBound) {
            resetXVelo();
        }
    }
    public void accelLeft(int leftBound){
        direction = 4;
        if(vx > -1* maxVelo)
            vx -= .5;

        int nextX = (int)(x+vx);
        if(nextX < leftBound) {
            resetXVelo();
        }
    }
    public void accelUp(int topBound){
        direction = 1;
        if(vy > -1 * maxVelo)
            vy-= .5;

        //next Y had a freaking += im angry "nextY = y += vy";
        int nextY = (int)(y + vy);
        if(nextY < topBound) {
            resetYVelo();
        }
    }
    public void accelDown(int bottomBound){
        direction = 3;
        if(vy < maxVelo)
            vy+= .5;
        //next Y had a freaking += im angry "nextY = y += vy";
        int nextY = (int)(y + vy);
        if(nextY + height > bottomBound) {
            resetYVelo();
        }
    }
    public void decreaseChip(int c){
        chips -= c;
    }
    public void setDirection(int i){
        direction = i;
    }
    public void setX(int a){
        x = a;
    }
    public void setY(int a){
        y = a;
    }

    public void takeDmg(int d){
        hp -= d;
    }
    
    public int getX(){
        return (int)x;
    }
    
    public int getHp(){
        return hp;
    }
    
    public int getCenterX(){
        return (int)(x*2 + width)/2;
    }
    public int getCenterY(){
        //i accidentally had an x here and the whole thing broke >:(
        return (int)(y*2 + height)/2;
    }
    
    public int getSafeCircle(){
        return safeCircle;
    }
    
    public int getY(){
        return (int)y;
    }
    public String toString()
    {
        return "players speed " + vx + " " + vy;
    }

    public int getMaxHp(){
        return maxHp;
    }

    public int getWidth(){
        return width;
    }


    public int getShootingCooldown(){
        return shootingCooldown;
    }
    public int getDmg(){
        return dmg;
    }
    public double getSpdMult(){
        return spdMult;
    }
    public double getMaxVelo(){
        return maxVelo;
    }
    public double getResistance(){
        return projResistance;
    }

    public double getMaxStamina(){
        return maxStamina;
    }

    public int getChip(){
        return chips;
    }
    public int getHeight(){
        return height;
    }
    public double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public boolean isAlive(){
        return hp > 0;
    }

    public void reset(){
        hp = 100;
    }

    public void increaseChips(int a){
        chips += a;
    }

    public void showInv(){
        showCoin = true;
    }

    public void hideInv(){
        showCoin = false;
    }

}

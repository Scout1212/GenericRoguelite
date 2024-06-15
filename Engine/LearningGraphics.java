package Engine;

import Engine.Map.Door.Door;
import Engine.Map.Item.Item;
import Engine.Map.Trap.Trap;
import Engine.Parents.Enemy.Enemy;
import Engine.Parents.Npc.Npc;
import Engine.Parents.Player.Player;
import Engine.Parents.Enemy.EnemyProjectile;
import Engine.Parents.Player.PlayerProjectile;

import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import java.util.ArrayList;

public class LearningGraphics extends JComponent implements KeyListener, MouseListener, MouseMotionListener
{
    //instance variables

    //todo fix enemy spawning
    private int WIDTH;
    private int HEIGHT;
    private Player player;
    private Npc bongo;
    private int tick;
    private int screenNumber, horiSize, vertSize;
    private int mainRoom, shop, hallway;
    //0 is the first room, 1 is hallway, 2 is shop,
    private int roomNumber, resetCount;
    private int leftBound, rightBound, topBound, bottomBound;
    private boolean firstEnter, refreshShop, touchedItem, moveTrap;
    private Door door, shopDoor;
    private String bannerText;
    //0 is the main game, 1 is the start screen, 3 will be an ending screen, 4 is the shopscreen
    private ArrayList<String> keys = new ArrayList<String>();
    private ArrayList<PlayerProjectile> pProjs = new ArrayList<PlayerProjectile>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<EnemyProjectile> eProjs =  new ArrayList<EnemyProjectile>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Trap> traps = new ArrayList<Trap>();
    private AudioClip BGMusic;
    private boolean isSetBGMusic;
    private int bannerSteps = 0;
    private int defaultVert = 100;
    private int defaultHori = 100;

    //Default Constructor
    public LearningGraphics(){
        //initializing instance variables
        WIDTH = 1400;
        HEIGHT = 750;
        setBGMusic("HalloweenChiptune");
        isSetBGMusic = false;


        bannerText = "default";
        borderSetter(defaultVert, defaultHori);
        player = new Player(leftBound, rightBound, topBound, bottomBound);
        door = new Door(leftBound, rightBound, topBound, bottomBound, false);
        shopDoor = new Door(leftBound, rightBound, topBound, bottomBound, true);
        shopDoor.setOnLeftWall(leftBound, topBound, bottomBound);
        bongo = new Npc(leftBound, rightBound, topBound, bottomBound);
        tick = 0;

        screenNumber = 1;
        roomNumber = 0;
        firstEnter = false;
        refreshShop = true;

        createTraps(5);

        shop = 2;
        mainRoom = 0;
        hallway = 1;



        //Setting up the GUI

        JFrame gui = new JFrame(); //This makes the gui box
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Makes sure program can close
        gui.setTitle("Learning Graphics"); //This is the title of the game, you can change it
        gui.setPreferredSize(new Dimension(WIDTH + 5, HEIGHT + 30)); //Setting the size for gui
        gui.setResizable(false); //Makes it so the gui cant be resized
        gui.getContentPane().add(this); //Adding this class to the gui

         /*If after you finish everything, you can declare your buttons or other things
          *at this spot. AFTER gui.getContentPane().add(this) and BEFORE gui.pack();
          */

        gui.pack(); //Packs everything together
        gui.setLocationRelativeTo(null); //Makes so the gui opens in the center of screen
        gui.setVisible(true); //Makes the gui visible
        gui.addKeyListener(this);//stating that this object will listen to the keyboard
        gui.addMouseListener(this); //stating that this object will listen to the Mouse
        gui.addMouseMotionListener(this); //stating that this object will acknowledge when the Mouse moves
    }

    public void reactKeys(){
        if(!keys.isEmpty()){
                if(keys.contains("w"))
                    player.accelUp(topBound);
                else if(keys.contains("s"))
                    player.accelDown(bottomBound);
                else
                    player.resetYVelo();

                if(keys.contains("a"))
                    player.accelLeft(leftBound);
                else if(keys.contains("d"))
                    player.accelRight(rightBound);
                else
                    player.resetXVelo();
                if(keys.contains("i") && roomNumber != shop){
                    player.setDirection(1);
                    PlayerProjectile p = player.shootUp(tick);
                    if(p != null)
                        pProjs.add(p);
                }
                else if(keys.contains("l") && roomNumber != shop){
                    player.setDirection(2);
                    PlayerProjectile p = player.shootRight(tick);
                    if(p != null)
                        pProjs.add(p);
                }
                else if(keys.contains("k") && roomNumber != shop){
                    player.setDirection(3);
                    PlayerProjectile p = player.shootDown(tick);
                    if(p != null)
                        pProjs.add(p);
                }
                else if(keys.contains("j") && roomNumber != shop){
                    player.setDirection(4);
                    PlayerProjectile p = player.shootLeft(tick);
                    if(p != null)
                        pProjs.add(p);
                }

                if(keys.contains("shift")){
                    player.sprint();
                }
                else
                    player.sprintRecharge();


        }
        else{
            player.setDirection(0);
            player.resetXVelo();
            player.resetYVelo();
            player.sprintRecharge();
        }
    }
    //This method will acknowledge user input
    public void keyPressed(KeyEvent e)
    {
        if(screenNumber == 0){
            if(e.getKeyCode() == KeyEvent.VK_A){
                if(!keys.contains("a"))
                    keys.add("a");
            }
            else if(e.getKeyCode() == KeyEvent.VK_S){
                if(!keys.contains("s"))
                    keys.add("s");
            }
            else if(e.getKeyCode() == KeyEvent.VK_W){
                if(!keys.contains("w"))
                    keys.add("w");
            }
            else if(e.getKeyCode() == KeyEvent.VK_D){
                if(!keys.contains("d"))
                    keys.add("d");
            }
            else if(e.getKeyCode() == KeyEvent.VK_J){
                if(!keys.contains("j"))
                    keys.add("j");
            }
            else if(e.getKeyCode() == KeyEvent.VK_K){
                if(!keys.contains("k"))
                    keys.add("k");
            }
            else if(e.getKeyCode() == KeyEvent.VK_L){
                if(!keys.contains("l"))
                    keys.add("l");
            }
            else if(e.getKeyCode() == KeyEvent.VK_I){
                if(!keys.contains("i"))
                    keys.add("i");
            }
            else if(e.getKeyCode() == KeyEvent.VK_SHIFT){
                if(!keys.contains("shift"))
                    keys.add("shift");
            }
            else if(e.getKeyCode() == KeyEvent.VK_E){
                if(!keys.contains("e"))
                    keys.add("e");
            }
            else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                screenNumber = 3;
                isSetBGMusic = false;
            }
        }
        else if(screenNumber == 1){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                screenNumber = 0;
                isSetBGMusic = false;
            }
        }
        else if(screenNumber == 2){
            //the game over screen
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                screenNumber = 0;
                isSetBGMusic = false;
                reset();
            }
        }
        else if(screenNumber == 3){
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                screenNumber = 0;
                isSetBGMusic = false;
            }
        }
    }

    //All your UI drawing goes in here
    public void paintComponent(Graphics g)
    {

        //todo how to manage the layers in drawing
        if(screenNumber == mainRoom){

            drawWalls(g);
            drawLists(g);
            if(roomNumber == mainRoom || roomNumber == hallway) {
                door.drawSelf(g);
            }
            if(roomNumber == mainRoom || roomNumber == shop)
                shopDoor.drawSelf(g);

            drawBar(g);

            player.drawSelf(g);

            if(roomNumber == shop) {
                bongo.drawSelf(g,touchedItem, bannerText);
                drawBanner(g);
                drawStats(g);
            }

            if(resetCount == 0 && roomNumber == mainRoom) {
                drawKeys(g);
            }

        }
        else if(screenNumber == 1){
            drawText(g, "Play", 550);
        }
        else if(screenNumber == 2){
            drawText(g, "You died", 550);
            drawText(g, "You survived: " + resetCount + " rooms", 600 + g.getFontMetrics().getHeight());
        }
        else if(screenNumber == 3){
            drawText(g, "Paused", 550);
        }

    }

    public void loop()
    {
        if(screenNumber == 0){
            reactKeys();
            player.applyVelo();
            enemyAct();
            if(roomNumber != shop)
                trapAct();

            if(roomNumber == shop) {
                bongo.act(player, leftBound, rightBound, topBound, bottomBound);
                itemAct();
            }

            checkProjCollision();
            checkEnemiesAlive();

            checkEProjCollision();
            checkEnemiesCollision();
            checkEnemiesPlayerCollision();

            roomStatus();

            checkPlayerDeath();
            changeRoom();

            tick++;
        }
        else if(screenNumber == 1){
            reactKeys();
        }
        else if (screenNumber == 3){
            //doesnt need react keys
        }

        manageBackgroundMusic();
        repaint();
    }
    //These methods are required by the compiler.
    //You might write code in these methods depending on your goal.
    public void keyTyped(KeyEvent e)
    {
    }
    public void keyReleased(KeyEvent e)
    {
        if(screenNumber == 0){
            if(e.getKeyCode() == KeyEvent.VK_A)
                keys.remove("a");
            else if(e.getKeyCode() == KeyEvent.VK_S)
                keys.remove("s");
            else if(e.getKeyCode() == KeyEvent.VK_D)
                keys.remove("d");
            else if(e.getKeyCode() == KeyEvent.VK_W)
                keys.remove("w");
            else if(e.getKeyCode() == KeyEvent.VK_J)
                keys.remove("j");
            else if(e.getKeyCode() == KeyEvent.VK_K)
                keys.remove("k");
            else if(e.getKeyCode() == KeyEvent.VK_L)
                keys.remove("l");
            else if(e.getKeyCode() == KeyEvent.VK_I)
                keys.remove("i");
            else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
                keys.remove("shift");
            else if(e.getKeyCode() == KeyEvent.VK_E)
                keys.remove("e");
        }
    }
    public void mousePressed(MouseEvent e)
    {
    }
    public void mouseReleased(MouseEvent e)
    {
    }
    public void mouseClicked(MouseEvent e)
    {
    }
    public void mouseEntered(MouseEvent e)
    {
    }
    public void mouseExited(MouseEvent e)
    {
    }
    public void mouseMoved(MouseEvent e)
    {
    }
    public void mouseDragged(MouseEvent e)
    {
    }
    public void start(final int ticks){
        Thread gameThread = new Thread(){
            public void run(){
                while(true){
                    loop();
                    try{
                        Thread.sleep(1000 / ticks);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        gameThread.start();
    }

    //this calls the act for the player projectiles and sees if the projectile should be deleted
    public void checkProjCollision(){
        if(!pProjs.isEmpty()){
            for(int i =0; i < pProjs.size(); i++){
                //turn the enemies into a list maybe.
                PlayerProjectile curProj = pProjs.get(i);

                boolean deleteProj;
                boolean hitEnemy = checkEnemiesProj(curProj);
                //check each enemy collision against the projectiles

                deleteProj = hitEnemy || curProj.act(leftBound, rightBound, topBound, bottomBound);

                if(deleteProj){
                    pProjs.remove(i);
                    i--;
                }
            }
        }
    }

    //checks a player projectile against each enemy to see if it collided
    public boolean checkEnemiesProj(PlayerProjectile curProj){
        boolean output = false;
        if(!enemies.isEmpty()){
                for(int a = 0; a < enemies.size(); a++){

                    Enemy curEnemy = enemies.get(a);

                    if(curEnemy.projCollision(curProj))
                        return true;
                }
            }
        return output;
    }



    //calls the act for enemy projectiles and sees if they should be deleted
    public void checkEProjCollision(){
        if(!eProjs.isEmpty()){
            for(int i = 0; i < eProjs.size(); i++){

                EnemyProjectile curProj = eProjs.get(i);

                boolean deleteProj = false;
                boolean hitPlayer = player.projCollision(curProj);

                deleteProj = hitPlayer || curProj.act(leftBound, rightBound, topBound, bottomBound);

                if(deleteProj){
                    //todo I could maybe add the poof when its done
                    eProjs.remove(i);
                    i--;
                }
            }
        }
    }

    //checks if the enemies are colliding with eachother, so they dont overlap
    public void checkEnemiesCollision(){
        for(int i = 0; i < enemies.size() -1; i++){
            for(int b = i + 1; b < enemies.size(); b++){
                Enemy e1 = enemies.get(i);
                Enemy e2 = enemies.get(b);
                e1.EnemyCollision(e2, leftBound, rightBound, topBound, bottomBound);
            }
        }
    }

    public void checkEnemiesPlayerCollision(){
            for(int e = 0; e < enemies.size(); e++){
                player.EnemyCollision(enemies.get(e), tick, leftBound, rightBound, topBound, bottomBound);
            }

    }

    //checks if they are alive so they will can get deleted when they die
    public void checkEnemiesAlive(){
        for(int i = 0; i < enemies.size(); i++){
            if(!enemies.get(i).isAlive()){
                player.increaseChips(2);
                enemies.remove(i);
                i--;
            }
        }
    }

    //calls act on each enemy and helps them shoot projectiles
    public void enemyAct(){
        for(int i = 0; i < enemies.size(); i++){
            Enemy curEnemy = enemies.get(i);
            curEnemy.act(player.getCenterX(), player.getCenterY(), leftBound, rightBound, topBound, bottomBound, player.getSafeCircle());

            EnemyProjectile temp = curEnemy.shoot(tick, player.getCenterX(), player.getCenterY());

            if(temp != null)
                eProjs.add(temp);
        }
    }

    //allows to create multiple enemies quickly
    public void createEnemies(int a){

        //todo alter the enemies added based on the reset count so add a dificulty input to enemies class
        //maybe also increase the amount of enemies based on reset count --> goal is to get as far as you can
        for(int i = 0; i < a; i++){
            enemies.add(new Enemy(leftBound, rightBound, topBound, bottomBound, resetCount));
        }
    }

    public void createTraps(int a){
        for(int i = 0; i < a; i++){
            traps.add(new Trap(leftBound, rightBound, topBound, bottomBound));
        }
    }

    public void checkPlayerDeath(){
        if(!player.isAlive()){
            screenNumber = 2;
        }
    }

    public void reset(){
        enemies.clear();
        keys.clear();
        pProjs.clear();
        eProjs.clear();
        player.reset();
        items.clear();
        addItem(3);
        resetCount = 0;
        roomNumber = mainRoom;
    }

    public void drawKeys(Graphics g){
        g.setColor(Color.gray);

        Image w = new ImageIcon(getClass().getResource("Images/Keys/WKey.png")).getImage();
        Image a = new ImageIcon(getClass().getResource("Images/Keys/AKey.png")).getImage();
        Image s = new ImageIcon(getClass().getResource("Images/Keys/SKey.png")).getImage();
        Image d = new ImageIcon(getClass().getResource("Images/Keys/DKey.png")).getImage();
        Image i = new ImageIcon(getClass().getResource("Images/Keys/IKey.png")).getImage();
        Image j = new ImageIcon(getClass().getResource("Images/Keys/JKey.png")).getImage();
        Image k = new ImageIcon(getClass().getResource("Images/Keys/KKey.png")).getImage();
        Image l = new ImageIcon(getClass().getResource("Images/Keys/LKey.png")).getImage();
        Image shift = new ImageIcon(getClass().getResource("Images/Keys/Shift.png")).getImage();
        //Image shift = new ImageIcon(getClass().getResource("ShiftKey.png")).getImage();
        int xMiddle = (rightBound - leftBound)/2 + leftBound;
        int yMiddle = (bottomBound - topBound)/3 + topBound;


        int leftXMiddle = xMiddle - 200;
        int rightXMiddle =  xMiddle + 200;



        g.drawImage(w, leftXMiddle, yMiddle, 50, 50, null);
        g.drawImage(s, leftXMiddle, yMiddle + 50, 50, 50, null);
        g.drawImage(a, leftXMiddle - 50, yMiddle + 50, 50, 50, null);
        g.drawImage(d, leftXMiddle + 50, yMiddle + 50, 50, 50, null);

        g.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 30));
        g.drawString("Movement", leftXMiddle - g.getFontMetrics().stringWidth("Movement")/2, yMiddle + 100 + g.getFontMetrics().getHeight());

        g.drawImage(i, rightXMiddle, yMiddle, 50, 50, null);
        g.drawImage(j, rightXMiddle, yMiddle + 50, 50, 50, null);
        g.drawImage(k, rightXMiddle - 50, yMiddle + 50, 50, 50, null);
        g.drawImage(l, rightXMiddle + 50, yMiddle + 50, 50, 50, null);
        g.drawString("Shooting", rightXMiddle - g.getFontMetrics().stringWidth("Shooting")/2, yMiddle + 100 + g.getFontMetrics().getHeight());

        g.drawImage(shift, xMiddle - 25, yMiddle + 300, 50, 50, null);
        g.drawString("Shift:Sprint", xMiddle - g.getFontMetrics().stringWidth("Shift:Sprint")/2, yMiddle + 300);

        g.drawString("Black Square -> Take Dmg", xMiddle - g.getFontMetrics().stringWidth("Black Square -> Dmg")/2, yMiddle - 50);


    }


    public void drawLists(Graphics g){
        if(roomNumber != shop) {
            if (!traps.isEmpty()) {
                for (int i = 0; i < traps.size(); i++) {
                    traps.get(i).drawSelf(g);
                }
            }
        }
        if(!enemies.isEmpty()){
            for(int i = 0; i < enemies.size(); i++){
                enemies.get(i).drawSelf(g);
            }
        }
        if(!pProjs.isEmpty()){
            for(int i = 0; i < pProjs.size(); i++){
                pProjs.get(i).drawSelf(g);
            }
        }
        if(!eProjs.isEmpty()){
            for(int i = 0; i < eProjs.size(); i++){
                eProjs.get(i).drawSelf(g);
            }
        }
        if(roomNumber == shop){
            if(!items.isEmpty()){
                for(int i = 0; i < items.size(); i++) {
                    items.get(i).drawSelf(g);
                }
            }
        }
    }

    public void trapAct(){
        for(int i = 0; i < traps.size(); i++){
            traps.get(i).collision(player, tick);
            if(moveTrap)
                traps.get(i).randomPosition(leftBound, rightBound, topBound, bottomBound);

            moveTrap = false;

        }
    }

    public void drawWalls(Graphics g){
        g.setColor(Color.black);

        g.fillRect(0,0, leftBound, HEIGHT);
        g.fillRect(0,0, WIDTH, topBound);
        g.fillRect(rightBound,0, horiSize, HEIGHT);

        g.fillRect(0,bottomBound,WIDTH, vertSize);
    }

    public void drawStats(Graphics g){
        g.setColor(Color.black);
        int spacer = g.getFontMetrics().getHeight() + 10;
        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString("Max Stamina: "+(int)player.getMaxStamina(), leftBound, bottomBound - spacer);
        g.drawString("Dmg: " + player.getDmg(), leftBound, bottomBound - spacer*2);
        g.drawString("Max Speed: "+player.getMaxVelo(), leftBound, bottomBound - spacer*3);
        g.drawString("Bullet Range: " + player.getResistance(), leftBound, bottomBound - spacer*4);
        g.drawString("Shot Cooldown: " + player.getShootingCooldown(), leftBound, bottomBound - spacer*5);
    }


    public void drawBar(Graphics g){
        double pPercentHp = (double)player.getHp()/player.getMaxHp();
        g.setColor(Color.blue);
        g.fillRect(horiSize/4, HEIGHT/4, 50, HEIGHT/2);
        g.setColor(Color.red);

        g.fillRect(horiSize/4, HEIGHT/4, 50, (int)((double)HEIGHT/2 * pPercentHp));
    }

    public void drawText(Graphics g, String t, int y){
        g.setColor(Color.BLUE);
        int positionX = (int)(((rightBound - leftBound)/2 + leftBound) - g.getFontMetrics().stringWidth(t)/2);
        int positionY = y;
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString(t, positionX, positionY);

        g.setColor(Color.red);
        g.fillRect(positionX, positionY, g.getFontMetrics().stringWidth(t), g.getFontMetrics().getHeight());
    }

    public void drawBanner(Graphics g){
        if(touchedItem){
            bannerSteps = 100;
        }

        if(bannerSteps != 0) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(bannerText, (rightBound - leftBound) / 2 + leftBound -g.getFontMetrics().stringWidth(bannerText)/2, topBound + g.getFontMetrics().getHeight());
            bannerSteps--;
        }
    }


    public void borderSetter(int vert, int hori){
        horiSize = hori;
        vertSize = vert;
        leftBound = hori;
        rightBound = WIDTH - hori;
        topBound = vert;
        bottomBound = HEIGHT - vert;
    }

    public void roomStatus(){
        if(enemies.isEmpty()){
            shopDoor.setOpen(true);
            door.setOpen(true);
        }
        else{
            door.setOpen(false);
            shopDoor.setOpen(false);
        }
    }

    public void changeRoom(){
        //figuring out which door it touched, 0 is none, 1 is hallway, 2 is shop
        int changeRoom = 0;
        firstEnter = false;
        if(roomNumber == hallway){
            shopDoor.setOpen(false);
        }


        if(door.playerCollision(player))
            changeRoom = 1;
        else if(shopDoor.playerCollision(player))
            changeRoom = 2;


        //this handles the transitions and roomNumber updating
        if(changeRoom == 1) {
            if(roomNumber == hallway){
                resetCount++;
                refreshShop = true;
                moveTrap = true;

                createEnemies(resetCount/2 + 3);
                roomNumber = mainRoom;
                shopDoor.setOnLeftWall(leftBound, topBound, bottomBound);
                isSetBGMusic = true;
            }
            else if(roomNumber == mainRoom){
                //todo if i want to change the hallway music i do that here
                roomNumber = hallway;
                isSetBGMusic = true;
            }

            firstEnter = true;
        }
        else if(changeRoom == 2){
            if(roomNumber == 0)
                roomNumber = 2;
            else if(roomNumber == 2) {
                roomNumber = 0;
                borderSetter(defaultVert, defaultHori);
                player.setX(leftBound + shopDoor.getWidth());
            }

            firstEnter = true;
            isSetBGMusic = false;
        }

        //this handles the room logic: updates and calls when first entering a room
        if(roomNumber == mainRoom && firstEnter){
            //main
            borderSetter(defaultVert, defaultHori);
            door.randomPosition(leftBound, rightBound, topBound, bottomBound);
            shopDoor.setOnLeftWall(leftBound, topBound, bottomBound);
            firstEnter = false;
            player.hideInv();
        }
        else if(roomNumber == hallway && firstEnter){
            createHallway(door.getPosition());
            firstEnter = false;
            player.hideInv();
        }
        else if(roomNumber == shop && firstEnter){
            //shop
            if(refreshShop) {
                items.clear();
                addItem(3);
            }
            borderSetter(defaultVert * 2, defaultHori * 2);
            shopDoor.setOnRightWall(rightBound, topBound, bottomBound);
            player.setX(rightBound - shopDoor.getWidth() - player.getWidth() - 10);
            player.setY(topBound + (bottomBound - topBound) / 2 - player.getHeight() / 2);
            player.showInv();
            shopDoor.setOpen(true);
            eProjs.clear();
            pProjs.clear();
            firstEnter = false;
        }
    }

    public void createHallway(int position){
        int offsetPlayerWall = 10;
        if(position == 1){
            borderSetter((int)(HEIGHT * .33333333), (int)(WIDTH*.069444444));
            door.setOnRightWall(rightBound, topBound, bottomBound);
            if(firstEnter) {
                player.setX(leftBound + offsetPlayerWall);
                player.setY(topBound + (bottomBound - topBound) / 2 - player.getHeight() / 2);
                firstEnter = false;
            }
        }
        else if(position == 0){
            //upward narrow room
            borderSetter((int)(HEIGHT * .069444444), (int)(WIDTH *.4));
            door.setOnTopWall(leftBound, rightBound, topBound);

            if(firstEnter) {
                player.setX((leftBound + (rightBound - leftBound) / 2));
                player.setY(bottomBound - offsetPlayerWall - player.getHeight() - player.getWidth() / 2);
            }
        }
    }

    public void itemAct(){
        if(roomNumber == shop) {
            if (!items.isEmpty() || bannerSteps > 0) {
                for (int i = 0; i < items.size(); i++) {
                    Item temp = items.get(i);
                    touchedItem = temp.touched(player);
                    if(touchedItem){
                        bannerText = temp.getName();
                        items.remove(i);
                        i--;
                    }
                }
            }
        }
    }
    //adds 1 heal and rest is random
    public void addItem(int amount){

        double percent = (double)1/(amount+1);
        int y = (topBound+(bottomBound - topBound)/3) - Item.getHeight()/2;
        Item temp = new Item((int)((rightBound - leftBound)*percent) + leftBound, y, false, false);
        Item temp2 = new Item((int)((rightBound - leftBound)*(percent*2)) + leftBound, y, true, true);


        items.add(temp);
        items.add(temp2);


        for(int i = 1; i < amount - 1; i++) {
            int rw = rightBound - leftBound;

            int iX = leftBound + (int)(rw * (percent * (i + 2)));
            Item temp3 = new Item(iX, y, true, false);
            items.add(temp3);
        }
        refreshShop = false;
    }

    public void setBGMusic(String filename){
        AudioClip wantedSong = Applet.newAudioClip(getClass().getResource("Music/" + filename + ".wav"));

        //maybe
        if(BGMusic == null){
            BGMusic = wantedSong;
        }
        else if(!BGMusic.equals(wantedSong)){
            BGMusic.stop();
            BGMusic = wantedSong;
            BGMusic.loop();
        }
    }

    public void manageBackgroundMusic(){
        if(screenNumber != 0 && !isSetBGMusic){
            //todo menu music
            setBGMusic("HalloweenChiptune");
            isSetBGMusic = true;
        }
        else{
            if(roomNumber == shop && !isSetBGMusic) {
                setBGMusic("CloudChiptune");
                isSetBGMusic = true;
            }
            else if(roomNumber == mainRoom && !isSetBGMusic){
                setBGMusic("Dummy!");
                isSetBGMusic = true;
            }
        }
    }


    public static void main(String[] args){
        LearningGraphics g = new LearningGraphics();
        g.start(60);
    }
}


package Game.Start;

import Game.People.Hero;
import Game.gameObject.World;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


//游戏中心
public class GameClient extends Frame {
    //世界宽度
    public static final int WORLD_WIDTH = 960;
    //世界高度
    public static final int WORLD_HEIGHT = 720;
    private Image offScreenImage;
    private World world;

    //判断是否为双人模式是的话就添加一个world
    public GameClient(boolean Doubleplayer){
        this.world = new World(WORLD_WIDTH, WORLD_HEIGHT, Doubleplayer);
        offScreenImage = null;
    }

    //继承runnable接口设置线程
    private class PaintThread implements Runnable {
        public void run() {
            while(true) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //画出world 主角死亡就画出gameover
    public void paint(Graphics g) {
        if(!world.End())
            world.drawWorld(g);
        else {
            world.drawEnd(g);
        }
    }
    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(WORLD_WIDTH, WORLD_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.lightGray);
        gOffScreen.fillRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    //创建窗口
    public void lauchFrame() {
        this.setLocation(400, 100);
        this.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        this.setTitle("ZombieCrisis");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.lightGray);
        this.addKeyListener(new KeyMonitor((Hero) world.getObject(0)));
        if(world.getObject(1) instanceof Hero)
            this.addKeyListener(new KeyMonitor((Hero) world.getObject(1)));
        setVisible(true);
        new Thread(new PaintThread()).start();
    }

    //创建Hero的键盘监听器
    private class KeyMonitor extends KeyAdapter {
        Hero hero;
        public KeyMonitor(Hero hero){
            this.hero = hero;
        }
        public void keyReleased(KeyEvent e) {
            this.hero.keyReleased(e);
        }
        public void keyPressed(KeyEvent e) {
            this.hero.KeyPressed(e);
        }
    }
}

package Game.People;

import Game.WeaponChildren.Fireball;
import Game.Role.Direction;
import Game.Role.Role;
import Game.WeaponChildren.Sword;
import Game.WeaponChildren.SwordSkill;
import Game.WeaponFather.Weapon;
import Game.gameObject.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Hero extends Role {

    //计算英雄经验值
    public static int score;
    //定义英雄的血量
    public static final int MAX_HP = 1200;
    //存放键盘按键
    private int[] keys;
    //
    private boolean bL=false, bU=false, bR=false, bD=false;

    //定义英雄的属性
    public Hero(int x, int y, int keyGroup, World world) {
        super("Hero", MAX_HP,14, 5, x, y, world);
        addWeapon(new Sword(this, world));
        addWeapon(new Fireball(this, world));
        setCurrentWeapon(getWeapons().get(0));
        keys = new int[7];
        //用数组代表键盘按键
        if(keyGroup == 0){
            keys[0] = KeyEvent.VK_LEFT;
            keys[1] = KeyEvent.VK_UP;
            keys[2] = KeyEvent.VK_RIGHT;
            keys[3] = KeyEvent.VK_DOWN;
            keys[4] = KeyEvent.VK_NUMPAD1;
            keys[5] = KeyEvent.VK_NUMPAD2;
            keys[6] = KeyEvent.VK_NUMPAD3;
        } else {
            keys[0] = KeyEvent.VK_A;
            keys[1] = KeyEvent.VK_W;
            keys[2] = KeyEvent.VK_D;
            keys[3] = KeyEvent.VK_S;
            keys[4] = KeyEvent.VK_J;
            keys[5] = KeyEvent.VK_K;
            keys[6] = KeyEvent.VK_L;
        }
    }

    //按键运行方法
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == keys[0]) bL = false;
        else if(key == keys[1]) bU = false;
        else if(key == keys[2]) bR = false;
        else if(key == keys[3]) bD = false;
        else if(key == keys[4]);
        else if(key == keys[5]);
        else if(key == keys[6]);
        locateDirection();
    }

    //按各个键不同的动作
    public void KeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == keys[0]) {bL = true;
            String filepath = "src\\MUSIC\\jiao2.wav";
            music musicObject = new music();
            musicObject.playMusic(filepath);
        }//向左
        else if(key == keys[1]) {bU = true;
            String filepath = "src\\MUSIC\\jiao2.wav";
            music musicObject = new music();
            musicObject.playMusic(filepath);
        }//向上
        else if(key == keys[2]) {bR = true;
            String filepath = "src\\MUSIC\\jiao2.wav";
            music musicObject = new music();
            musicObject.playMusic(filepath);
        }//向右
        else if(key == keys[3]) {bD = true;
            String filepath = "src\\MUSIC\\jiao2.wav";
            music musicObject = new music();
            musicObject.playMusic(filepath);
        }//向下
        //普通公鸡
        else if(key == keys[4] && this.getCurrentWeapon().getColdDown() == 0) {
            this.getCurrentWeapon().setState();
            this.getCurrentWeapon().setColdDown();
            String filepath = "src\\MUSIC\\ji.wav";
            music musicObject = new music();
            musicObject.playMusic(filepath);
        }
        //切换武器
        else if(key == keys[5]){
            this.NextWeapon();
        }
        //放大招 如果按下L键冷却为0
        else if(key == keys[6] && this.getCurrentWeapon().getColdDown() == 0){
            Weapon weapon = this.getCurrentWeapon();
            if(weapon instanceof Fireball) {
                ((Fireball) this.getCurrentWeapon()).setUltimateState();
                this.getCurrentWeapon().setColdDown();
                String filepath = "src\\MUSIC\\fireball.wav";
                music musicObject = new music();
                musicObject.playMusic(filepath);
            }
            else if (weapon instanceof SwordSkill){
                this.getCurrentWeapon().setState();
                this.getCurrentWeapon().setColdDown();
            }
        }
        locateDirection();
    }

    //定位的方法
    public void locateDirection() {
        //就是如果按的不是其他三个键就返回按那个键的该有的方向
        //2个键就是45度角的方向
        //都没按就是静止状态
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    //画出人物现在的武器
    public void draw(Graphics g){
        g.drawString(getCurrentWeapon().toString(), this.x - 20, this.y - 45);
        //g.drawString("HP: " + String.valueOf(this.HP),this.x-8,this.y-28);
        drawBloodBar(g);
        int b = this.getBegin();
        if(b > 0 && (b / 3) % 2 == 0) {
        	this.getCurrentWeapon().maintainColdDown();
        	mainTainWalkState(16);
            move();
        } else {
        	super.draw(g);
        }
    }

    //设置死亡状态
    public void setDeadState(){
    	this.setxIncrement(0, 0);
    	this.setyIncrement(0, 0);
    	this.onAttackState = 0;
        this.deadState = 600;
    }

    //重新启动
    public void resetBegin() {
        this.x = (new Random().nextInt(100) % 2 == 0) ? 340 : 620;
        this.y = 280;
        this.setHP(MAX_HP);
        this.deadState = -1;
        super.resetBegin();
    }

    //维持死亡状态
    public void maintainDeadState() {
    	world.searchHero();
    	if(deadState <= 0) {
        	if(world.searchHero()) {
        		this.resetBegin();
        	} else {
        		world.objDead(this);
        	}
    	} else {
    		this.deadState++;
    	}
    }
}

package Game.gameObject;

import Game.People.Hero;
import Game.WeaponChildren.Fireball;
import Game.WeaponFather.GameObject;
import Game.WeaponFather.Weapon;

import java.awt.*;
import java.util.Random;

public class Box extends GameObject {
    //延迟时间
    public static final int DELAYTIME = 800;
    //延迟
    private int delay = 0;
    //设定box的属性
    public Box(int x, int y, World world) {
        super("Box", 14, 0, 99999, x, y, false, world);
    }

    //画出箱子
    public void draw(Graphics g){
        world.collisionDetection(this);
        g.drawImage(imgMap.get(name), x - 20, y - 10, 60, 60, null);
    }
    //碰撞回应
    public void collisionResponse(GameObject object){
        if(object instanceof Hero) {
            Random rand = new Random();
            //随机生成一个n值,如果n<45hero获得fireball的次数,如果n<90获得血量
            int n = Math.abs(rand.nextInt()) % 100;
            if(n < 45)
            	fireballBox((Hero) object);
            else if(n < 90)
                    bloodBox((Hero) object);
        //    System.out.println(n);
            world.pickUpBox(this);
            //设置延时
            setDelay(DELAYTIME);
        }
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void onAttack(Weapon weapon){
    }

    //设置回复火球术的箱子回复的方法
    public void fireballBox(Hero hero) {
        for (Weapon weapon : hero.getWeapons())
            if (weapon instanceof Fireball) {
                ((Fireball) weapon).setNum(((Fireball) weapon).getMaxNum());
                break;
            }
    }
    //设置回血方法回满
    public void bloodBox(Hero hero) {
        hero.setHP(Hero.MAX_HP);
    }

    //判断是否碰撞人物墙壁之类
    public boolean collisionDetection(GameObject object){
        if(!(object instanceof Hero)) return false;
        double deltaX = this.x - object.getX();
        double deltaY = (this.y - object.getY());
        double d = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        int R = this.getRadius() + object.getRadius();
        if(d <= R) return true;
        return false;
    }
}

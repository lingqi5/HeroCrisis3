package Game.WeaponFather;

import Game.People.Enemy;
import Game.Role.Direction;
import Game.Role.Role;
import Game.gameObject.World;

import java.awt.*;
import java.util.Iterator;

public abstract class Weapon extends GameObject {
    protected int state;//状态
    protected Role host;//武器的主人
    protected int coldDownTime;//冷却时间
    protected int coldDown;//冷却
    protected int damage;//伤害
    protected int attackRange;//攻击范围
    protected int attackAngle;//攻击角

    //武器属性
    public Weapon(String name, int radius, int speed, int damage, int coldDownTime, Role host, int attackRange,int attackAngle, boolean collidable, World world) {
        super(name, radius, speed, host.getDir(), 9999, host.x, host.y, collidable,  world);
        this.damage = damage;//伤害
        this.coldDownTime = coldDownTime;//冷却时间
        this.coldDown = 0;//冷却
        this.host = host;//武器的主人
        this.attackRange = attackRange;//攻击范围
        this.attackAngle = attackAngle;//攻击角
        state = -1;//
    }

    //设置武器伤害
    public void setDamage(int damage) {
        this.damage = damage;
    }

    //画出平常攻击
    public int drawNomalAttack(Graphics g){
        //
        if(state < 0) return state;
        int lastState = state;
        int picX = getState() / 3 + 5;
        int picY = (host.getDir() == Direction.STOP ? host.getOldDir() : host.getDir()).ordinal();
        drawOneImage(g, host.name, Role.PICOFFSET, host.x, host.y, picX, picY);
        maintainState(9);
        return lastState;
    }

    //攻击
    public void Attack(){
        //迭代器
        Iterator<GameObject> iter = world.getObjectsIterator();

        while(iter.hasNext()){
            GameObject object = iter.next();
            if(this.host.equals(object)) continue;
            if(this.host instanceof Enemy && object instanceof Enemy) continue;
            int objX = object.getX();
            int objY = object.getY();
            int deltaX = objX - this.getX();
            int deltaY = - objY + this.getY();
            double D = getDistance(objX, objY, this.getX(),this.getY());
            if(D < getAttackRange()){
                double sinA = deltaY / D;
                double cosA = deltaX / D;
                double angle1 = Math.toDegrees(Math.asin(sinA));
                double angle2 = Math.toDegrees(Math.acos(cosA));
                double angle = angle2;
                if(angle1 < 0) angle = 360 - angle;
                double myangle = Direction.toDegree(this.host.getDir());
                if(myangle == 360) myangle = Direction.toDegree(this.host.getOldDir());
                if(Math.abs(myangle - angle) < attackAngle || 360 - Math.abs(myangle - angle) < attackAngle)
                    object.onAttack(this);
            }
        }
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int maintainState(int n){
        this.state++;
        if(state >= n) {
            state = -1;
        }
        return state;
    }

    public int maintainColdDown(){
        if(coldDown > 0)
            coldDown--;
        return coldDown;
    }

    public void setColdDownTime(int coldDownTime) {
        this.coldDownTime = coldDownTime;
    }

    public void setColdDown(){
        coldDown = coldDownTime;
    }

    public int getColdDown() {
        return coldDown;
    }

    public void onAttack(Weapon weapon){ }

    public void draw(Graphics g){
    }

    public void collisionResponse(GameObject object){
    }

    public int getDamage(){
        return this.damage;
    }

    public int getState(){
        return this.state;
    }

    public void setState(){
        this.state = 0;
    }

    public void resetState(){
        this.state = -1;
    }

    public boolean isHost(Role role){
        if(this.host.equals(role)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

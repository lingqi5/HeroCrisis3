package Game.Role;

import Game.WeaponFather.GameObject;
import Game.People.Hero;
import Game.WeaponChildren.Hand;
import Game.WeaponChildren.Sword;
import Game.WeaponFather.Weapon;
import Game.gameObject.World;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Role extends GameObject {
    public static final int PICOFFSET = 32;//为了画出人物和僵尸武器之类查找人物现在的状态然后在那一张很大的人物图取出画出来
    private int walkState; //行走状态
    private List<Weapon> weapons;//武器列表
    private Weapon currentWeapon;//现在的武器
    protected int deadState;//死亡状态
    private int maxHP;//满血
    private int begin;//开始
    //血压
    private class BloodBar {
        //画出血液
        public void draw(Graphics g) {
            //最大血液长度
            int maxLength = 40;
            //
            int length = (int)((double)getHP() / (double)getMaxHP() * 40);
            Color c = g.getColor();
            //设置血液颜色为红色
            g.setColor(Color.RED);
            //画出矩形
            g.drawRect(x - 20, y - 40, maxLength, 7);
            //将血液颜色填充进矩形
            g.fillRect(x - 20, y - 40, length, 7);
            g.setColor(c);

        }
    }
    private BloodBar bloodBar;

    //基础规则的参数列表
    public Role(String name, int HP, int radius, int speed,int x, int y, World world) {
        super(name , radius, speed, HP, x, y, true, world);
        this.maxHP = this.HP;
        this.weapons = new ArrayList<>();
        this.walkState = 0;
        this.deadState = -1;
        if(this instanceof Hero)
        	this.begin = 250;
        else this.begin = 0;
        bloodBar = new BloodBar();
    }

    //判断行走改变方向什么的
    public int mainTainWalkState(int n){
        //如果不是静止状态 老方向等于新方向就行走一单位 如果不等于就改变方向不行走
        if(dir != Direction.STOP) {
            if (dir == oldDir) {
                walkState++;
            } else {
                walkState = 0;//保持坐标不变
                oldDir = dir;//原方向和新方向相同+
            }
        } else {
            walkState = -1;//后退一步
        }
        if(walkState >= n) walkState = 0;
        return walkState;
    }

    //画出行走图片
    public void drawWalkImage(Graphics g) {
        if(mainTainWalkState(16) < 0){
            drawOneImage(g, name, PICOFFSET,this.x, this.y, 0, this.oldDir.ordinal());
        } else {
            drawOneImage(g, name, PICOFFSET, this.x, this.y, walkState / 4 + 1, this.dir.ordinal());
        }
    }

    //画笔画出图片
    public void draw(Graphics g) {
	    if(deadState >= 0){
	        this.drawOneImage(g, name, PICOFFSET, this.x, this.y, 13, 0);
	        this.maintainDeadState();
	        return;
	    }
	    //检测到攻击画出 伤害大于20添加血迹
	    if(checkOnAttack() > 0){
	        this.drawOneImage(g, name, PICOFFSET, this.x, this.y, 0, this.oldDir.ordinal());
	        Random rand = new Random();
	        if(Math.abs(rand.nextInt(100)) > 20) world.addBlood(this.x, this.y);
	        onAttackState--;
	    } else if (currentWeapon.getState() >= 0 && (currentWeapon instanceof Sword || currentWeapon instanceof Hand)) {
	        this.currentWeapon.drawNomalAttack(g);
	        if (this.currentWeapon.getState() == 1)
	            this.currentWeapon.Attack();
	        this.currentWeapon.maintainColdDown();
	        return;
	    }
	    this.drawWalkImage(g);
        
        this.currentWeapon.maintainColdDown();
        //g.drawString("("+String.valueOf(this.x) +","+ String.valueOf(this.y)+")", this.x-8, this.y - 40);
        move();
    }

    //移动方法
    public void move(){
        if((getDir() == Direction.STOP  && checkOnAttack() <= 0) || getHP() <= 0) return;
        double degree = Direction.toDegree(getDir());
        if(checkOnAttack() <= 0) {
            setxIncrement(degree, speed);
            setyIncrement(degree, speed);
        }
        world.collisionDetection(this);
        this.x += getxIncrement();
        this.y += getyIncrement();
    }

    //碰撞响应
    public void collisionResponse(GameObject object){
        //如果位置静止
        if(this.dir == Direction.STOP) return;
        //将要碰撞的物体和自己位置x坐标的差值
        int deltaX = object.getX() - this.getX();
        //y坐标的差值
        int deltaY = object.getY() - this.getY();
        //临时矢量x
        double tmpVectorX = 1.0 / deltaX;
        //临时矢量y
        double tmpVectorY = -1.0 / deltaY;
        //求出临时矢量的第三边 勾股定理
        double normOfTmp = Math.sqrt(Math.pow(tmpVectorX, 2) + Math.pow(tmpVectorY, 2));
        //弧度制转换
        double dirX = Math.cos(Math.toRadians(Direction.toDegree(this.dir)));
        //弧度值转换
        double dirY = -Math.sin(Math.toRadians(Direction.toDegree(this.dir)));
        //求出边长边乘上弧度制就是那个圆弧
        double newSpeed = (tmpVectorX * dirX + tmpVectorY * dirY) / normOfTmp * getSpeed();
        //x的新位置 原来x+圆弧边的期中一个 y同理
        int newDirX = (int) (newSpeed * tmpVectorX);
        int newDirY = (int) (newSpeed * tmpVectorY);
        this.x += newDirX;
        this.y += newDirY;
    }

    //如果攻击状态
    public void onAttack(Weapon weapon){
        //hp<=0
    	if(getHP() <= 0 || begin > 0) return;
    	//设置攻击状态初始值为5
        this.onAttackState = 5;
        //设置hp为现在的hp-武器的伤害值
        this.setHP(this.getHP() - weapon.getDamage());
        //如果得到hp小于0 设置hp为0 并设置为死亡状态
        if(getHP() <= 0) {
            this.deadState=0;

            this.setHP(0);
            this.setDeadState();
            this.deadState=0;

            return;
        }
        //武器的x坐标
        int weaponX = weapon.getX();
        //武器的y坐标
        int weaponY = weapon.getY();
        //武器坐标x与物体坐标x的差值
        int deltaX = weaponX - this.x;
        //武器坐标y与物体坐标y的差值
        int deltaY = weaponY - this.y;
        //D勾股定理算出三角形第三条边的值
        double D = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        //cosA
        double cosA = deltaX / D;
        double sinA = deltaY / D;
        //x的增加量
        this.xIncrement = (int)(-cosA * 8);
        //y的增量
        this.yIncrement = (int)(-sinA * 8);
        //添加血液再物体的坐标上
        world.addBlood(this.x, this.y);
    }

    //设置死亡状态的数值为150
    public void setDeadState(){
        this.deadState = 150;
    }
    //得到武器数量
    public int getWeaponsAmount(){
        return weapons.size();
    }
    //添加武器
    public void addWeapon(Weapon weapon){
        weapons.add(weapon);
    }
    //设置现在的武器
    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }
    //得到现在的武器
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }
    //存储武器的集合获得武器
    public List<Weapon> getWeapons() {
        return weapons;
    }
    //设置武器
    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }
    //维持死亡状态 死亡状态小于等于0移除图片
    public void maintainDeadState() {
        //如果死亡状态大于0 --
    	if(deadState > 0)
        	deadState--;
    	else
    		world.objDead(this);
    }
    //下一个武器
    public void NextWeapon() {
        //设置指针代替现在的武器
        int index = weapons.indexOf(currentWeapon);
        //如果现在的武器存储值加一小于现在的武器数 现在的武器值加一
        if(index + 1 < this.getWeaponsAmount()){
            currentWeapon = weapons.get(index + 1);
        } else {
            currentWeapon = weapons.get(0);
        }
    }

    //判断是否碰撞
    public boolean collisionDetection(GameObject object){
        //如果物体待敌武器和武器物体返回未碰撞
        if(object instanceof Weapon && ((Weapon) object).isHost(this)){
            return false;
        }
        return super.collisionDetection(object);
    }
    //得到血量
    public int getMaxHP() {
        return maxHP;
    }

    //画出地上的血液
    public void drawBloodBar(Graphics g){
        bloodBar.draw(g);
    }
    //得到开始
    public int getBegin() {
    	return begin--;
    }
    //重新开始
    public void resetBegin() {
    	this.begin = 350;
    }
}
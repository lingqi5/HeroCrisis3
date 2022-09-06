package Game.WeaponFather;

import Game.Role.Direction;
import Game.People.Hero;
import Game.gameObject.World;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GameObject implements Cloneable{
    protected String name;//名称·
    protected int radius; //半径
    protected int speed;//速度
    protected int xIncrement;//x增长
    protected int yIncrement;//y增长
    protected Direction dir;//方向
    protected Direction oldDir;//原来的方向
    protected int x, y;//坐标
    protected int HP;//血量
    protected int onAttackState;//攻击状态
    protected boolean collidable;//是否碰撞
    protected World world;//世界
    protected static Toolkit tk = Toolkit.getDefaultToolkit();//键盘
    protected static Image[] imgs = null;//图片数组
    protected static Map<String, Image> imgMap = new HashMap<String, Image>();//地图

    //画笔
    public abstract void draw(Graphics g);
    //碰撞响应
    public abstract void collisionResponse(GameObject object);
    //攻击
    public abstract void onAttack(Weapon weapon);

    static {
        //存入游戏所需图片
        imgs = new Image[] {
                tk.getImage(GameObject.class.getClassLoader().getResource("images/hero.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/monster.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/fireball.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/ghost.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/ghostball.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/wall.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/blood.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/box.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/SwordSkill.png"))
        };
        //将图片以序号放置
        imgMap.put("Hero", imgs[0]);
        imgMap.put("Monster", imgs[1]);
        imgMap.put("Fireball", imgs[2]);
        imgMap.put("Ghost", imgs[3]);
        imgMap.put("Ghostball", imgs[4]);
        imgMap.put("Wall", imgs[5]);
        imgMap.put("Blood", imgs[6]);
        imgMap.put("Box", imgs[7]);
        imgMap.put("SwordSkill",imgs[8]);
    }

    //构造方法 负责调用时修改数值 静止的物体
    public GameObject(String name, int radius, int speed, int HP, int x, int y, boolean collidable, World world) {
        this.name = name;
        this.radius = radius;
        this.speed = speed;
        this.xIncrement = 0;
        this.yIncrement = 0;
        this.onAttackState = 0;
        this.HP = HP;
        this.dir = Direction.STOP;
        this.oldDir = Direction.D;
        this.x = x;
        this.y = y;
        this.collidable = collidable;
        this.world = world;
    }

    //移动物体
    public GameObject(String name, int radius, int speed, Direction dir, int HP, int x, int y, boolean collidable, World world) {
        this.name = name;
        this.radius = radius;
        this.speed = speed;
        this.xIncrement = 0;
        this.yIncrement = 0;
        this.onAttackState = 0;
        this.dir = dir;
        this.HP = HP;
        this.oldDir = Direction.D;
        this.x = x;
        this.y = y;
        this.collidable = collidable;
        this.world = world;
    }

    //画出一张图片
    public void drawOneImage(Graphics g, String name, int picOffset,int x, int y, int picX, int picY){
        g.drawImage(
                imgMap.get(name),
                x - picOffset - 4,
                y - picOffset - 4,
                x + picOffset + 4,
                y + picOffset + 4,
                picX * picOffset * 2,
                picY * picOffset * 2,
                picX * picOffset * 2 + picOffset * 2 - 1,
                picY * picOffset * 2 + picOffset * 2 - 1,
                null);

    }

    //碰撞检测
    public boolean collisionDetection(GameObject object){
        //如果碰撞返回错误
        if(!object.isCollidable()) return false;
        //如果得到的位置为停止或没有攻击返回未碰撞
        if(this.getDir() == Direction.STOP  && this.checkOnAttack() <= 0) return false;
        //x坐标差值
        double deltaX = this.x - object.getX();
        //y坐标差值
        double deltaY = (this.y - object.getY());
        //sqrt开平方跟 pow幂运算这个就是平方 d是三角形第三条边 为了计算cos sin 值
        double d = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        //Radius半径
        int R = this.getRadius() + object.getRadius();
        //如果d<=r碰撞 让物体后移
        if(d <= R){
            //计算cos值
            double cosValue = deltaX / d;
            //sin值
            double sinValue = deltaY / d;
            //碰撞后x移动的距离
            int offsetY = (int)((R - d + 2) * sinValue);
            //碰撞后y移动的距离
            int offsetX = (int)((R - d + 2) * cosValue);
            //碰撞后下x，y坐标值
            this.x += offsetX;
            this.y += offsetY;
            return true;
        }
        return false;
    }

    //检测攻击
    public int checkOnAttack(){
        return (this.onAttackState);
    }
    //重置攻击状态 默认值改为0
    public void resetOnAttackState(){
        this.onAttackState = 0;
    }
    //得到速度
    public int getSpeed() {
        return this.speed;
    }
    //得到半径
    public int getRadius() {
        return this.radius;
    }
    //得到方向
    public Direction getDir() {
        return this.dir;
    }
    //设置方向
    public void setDir(Direction dir) {
        this.dir = dir;
    }
    //得到原来地方向
    public Direction getOldDir() {
        return this.oldDir;
    }
    //设置半径
    public void setRadius(int radius) {
        this.radius = radius;
    }
    //得到x值
    public int getX() {
        return this.x;
    }
    //得到y值
    public int getY() {
        return this.y;
    }
    //得到hp值
    public int getHP() {
        return this.HP;
    }
    //设置hp
    public void setHP(int HP) {
    	if(HP == 0 && !(this instanceof Hero))
    		this.collidable = false;
        this.HP = HP;
    }
    //碰撞
    public boolean isCollidable() {
        return collidable;
    }
    //得到x增加值
    public int getxIncrement() {
        return xIncrement;
    }
    //设置x地增加值
    public void setxIncrement(double degree, int speed) {
        this.xIncrement = (int)(getSpeed() * Math.cos(Math.toRadians(degree)));
    }
    //得到y地增加值
    public int getyIncrement() {
        return yIncrement;
    }
    //得到y地增加值
    public void setyIncrement(double degree, int speed) {
        this.yIncrement = -(int)(getSpeed() * Math.sin(Math.toRadians(degree)));
    }
    //得到物体距离
    public double getDistance(int x1, int y1, int x2, int y2){
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    //重写clone方法要不然使用会报出异常
    //clone()方法可以克隆对象
    //surpe.clone() 深克隆引用地数据类型在修改的时候也会修改
    @Override
    public Object clone() {
        GameObject obj = null;
        try{
            obj = (GameObject) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

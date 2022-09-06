package Game.gameObject;

import Game.People.*;
import Game.Role.Role;
import Game.WeaponFather.GameObject;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    private CopyOnWriteArrayList<GameObject> objects;//存储物体的集合
    private CopyOnWriteArrayList<Blood> bloods;//存储血液的集合
    private List<Box> pickedBoxes;//存储盒子的集合
    private int maxBloodNum = 5000;//最多血液数量
    private int bloodNum;//血液数
    private int width;//宽度
    private int height;//高度
    private int maxEnemyNum;//最大敌人的数量
    private int currentEnemyNum;//现在敌人的数量
    private int producedEnemyNum;//生产敌人的数量
    private int produceDelay;//生产延时
    private int boxDelay;//盒子产生的延时
    private Image endImg;//结束图片
    private int end;//结束状态

    //定义世界的参数
    public World(int width, int height, boolean Doubleplayer) {
        this.width = width;//宽度
        this.height = height;//高度
        this.objects = new CopyOnWriteArrayList<>();//物体集合初始化
        this.bloods = new CopyOnWriteArrayList<>();//血液集合初始化
        this.pickedBoxes = new ArrayList<>();//盒子集合的初始化
        this.bloodNum = 0;//血液初始数量
        this.maxEnemyNum = 3;//最大的敌人数量
        this.currentEnemyNum = 0;//现在的敌人数量
        this.producedEnemyNum = 0;//初始生产敌人数量
        this.boxDelay = 0;//初始化盒子延时
        this.end = -1;//设置结束的初始值为-1
        this.endImg = Toolkit.getDefaultToolkit().getImage(World.class.getClassLoader().getResource("images/gameover.png"));
        //hero的坐标
        objects.add(new Hero(340, 180, 1, this));

        //双人游戏
        if(Doubleplayer)
            objects.add(new Hero(620, 180, 0, this));
        objects.add(new Border(0, this));
        objects.add(new Border(1, this));
        objects.add(new Border(2, this));
        objects.add(new Border(3, this));
        objects.add(new Box(320, 360, this));
        objects.add(new Box(640, 360, this));
        for(int i = 1; i <= 2; i++)
            for(int j = 1; j <= 3; j++){
                objects.add(new Wall(width / 4 * j, height / 3 * i, this));
            }
    }

    public Iterator<GameObject> getObjectsIterator(){
        return objects.iterator();
    }

    public void removeObject(GameObject obj){
        objects.remove(obj);
    }

    public void addObject(GameObject obj){
        objects.add(obj);
    }

    public GameObject getObject(int index){
        return objects.get(index);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentEnemyNum() {
        return currentEnemyNum;
    }

    public void setCurrentEnemyNum(int currentEnemyNum) {
        this.currentEnemyNum = currentEnemyNum;
    }

    public void setProduceDelay(){
        this.produceDelay = 50;
    }

    //吃盒子的方法
    public void pickUpBox(Box box){
        this.removeObject(box);
        pickedBoxes.add(box);
        box.setDelay(Box.DELAYTIME);
    }

    //生产盒子的方法
    public void produceBox(){
        //遍历盒子 如果盒子的延迟时间等于0 添加盒子这个物体
        for(Box box : pickedBoxes){
            if(box.getDelay() == 0){
                this.addObject(box);
                pickedBoxes.remove(box);
                break;
            } else {
                box.setDelay(box.getDelay() - 1);
            }
        }
    }

    //生产敌人
    public void produceEnemy(){
        //生产敌人的延迟
        produceDelay = (produceDelay - 1 > 0? produceDelay - 1 : 0);
        //现在的敌人大于100返回
        if(currentEnemyNum >= 100) return;
        //生产的敌人小于最大敌人数量并且生产延迟小于等于0
        if(producedEnemyNum < maxEnemyNum && produceDelay <= 0){
            Random rand = new Random();
            //生成随机数余4
            int pos = Math.abs(rand.nextInt()) % 4;//能生成4个值0-3
            int type = Math.abs(rand.nextInt()) % 100;//生产0-99的数
            int t = (rand.nextInt() % 2) * Role.PICOFFSET * 2;//产生0或1*32*2
            int off = Role.PICOFFSET + 10;//42就关闭
            //根据pos的值产生幽灵
            switch (pos){
                case 0:
                    objects.add(type < 10 ? new Ghost(width / 2 + t, off, this) : new Monster(width / 2 + t, off, this));
                    break;
                case 1:
                    objects.add(type < 10 ? new Ghost(off, height / 2 + t, this) : new Monster(off, height / 2 + t, this));
                    break;
                case 2:
                    objects.add(type < 10 ? new Ghost(width / 2 + t, height - off, this) : new Monster(width / 2 + t, height - off, this));
                    break;
                case 3:
                    objects.add(type < 10 ? new Ghost(width - off, height / 2 + t, this) : new Monster(width -off, height / 2 + t, this));
                    break;
                default:
                    break;
            }
            //现在的敌人数量加一
            currentEnemyNum++;
            //生产敌人的数量加一
            producedEnemyNum++;
            //设置生产延时
            setProduceDelay();
        } //如果现在的敌人数量小于等于0并且生产数量等于最大量 最大敌人量加3 生产数量变为0(敌人会越来越多)
        else if(currentEnemyNum <= 0 && producedEnemyNum == maxEnemyNum) {
            maxEnemyNum += 3;
            producedEnemyNum = 0;
        }
    }

    //检验是否碰撞
    public boolean collisionDetection(GameObject obj){
        //通过迭代器访问游戏物体的集合
        Iterator<GameObject> iter = this.getObjectsIterator();
        int flag = 0;
        while(iter.hasNext()){
            //查找下一步如果下一个物体和现在的物体相同并且下个状态的hp>0  指针变为1返回碰撞
            GameObject tmpObj = iter.next();
            if(!obj.equals(tmpObj) && tmpObj.getHP() > 0){
                if(obj.collisionDetection(tmpObj)){
                    obj.collisionResponse(tmpObj);
                    flag = 1;
                }
            }
        }
        if(flag == 1) return true;
        else return false;
    }

    //物体种类
    public void objectSort(){
        //对物体集合进行排序从小到大
        Collections.sort(objects, new Comparator<GameObject>() {
            @Override
            //比较2个物体的y 如果y相等比较x坐标 否则返回i值
            public int compare(GameObject obj1, GameObject obj2) {
                int i = obj1.getY() - obj2.getY();
                if(i == 0){
                    return obj1.getX() - obj2.getX();
                }
                return i;
            }
        });
    }

    //画出world
    public void drawWorld(Graphics g){
        //如果游戏结束end--
        if(isEnd()) {
            end--;
        }
        //生产敌人 生产箱子
        produceEnemy();
        produceBox();
        //遍历血液 画出血液
        for(Blood blood : bloods){
            blood.draw(g);
        }
        //对物体进行数值排序
        this.objectSort();
        Iterator<GameObject> iter =this.getObjectsIterator();
        //根据排序值依次画出
        while(iter.hasNext()){
            iter.next().draw(g);
        }
    }

    //物体死亡如果obj代替敌人 现在的敌人数量减一 移除敌人
    public void objDead(Object obj){
        if(obj instanceof Enemy) currentEnemyNum--;
        this.objects.remove(obj);
    }

    //添加血液数量
    public int addBloodNum(){
        //血液数等于血液数加一余最大血液数
        bloodNum = (bloodNum + 1) % maxBloodNum;
        return bloodNum;
    }
    //利用迭代器浏览英雄
    public boolean searchHero() {
        //创建迭代器
        Iterator<GameObject> iter = getObjectsIterator();
        //如果有下一个 物体等于下一个状态 如果obj代替的是hero 并且obj的hp>0返回 否则 游戏结束 返回false
        while(iter.hasNext()) {
            GameObject obj = iter.next();
            if(obj instanceof Hero && obj.getHP() > 0) return true;
        }
        gameOver();
        return false;
    }

    //添加血液
    public void addBlood(int x, int y){
        int n = addBloodNum();
        if(bloods.size() < maxBloodNum){
            bloods.add(new Blood(x, y, this));
        } else {
            bloods.set(n, new Blood(x, y, this));
        }
    }

    //画出结局
    public void drawEnd(Graphics g) {
        g.drawImage(endImg, 0, 0, width, height, null);
    }

    //游戏结束方法
    public void gameOver() {
        if(!isEnd())
            this.end = 30;
    }
    //结束
    public boolean isEnd() {
        return this.end >= 0;
    }
    //
    public boolean End() {
        return this.end == 0;
    }
}

package Game.People;

import Game.WeaponFather.GameObject;
import Game.gameObject.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//世界的网格
class Grid implements Cloneable{
    public static final int LENGTH = 10;
    private int gridX, gridY;//网格的x坐标 y坐标
    private int x, y;
    private boolean accessible;//是否可见
    private GameObject object;
    private boolean isBorder;

    public Grid(int gridX, int gridY, boolean isBorder) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.x = gridX * LENGTH;
        this.y = gridY * LENGTH;
        this.isBorder = isBorder;//是边界
        this.accessible = !isBorder;//不是边界
        this.object = null;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public GameObject getObject() {
        return object;
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

    //克隆网格
    @Override
    public Object clone() {
        Grid tmp = null;
        try{
            tmp = (Grid) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tmp;
    }
}

//世界网格
public class WorldGrids {
    //使用有序集合存储网格
    private List<Grid> grids;
    private World world;
    private List<Grid> unaccessibleGrids;
    private int w, h;

    public WorldGrids(World world) {
        //实例化对象
        this.world = world;
        //地图长度除网格长度 地图宽度除网格宽度 获得网格的个数
        w = world.getWidth() / Grid.LENGTH;
        h = world.getHeight() / Grid.LENGTH;
        this.grids = new ArrayList<>();
        this.unaccessibleGrids = new ArrayList<>();
        //遍历网格 添加进集合
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(j == 0 || j == 1 || i <= 5)
                    grids.add(new Grid(j, i, true));
                else
                    grids.add(new Grid(j, i, false));
            }
        }
    }

    //更新网格
    public void updateGrids(){
        resetGrid();;

        //创建游戏物体迭代器
        Iterator<GameObject> objIter = world.getObjectsIterator();
        //遍历物体 object等于下一个物体
        while(objIter.hasNext()){
            GameObject object = objIter.next();

            if(!object.isCollidable() || object instanceof Border) continue;
            Iterator<Grid> tmpIt = getGrid(object).iterator();
            while(tmpIt.hasNext()){
                Grid tmp = tmpIt.next();
                tmp.setObject(object);
                tmp.setAccessible(false);
                unaccessibleGrids.add(tmp);
            }
        }
    }

    //复位网格
    public void resetGrid(){
        Iterator<Grid> unAcGridIter = getUnaccessibleGridsIterator();
        while(unAcGridIter.hasNext()){
            Grid tmp = unAcGridIter.next();
            tmp.setAccessible(true);
            tmp.setObject(null);
            unAcGridIter.remove();
        }
    }

    //得到网格 用世界的宽度和高度除网格的高度和宽度得到网格的个数
    public List<Grid> getGrid(GameObject obj){
        List<Grid> grids = new ArrayList<>();
        int objX = obj.getX() - obj.getRadius();
        int objY = obj.getY() - obj.getRadius();
        int length = obj.getRadius() * 2;
        int t = length / Grid.LENGTH + 1;
        for(int x = objX, i = 0; i < t && i < w; i++, x += Grid.LENGTH)
            for(int y = objY, j = 0; j < t && j < h; j++, y += Grid.LENGTH) {
                Grid tmp = getGrid(x+Grid.LENGTH, y+Grid.LENGTH);
                if(grids.indexOf(tmp) < 0)
                    grids.add(tmp);
            }
        return grids;
    }

    public Grid getGrid(int x, int y){
        int gridX = x / Grid.LENGTH;
        int gridY = y / Grid.LENGTH;
        return this.grids.get(gridY * w + gridX);
    }

    public Grid get(int gridX, int gridY){
        if(gridX < 0) gridX = 0;
        if(gridY < 0) gridY = 0;
        if(gridX >= w) gridX = w-1;
        if(gridY >= h) gridY = h-1;
        return this.grids.get(gridY * w + gridX);
    }

    public Iterator<Grid> getUnaccessibleGridsIterator(){
        return unaccessibleGrids.iterator();
    }

    public Iterator<Grid> getGridsIterator(){
        return grids.iterator();
    }
}

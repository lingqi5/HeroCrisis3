package Game.People;


//继承排序接口 与本身进行比较
public class PathNode implements Comparable<PathNode>{
    //网格状态数据
    private Grid stateData;
    //节点
    private PathNode parentNode;
    private int g, h, f, depth; //g 从起点移动到指定方格的移动代价, h 从指定的方格移动到终点的估算成本
    //获得节点
    public PathNode getParentNode() {
        return parentNode;
    }
    //设置节点
    public void setParentNode(PathNode parentNode) {
        this.parentNode = parentNode;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Grid getStateData() {
        return stateData;
    }

    public void setStateData(Grid stateData) {
        this.stateData = stateData;
    }

    public PathNode(Grid stateData, PathNode parentNode, int g, int h, int depth) {
        this.stateData = stateData;
        this.parentNode = parentNode;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
        this.depth = depth;
    }

    //将other 参数与NodeComp比较
    @Override
    public int compareTo(PathNode other)
    {
        int NodeComp = (this.f - other.getF()) * -1;
        if (NodeComp == 0)
        {
            NodeComp = (this.depth - other.getDepth());
        }
        return NodeComp;
    }
}

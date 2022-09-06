package Game.Role;

public enum Direction {
    //定义direction的8种方向 和静止状态
    LD, RU, LU, RD, L, R, U, D, STOP;

    //定义位置
    public static double toDegree(Direction dir){
        switch (dir){
            //右边
            case R: return 0;
            //右上
            case RU: return 45;
            //上边
            case U: return 90;
            //左上
            case LU: return 135;
            //左边
            case L: return 180;
            //左下
            case LD: return 225;
            //下边
            case D: return 270;
            //右下
            case RD:  return 315;

            default: return 360;
        }
    }

    //得到x向量坐标(转换为弧度)
    public static double getVectorX(Direction dir){
        switch (dir){
            case R: return 1;
            case RU: return Math.cos(Math.toRadians(45));
            case U: return 0;
            case LU: return -Math.cos(Math.toRadians(45));
            case L: return -1;
            case LD: return -Math.cos(Math.toRadians(45));
            case D: return 0;
            case RD:  return Math.cos(Math.toRadians(45));
            default: return -2;
        }
    }

    //得到y向量坐标(转换为弧度)
    public static double getVectorY(Direction dir){
        switch (dir){
            case R: return 0;
            case RU: return Math.cos(Math.toRadians(45));
            case U: return 1;
            case LU: return Math.cos(Math.toRadians(45));
            case L: return 0;
            case LD: return -Math.cos(Math.toRadians(45));
            case D: return -1;
            case RD:  return -Math.cos(Math.toRadians(45));
            default: return -2;
        }
    }
}

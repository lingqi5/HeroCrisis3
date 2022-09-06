package Game.People;

import Game.WeaponChildren.Ghostball;
import Game.Role.Direction;
import Game.gameObject.World;

import java.awt.*;

public class Ghost extends Enemy {
    //攻击范围为300
    private int attackRange = 300;

    //设置幽灵的血量
    public Ghost(int x, int y, World world){
        super("Ghost", 130, 14, 2, x, y, world);
        addWeapon(new Ghostball(this, world));
        setCurrentWeapon(getWeapons().get(0));
    }

    //画出幽灵
    public void draw(Graphics g) {
        //幽灵不为空并且血量大于0画出
        if(getTarget() != null && getTarget().getHP() > 0 && this.getHP() > 0) {
            //与人物的距离
            int distance = (int) getDistance(this.getX(), this.getY(), getCurrentTarget().getX(), getCurrentTarget().getY());
            if (distance <= attackRange && this.getCurrentWeapon().getColdDown() == 0) {
                double currentDegree = Direction.toDegree(this.dir == Direction.STOP ? oldDir : dir);
                double targetDegree = getDeltaDegree(getTarget().getX(), getTarget().getY());
                if(Math.abs(targetDegree - currentDegree) < 60 || Math.abs(360 - targetDegree + currentDegree) < 60) {
                    this.getCurrentWeapon().setState();
                    this.getCurrentWeapon().setColdDown();
                    getPath();
                }
            }
        }
        locateDirection();
        super.draw(g);
    }
}

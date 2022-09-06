package Game.WeaponChildren;

import Game.Role.Role;
import Game.WeaponFather.Ball;
import Game.gameObject.World;

public class Fireball extends Ball {
    int maxNum = 80;
    public Fireball(Role role, World world) {
        //名称,半径,速度,伤害,冷却时间
        super("Fireball", 15, 15, 55, 10, role, world);
        setNum(maxNum);
        this.picOffset = 16;

    }

    public int getMaxNum() {
        return maxNum;
    }
}



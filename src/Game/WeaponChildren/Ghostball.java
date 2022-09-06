package Game.WeaponChildren;

import Game.Role.Role;
import Game.WeaponFather.Ball;
import Game.gameObject.World;

public class Ghostball extends Ball {
    public Ghostball(Role role, World world) {
        //设置幽灵球的属性
        super("Ghostball", 7, 8, 80, 30, role, world);
        this.setNum(10000);
        this.picOffset = 8;
    }
}

package Game.WeaponChildren;

import Game.Role.Role;
import Game.WeaponFather.Weapon;
import Game.gameObject.World;

public class Hand extends Weapon {
    private int attackRange;

    //攻击模式:手 设置初始属性
    public Hand(Role role, World world){
        super("Hand", 0, 15, 180,12, role, 55, 70, false, world);
    }

    public void Attack(){
        super.Attack();
    }

    public int getX(){
        return this.host.getX();
    }

    public int getY(){
        return this.host.getY();
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }
}

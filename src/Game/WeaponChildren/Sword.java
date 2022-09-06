package Game.WeaponChildren;

import Game.Role.Role;
import Game.WeaponFather.Weapon;
import Game.gameObject.World;

public class Sword extends Weapon {
    //剑的属性
    public Sword(Role role, World world){
        super("Sword", 0, 15, 80, 12, role, 80, 42, false, world);
    }
    //攻击
    public void Attack(){
        super.Attack();
    }
    //坐标
    public int getX(){
        return this.host.getX();
    }
    //坐标
    public int getY(){
        return this.host.getY();
    }
    //攻击方式
    public int getAttackRange() {
        return attackRange;
    }
    //设置攻击方式
    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }
}

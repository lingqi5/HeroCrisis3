package Game.gameObject;

import Game.WeaponFather.GameObject;
import Game.WeaponFather.Weapon;

import java.awt.*;

public class Wall extends GameObject {
    //设置墙体
    public Wall(int x, int y, World world) {
        super("Wall", 50, 0, 99999, x, y, true, world);
    }
    //画出墙体
    public void draw(Graphics g){
        g.drawImage(imgMap.get(name), x - 50, y - 75, 100, 150, null);
    }

    public void collisionResponse(GameObject object){

    }
    //设置能打到
    public void onAttack(Weapon weapon){

    }
}

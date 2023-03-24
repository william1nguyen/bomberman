package entities;

import graphics.Sprite;
import javafx.scene.image.Image;

import static Bomber.Game.*;

public class Kondoria extends Monster {
    private long IntervalChangeDirection = 2100000000;
    private long lastChangeDirection = 0;

    public Kondoria(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update() {
        if (isDead()) {
            dead();
        }
        else {
            findBomber();
            move();
        }

        spriteChange();
    }

    private void findBomber() {
        if (this.x < bomber.getX()) { direct = RIGHT; return; }
        if (this.x > bomber.getX()) { direct = LEFT; return; }
        if (this.y < bomber.getY()) { direct = DOWN; return; }
        if (this.y > bomber.getY()) { direct = UP; return; }
    }

    public void move() {
        if (direct == LEFT) {
            if (spriteCount == 0) img = Sprite.kondoria_left1.getFxImage();
            else if (spriteCount == 1) img = Sprite.kondoria_left2.getFxImage();
            else if (spriteCount == 2) img = Sprite.kondoria_left3.getFxImage();
        }
        else {
            if (spriteCount == 0) img = Sprite.kondoria_right1.getFxImage();
            else if (spriteCount == 1) img = Sprite.kondoria_right2.getFxImage();
            else if (spriteCount == 2) img = Sprite.kondoria_right3.getFxImage();
        }

        x += DIR_X[direct];
        y += DIR_Y[direct];
    }

    public void dead() {
        if (countdownSecond != 0) {
            img = Sprite.kondoria_dead.getFxImage();
            countdownSecond--;
        }
        else {
            img = null;
            numberOfMonster--;
            removeList.add(this);
        }
    }
}
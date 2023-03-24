package entities;

import graphics.Sprite;
import javafx.scene.image.Image;

import static Bomber.Game.*;

public class Doll extends Monster {
    private long IntervalChangeDirection = 2100000000;
    private long lastChangeDirection = 0;
    private int chaseDistance = 150;
    private int cooldownSkill = 0;
    private int countdown = 0;


    public Doll(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update() {
        if (isDead()) {
            dead();
        }
        else {
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
            if (spriteCount == 0) img = Sprite.doll_left1.getFxImage();
            else if (spriteCount == 1) img = Sprite.doll_left2.getFxImage();
            else if (spriteCount == 2) img = Sprite.doll_left3.getFxImage();
        }
        else {
            if (spriteCount == 0) img = Sprite.doll_right1.getFxImage();
            else if (spriteCount == 1) img = Sprite.doll_right2.getFxImage();
            else if (spriteCount == 2) img = Sprite.doll_right3.getFxImage();
        }

        Pair position = new Pair(this.x, this.y);
        if (position.distance(new Pair(bomber.x, bomber.y)) <= chaseDistance && cooldownSkill == 0) {
            if ((Math.abs(bomber.x - this.x) < Sprite.SCALED_SIZE / 2 && (direct == UP || direct == DOWN))
                || (Math.abs(bomber.y - this.y) < Sprite.SCALED_SIZE / 2 && (direct == LEFT || direct == RIGHT))
            ) {
                countdown = 50;
                cooldownSkill = 250;
            }
        }

        if (countdown != 0) {
            if (countdown <= 30) {
                int addX = DIR_X[direct] * 4;
                int addY = DIR_Y[direct] * 4;

                if (direct == LEFT) {
                    addX = Math.max(addX, Sprite.SCALED_SIZE - x);
                } else if (direct == RIGHT) {
                    addX = Math.min(addX, (WIDTH - 1) * Sprite.SCALED_SIZE - x);
                } else if (direct == UP) {
                    addY = Math.max(addY, Sprite.SCALED_SIZE - y);
                } else if (direct == DOWN) {
                    addY = Math.min(addY, (HEIGHT - 1) * Sprite.SCALED_SIZE - y);
                }

                x += addX;
                y += addY;
            }
            countdown--;
        } else {
            findBomber();
            x += DIR_X[direct];
            y += DIR_Y[direct];
        }

        if (cooldownSkill > 0) {
            cooldownSkill--;
        }
    }

    public void dead() {
        if (countdownSecond != 0) {
            img = Sprite.doll_dead.getFxImage();
            countdownSecond--;
        }
        else {
            img = null;
            numberOfMonster--;
            removeList.add(this);
        }
    }
}
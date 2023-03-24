package entities;

import graphics.Sprite;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import Bomber.Game;
import static Bomber.Game.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Minvo extends Monster {
    private int dir = 0;
    private int lastMove = -1;
    private long IntervalChangeDirection = 2100000000;
    private long lastChangeDirection = 0;

    public Minvo(int x, int y, Image img) {
        super( x, y, img);
        dir = getDirection();
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

    private int getDirection() {
        int u = block(x);
        int v = block(y);
        Random random = new Random();
        int order = (random.nextInt() + 132132) % 2;
        if (order == 0) {
            for (int i = 0; i < 4; ++i) {
                if (checkGrid(u + DIR_X[i], v + DIR_Y[i]) == true) {
                    return i;
                }
            }
        } else {
            for (int i = 3; i >= 0; --i) {
                if (checkGrid(u + DIR_X[i], v + DIR_Y[i]) == true) {
                    return i;
                }
            }
        }
        return 0;
    }

    public int block(int x) {
        return (x + 16 + Sprite.SCALED_SIZE - 1) / Sprite.SCALED_SIZE - 1;
    }

    public boolean checkGrid(int block_x, int block_y) {
        if (block_x < 0 || block_y < 0 || block_x >= WIDTH || block_y >= HEIGHT) {
            return false;
        }

        Monster temp = new Monster(block_x, block_y, null);

        for (Entity entity : entities) {
            if (entity instanceof Brick || entity instanceof Wall) {
                if (temp.isCollide(entity)) {
                    return false;
                }
            }
        }

        for (Entity entity : stillObjects) {
            if (entity instanceof Brick || entity instanceof Wall) {
                if (temp.isCollide(entity)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void move() {
        if (dir == LEFT) {
            if (spriteCount == 0) img = Sprite.minvo_left1.getFxImage();
            else if (spriteCount == 1) img = Sprite.minvo_left2.getFxImage();
            else if (spriteCount == 2) img = Sprite.minvo_left3.getFxImage();
        }
        else {
            if (spriteCount == 0) img = Sprite.minvo_right1.getFxImage();
            else if (spriteCount == 1) img = Sprite.minvo_right2.getFxImage();
            else if (spriteCount == 2) img = Sprite.minvo_right3.getFxImage();
        }

        if (x == block(x) * Sprite.SCALED_SIZE && y == block(y) * Sprite.SCALED_SIZE) {
            int u = block(x);
            int v = block(y);
            if (checkGrid(u + DIR_X[dir], v + DIR_Y[dir]) == false) {
                dir = getDirection();
            }
        }

        x += DIR_X[dir];
        y += DIR_Y[dir];
    }

    public void dead() {
        if (countdownSecond != 0) {
            if (countdownSecond > 10) img = Sprite.minvo_dead.getFxImage();
            else if (countdownSecond > 8) img = Sprite.mob_dead1.getFxImage();
            else if (countdownSecond > 3) img = Sprite.mob_dead2.getFxImage();
            else img = Sprite.mob_dead3.getFxImage();
            countdownSecond--;
        }
        else {
            numberOfMonster--;
            removeList.add(this);
        }
    }
}
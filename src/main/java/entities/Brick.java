package entities;

import graphics.Sprite;
import javafx.scene.image.Image;
import Bomber.Game;

public class Brick extends Entity {
    public boolean isBreak = false;
    public long endAnimation;
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    public void break_brick() {
        endAnimation = System.nanoTime() + 500000000;
        lastSpriteChange = System.nanoTime();
        IntervalSpriteChange = 500000000 / 3;
        isBreak = true;
    }
    @Override
    public void update() {
        if( !isBreak ) return;
        spriteChange();
        if( spriteCount == 0 ) img = Sprite.brick_exploded.getFxImage();
        else if( spriteCount == 1 ) img = Sprite.brick_exploded1.getFxImage();
        else if( spriteCount == 2 ) img = Sprite.brick_exploded2.getFxImage();
        if( System.nanoTime() > endAnimation ) {
            Game.removeList.add(this);
        }
    }
}

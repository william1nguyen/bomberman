package entities;

import graphics.Sprite;
import javafx.scene.image.Image;

public class Flame extends Entity {

    public Sprite[] flames;
    public Flame(int x, int y, Image img , Sprite[] flames ) {
        super(x, y, img);
        this.flames = flames;
    }

    @Override
    public void update() {
        img = flames[spriteCount].getFxImage();
    }
}

package entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import static Bomber.Game.bomber;

public class Monster extends Entity {
    public int direct;
    public long countdownSecond = 20;

    public Monster(int x, int y, Image img) {
        super( x, y, img);
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public void update(){}
    public void move(){}
    public void dead() {}
    @Override
    public void setDead() {
        this.isDead = true;
    }
}
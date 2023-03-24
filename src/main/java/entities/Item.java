package entities;

import graphics.Sprite;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.awt.*;

public class Item extends Entity {
    public Item(int x, int y, Image img) {
        super( x, y, img);
        this.solidArea = new Rectangle(10 , 10 , Sprite.SCALED_SIZE - 20 , Sprite.SCALED_SIZE - 20 );
    }
    public void update(){}
}
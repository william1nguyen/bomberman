package entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class FlameItem extends Item {
    public FlameItem(int x, int y, Image img) {
        super( x, y, img);
    }

    public void update(){}
}
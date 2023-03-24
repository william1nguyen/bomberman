package entities;

import graphics.Sprite;
import javafx.scene.image.Image;
import Bomber.Game;
import static entities.Bomb.*;
import static Bomber.Game.*;

public class Portal extends Entity {

    public Portal(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        if (numberOfMonster == 0) {
            collideHandler(bomber);
        }
    }



    @Override
    public void collideHandler(Entity entity) {
        if (entity instanceof Bomber) {
            if (this.isCollide(entity)) {
                if (idLevel < limitLevel - 1) {
                    ++idLevel;
                    resetBomber();
                    createMap();
                    buildEntities();
                    gameState = "Load Stage";
                    countdownStage = 50;
                } else if (idLevel == limitLevel - 1) {
                    gameState = "Game is over ! You win !";
                }
            }
        }
    }
}

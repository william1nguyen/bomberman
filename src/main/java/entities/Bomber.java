package entities;

import graphics.Sprite;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.List;
import Bomber.Game;
import static Bomber.Game.*;

public class Bomber extends Entity {
    public int STEP_SIZE = 7;
    public int dir;
    public boolean pressed = false;
    public boolean upPressed = false , downPressed = false , leftPressed = false , rightPressed = false , spacePressed = false;
    public long IntervalMove = 1000000000 / 20;
    public long lastMove = 0;
    public long endAnimation;
    public Bomber(int x, int y, Image img) {
        super( x, y, img);
        solidArea = new Rectangle( 4 , 12 , 18 , 20 );
    }

    private boolean moved = false;
    @Override
    public void update() {
        if( isDead ) {
            dead();
        }
        else {
            move();
            collide();
        }
        spriteChange();
    }

    @Override
    public void setDead() {
        isDead = true;
        endAnimation = System.nanoTime() + IntervalSpriteChange * 3;
        spriteCount = 0;
        lastSpriteChange = System.nanoTime();
        Game.play_wav( Game.bomber_die );
    }

    @Override
    public void collideHandler(Entity entity) {
        if( System.nanoTime() - this.endAnimation < 2000000000 && ( entity instanceof Monster || entity instanceof Bomb ) ){
            return;
        }
        if( entity instanceof Wall || entity instanceof Brick ) {
            if( this.isCollide(entity) ) {
                int curX = x , curY = y;
                boolean fixCollide = false;
                for(int i = -5; i <= 5; i++) {
                    this.x = curX + i;
                    if( !this.isCollide(entity) ) {
                        fixCollide = true;
                        break;
                    }
                }
                if(!fixCollide) {
                    this.x = curX;
                    for(int i = -5; i <= 5; i++) {
                        this.y = curY + i;
                        if( !this.isCollide(entity) ) {
                            fixCollide = true;
                            break;
                        }
                    }
                    if(!fixCollide) this.y = curY;
                }
            }

            while ( this.isCollide(entity) ){
                x -= DIR_X[dir];
                y -= DIR_Y[dir];
            }
        }

        if( entity instanceof Monster && this.isCollide(entity) ) {
            this.setDead();
        }

        if( entity instanceof Bomb && this.isCollide(entity) ) {
            if( ((Bomb) entity).getOut ) {
                while ( this.isCollide(entity) ){
                    x -= DIR_X[dir];
                    y -= DIR_Y[dir];
                }
            }
        }

        if( entity instanceof FlameItem && this.isCollide(entity) ) {
            Bomb.flameLength++;
            Game.removeList.add(entity);
        }

        if( entity instanceof SpeedItem && this.isCollide(entity) ) {
            STEP_SIZE ++;
            Game.removeList.add(entity);
        }

        if( entity instanceof BombItem && this.isCollide(entity) ) {
            Bomb.bombCapacity++;
            Game.removeList.add(entity);
        }
    }

    public void move() {
        moved = false;
        if( rightPressed ) {
            dir = RIGHT;
            if( spriteCount == 0 ) img = Sprite.player_right.getFxImage();
            else if( spriteCount == 1 ) img = Sprite.player_right_1.getFxImage();
            else if( spriteCount == 2 ) img = Sprite.player_right_2.getFxImage();
        }
        if( leftPressed ) {
            dir = LEFT;
            if( spriteCount == 0 ) img = Sprite.player_left.getFxImage();
            else if( spriteCount == 1 ) img = Sprite.player_left_1.getFxImage();
            else if( spriteCount == 2 ) img = Sprite.player_left_2.getFxImage();
        }
        if( downPressed ) {
            dir = DOWN;
            if( spriteCount == 0 ) img = Sprite.player_down.getFxImage();
            else if( spriteCount == 1 ) img = Sprite.player_down_1.getFxImage();
            else if( spriteCount == 2 ) img = Sprite.player_down_2.getFxImage();
        }
        if( upPressed ) {
            dir = UP;
            if( spriteCount == 0 ) img = Sprite.player_up.getFxImage();
            else if( spriteCount == 1 ) img = Sprite.player_up_1.getFxImage();
            else if( spriteCount == 2 ) img = Sprite.player_up_2.getFxImage();
        }

        pressed = downPressed || upPressed || rightPressed || leftPressed;
        if( pressed && System.nanoTime() - lastMove > IntervalMove ){
            x += DIR_X[dir] * STEP_SIZE;
            y += DIR_Y[dir] * STEP_SIZE;
            lastMove = System.nanoTime();
            moved = true;
        }
    }
    public void dead(){
        if( spriteCount == 0 ) img = Sprite.player_dead1.getFxImage();
        else if( spriteCount == 1 ) img = Sprite.player_dead2.getFxImage();
        else if( spriteCount == 2 ) img = Sprite.player_dead3.getFxImage();

        if( endAnimation < System.nanoTime() ) {
            if (bomberLifeRemain > 0) {
                isDead = false;
                x = Sprite.SCALED_SIZE;
                y = Sprite.SCALED_SIZE;
                img = Sprite.player_right.getFxImage();
            } else {
                gameState = "Game is over ! You lose !";
            }
            bomberLifeRemain--;
        }
    }

}
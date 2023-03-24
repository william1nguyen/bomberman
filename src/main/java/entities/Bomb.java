package entities;

import graphics.Sprite;
import javafx.scene.image.Image;
import Bomber.Game;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Entity {
    boolean exceededTimeLimit = false;
    private long timeLimit;
    public static int flameLength = 1;
    public List<Flame> curFlame;
    public boolean getOut = false;
    public static int bombCapacity = 2;
    public static int bombCount;
    public Bomb(int x, int y, Image img) {
        super(x, y, img);
        timeLimit = System.nanoTime() + 2000000000;
        curFlame = new ArrayList<>();
        bombCount++;
    }

    @Override
    public void update() {
        if( !this.isCollide(Game.bomber) ) {
            this.getOut = true;
        }
        spriteChange();
        if( spriteCount == 0 ) img = Sprite.bomb.getFxImage();
        else if( spriteCount == 1 ) img = Sprite.bomb_1.getFxImage();
        else if( spriteCount == 2 ) img = Sprite.bomb_2.getFxImage();
        explode();
        if( System.nanoTime() > timeLimit + 500000000 ) {
            Game.removeList.add(this);
            Game.removeList.addAll( curFlame );
            bombCount--;
        }
    }

    public Sprite[] new_flame_sprite(Sprite x , Sprite x1 , Sprite x2) {
        Sprite[] flames = new Sprite[3];
        flames[0] = x;
        flames[1] = x1;
        flames[2] = x2;
        return flames;
    }

    public boolean is_explode_collide(int block_x , int block_y) {
        for(Entity entity : Game.stillObjects) {
            if( entity.x/32 == block_x && entity.y/32 == block_y ) {
                if( entity instanceof Wall ) {
                    return true;
                }
            }
        }

        for(Entity entity : Game.entities) {
            if( entity.x/32 == block_x && entity.y/32 == block_y ) {
                if( entity instanceof Brick ) {
                    ((Brick) entity).break_brick();
                    return true;
                }
            }
        }

        return false;
    }

    public void explode() {
        if( System.nanoTime() > timeLimit ) {
            if( !this.getOut ) {
                Game.bomber.setDead();
            }
            IntervalSpriteChange = 1000000000 / 6;
            if( !exceededTimeLimit ) {
                exceededTimeLimit = true;
                spriteCount = 0;
                Game.play_wav( Game.bomb_bang );
                // left
                boolean noLeft = false;
                for(int i = 0 ; i < flameLength - 1; i++) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_horizontal ,
                            Sprite.explosion_horizontal1 ,
                            Sprite.explosion_horizontal2 );
                    Flame newFlame = new Flame( this.x / 32 - i  - 1, this.y / 32 , null , newFlameSprite );
                    if( is_explode_collide(newFlame.x/32, newFlame.y/ 32) ) {
                        noLeft = true;
                        break;
                    }
                    curFlame.add( newFlame );
                }
                if( !noLeft ) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_horizontal_left_last ,
                            Sprite.explosion_horizontal_left_last1 ,
                            Sprite.explosion_horizontal_left_last2 );
                    Flame newFlame = new Flame( this.x/32 - flameLength, this.y/32 , null , newFlameSprite );
                    if( !is_explode_collide(newFlame.x/32, newFlame.y/32 ) ) {
                        curFlame.add(newFlame);
                    }
                }
                // right
                boolean noRight = false;
                for(int i = 0 ; i < flameLength - 1; i++) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_horizontal ,
                            Sprite.explosion_horizontal1 ,
                            Sprite.explosion_horizontal2 );
                    Flame newFlame = new Flame( this.x / 32 + i + 1, this.y / 32 , null , newFlameSprite );
                    if( is_explode_collide(newFlame.x/32 , newFlame.y/32 ) ) {
                        noRight = true;
                        break;
                    }
                    curFlame.add( newFlame );
                }
                if( !noRight ) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_horizontal_right_last ,
                            Sprite.explosion_horizontal_right_last1 ,
                            Sprite.explosion_horizontal_right_last2 );
                    Flame newFlame = new Flame( this.x/32 + flameLength , this.y/32 , null , newFlameSprite );
                    if( !is_explode_collide(newFlame.x/32, newFlame.y/32 ) ) {
                        curFlame.add(newFlame);
                    }
                }

                // up
                boolean noUp = false;
                for(int i = 0 ; i < flameLength - 1; i++) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_vertical ,
                            Sprite.explosion_vertical1 ,
                            Sprite.explosion_vertical2 );
                    Flame newFlame = new Flame( this.x / 32 , this.y / 32 - i - 1, null , newFlameSprite );
                    if( is_explode_collide(newFlame.x/32 , newFlame.y/32 ) ) {
                        noUp = true;
                        break;
                    }
                    curFlame.add( newFlame );
                }

                if( !noUp ) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_vertical_top_last ,
                            Sprite.explosion_vertical_top_last1 ,
                            Sprite.explosion_vertical_top_last2 );
                    Flame newFlame = new Flame( this.x / 32 , this.y / 32 - flameLength, null , newFlameSprite );
                    if( !is_explode_collide(newFlame.x/32 , newFlame.y/32 ) ) {
                        curFlame.add( newFlame );
                    }
                }

                // down
                boolean noDown = false;
                for(int i = 0 ; i < flameLength - 1; i++) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_vertical ,
                            Sprite.explosion_vertical1 ,
                            Sprite.explosion_vertical2 );
                    Flame newFlame = new Flame( this.x / 32 , this.y / 32 + i + 1 , null , newFlameSprite );
                    if( is_explode_collide(newFlame.x/32 , newFlame.y/32) ) {
                        noDown = true;
                        break;
                    }
                    curFlame.add( newFlame );
                }
                if( !noDown ) {
                    Sprite[] newFlameSprite = new_flame_sprite(
                            Sprite.explosion_vertical_down_last ,
                            Sprite.explosion_vertical_down_last1 ,
                            Sprite.explosion_vertical_down_last2 );
                    Flame newFlame = new Flame( this.x / 32 , this.y / 32 + flameLength, null , newFlameSprite );
                    if( !is_explode_collide(newFlame.x/32 , newFlame.y/32 ) ) {
                        curFlame.add( newFlame );
                    }
                }

                for(Flame flame : curFlame) {
                    for(Entity entity : Game.entities) {
                        if( (entity instanceof Bomber || entity instanceof  Monster ) &&
                                flame.isCollide(entity) ) {
                            entity.setDead();
                            Game.play_wav( Game.enemy_die );
                        }
                        if( entity instanceof Bomb && flame.isCollide(entity) ) {
                            ((Bomb) entity).timeLimit = System.nanoTime();
                        }
                    }
                }
                Game.addList.addAll( curFlame );
            }

            for(Flame flame : curFlame) {
                flame.spriteCount = this.spriteCount;
            }

            if( spriteCount == 0 ) img = Sprite.bomb_exploded.getFxImage();
            else if( spriteCount == 1 ) img = Sprite.bomb_exploded1.getFxImage();
            else if( spriteCount == 2 ) img = Sprite.bomb_exploded2.getFxImage();
        }
    }
}

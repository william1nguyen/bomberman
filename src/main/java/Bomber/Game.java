package Bomber;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import entities.*;
import graphics.Sprite;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Game extends Application {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    public static final int FPS = 60;
    private GraphicsContext gc;
    private Canvas canvas;
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> removeList = new ArrayList<>();
    public static List<Entity> addList = new ArrayList<>();
    public Group root = new Group();
    public Label labelLife = new Label();
    public Label labelStage = new Label();
    public Label labelMonster = new Label();
    public Label currentStage = new Label();
    public Label msg = new Label();
    public Label restartMsg = new Label();
    public Image image;
    public ImageView imageView;

    private long Interval = 1000000000 / FPS;
    private long lastUpdate = 0;
    public static int countdownStage = 0;
    public static int numberOfMonster = 0;
    public static int bomberLifeRemain = 5;
    public static String gameState = "Menu";
    public static Bomber bomber = new Bomber(1 , 1 , null);
    public static File bomb_bang , backgroundMusic , bomber_die , enemy_die, item ;
    public static int idLevel = 0;
    public static int limitLevel = 5;
    public static String level[] = {
            "src/main/resources/level1.txt",
            "src/main/resources/level2.txt",
            "src/main/resources/level3.txt",
            "src/main/resources/level4.txt",
            "src/main/resources/level5.txt",
    };

    public static void main(String[] args) {
        Application.launch(Game.class);
    }

    public static class Pair {
        public int x;
        public int y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int distance(Pair other) {
            int d = (x - other.x) * (x - other.x);
            int e = (y - other.y) * (y - other.y);
            return (int)Math.sqrt(d + e);
        }
    }

    public static void play_wav(File file) {
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * (HEIGHT + 1));
        gc = canvas.getGraphicsContext2D();

        // Tao titles
        stage.setTitle("Bomberman");

        // Tao root container

        Scene scene = new Scene(root);
        root.getChildren().add(canvas);
        stage.setTitle("BomberMan");

        bomb_bang = new File("bomb_bang.wav");
        backgroundMusic = new File("bgm.mp3");
        bomber_die = new File("bomber_die.wav");
        enemy_die = new File("enemy_die.wav");
        item = new File("item.wav");
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() != null) {
                    if (gameState == "Menu") {
                        gameState = "Load Stage";
                        countdownStage = 50;
                    }
                }
                switch ( keyEvent.getCode() ) {
                    case UP :bomber.upPressed = true ; break;
                    case DOWN :bomber.downPressed = true; break;
                    case RIGHT :bomber.rightPressed = true; break;
                    case LEFT : bomber.leftPressed = true; break;
                    case M : break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch ( keyEvent.getCode() ) {
                    case UP : bomber.upPressed = false ; break;
                    case DOWN : bomber.downPressed = false; break;
                    case RIGHT : bomber.rightPressed = false; break;
                    case LEFT : bomber.leftPressed = false; break;
                    case SPACE :
                        if (gameState == "continue") {
                            int bomber_block_x = (bomber.x - 16) / 32 + 1;
                            int bomber_block_y = (bomber.y - 16) / 32 + 1;
                            if (Bomb.bombCount < Bomb.bombCapacity) {
                                boolean valid = true;
                                for( Entity entity : entities ) {
                                    if( entity instanceof Bomb &&
                                            entity.x == bomber_block_x*32 && entity.y == bomber_block_y * 32 ) {
                                        valid = false;
                                    }
                                }
                                if( valid ) {
                                    entities.add(new Bomb(bomber_block_x, bomber_block_y, Sprite.bomb.getFxImage()));
                                }
                            }
                        }
                        break;
                    case R: gameRestart(); break;
                    case Q: System.exit(1); break;
                }
            }
        });

        Media media = new Media(backgroundMusic.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(1000000);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                    render_update();
                    mediaPlayer.play();
            }
        };
        timer.start();

        stage.setScene(scene);
        stage.show();
    }

    public static void resetBomber() {
        bomber = new Bomber(1, 1, null);
        Bomb.bombCapacity = 2;
        Bomb.flameLength = 1;
        Bomb.bombCount = 0;
    }

    public void gameRestart() {
        entities.clear();
        stillObjects.clear();
        idLevel = 0;
        numberOfMonster = 0;
        bomberLifeRemain = 5;
        resetBomber();
        gameState = "Start";
    }

    public static void buildEntities() {
        try {
            File file = new File(level[idLevel]);
            Scanner scanner = new Scanner(file);

            for (int i = 0; i < HEIGHT; i++) {
                String readMap = scanner.nextLine();

                for (int j = 0; j < WIDTH; j++) {
                    Entity object = null;
                    switch (readMap.charAt(j)) {
                        case 'p':
                            bomber = new Bomber(j, i, Sprite.player_right.getFxImage());
                            break;
                        case '1': numberOfMonster++; entities.add(new Balloom(j, i, Sprite.balloom_right1.getFxImage())); break;
                        case '2': numberOfMonster++; entities.add(new Oneal(j, i, Sprite.oneal_right1.getFxImage())); break;
                        case '3': numberOfMonster++; entities.add(new Minvo(j, i, Sprite.minvo_right1.getFxImage())); break;
                        case '4': numberOfMonster++; entities.add(new Doll(j, i, Sprite.doll_right1.getFxImage())); break;
                        case '5': numberOfMonster++; entities.add(new Kondoria(j, i, Sprite.kondoria_right1.getFxImage())); break;
                        case 'f': {
                            entities.add(new FlameItem(j, i, Sprite.powerup_flames.getFxImage()));
                            entities.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        }
                        case 's': {
                            entities.add(new SpeedItem(j, i, Sprite.powerup_speed.getFxImage()));
                            entities.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        }
                        case 'b': {
                            entities.add(new BombItem(j, i, Sprite.powerup_bombs.getFxImage()));
                            entities.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        }
                        case '#': stillObjects.add(new Wall(j, i, Sprite.wall.getFxImage())); break;
                        case '*': entities.add(new Brick(j, i, Sprite.brick.getFxImage())); break;
                        case 'x': {
                            stillObjects.add(new Portal(j, i, Sprite.portal.getFxImage()));
                            entities.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        }
                    }
                }
            }
            entities.add(bomber);

        } catch (Exception e) {

        };
    }

    public static void createMap() {
        entities.clear();
        stillObjects.clear();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Entity object;
                object = new Grass(i, j, Sprite.grass.getFxImage());
                if (j == 0 || j == HEIGHT - 1 || i == 0 || i == WIDTH - 1) {
                    object = new Wall(i, j, Sprite.wall.getFxImage());
                }
                stillObjects.add(object);
            }
        }
    }

    int count = 0;
    long pre_count = 0;

    public void render_update() {
        if (gameState == "Game is over ! You lose !") {
            root.getChildren().remove(msg);
            root.getChildren().remove(restartMsg);

            msg.setText(gameState);
            msg.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
            msg.setLayoutX(180);
            msg.setLayoutY(150);

            restartMsg.setText("Press R to restart");
            restartMsg.setFont(Font.font("Verdana", FontWeight.BOLD, 35));
            restartMsg.setLayoutX(310);
            restartMsg.setLayoutY(250);

            root.getChildren().add(msg);
            root.getChildren().add(restartMsg);
            return;
        } else {
            root.getChildren().remove(msg);
            root.getChildren().remove(restartMsg);
        }

        if (gameState == "Game is over ! You win !") {
            root.getChildren().remove(msg);
            root.getChildren().remove(restartMsg);

            msg.setText(gameState);
            msg.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
            msg.setLayoutX(180);
            msg.setLayoutY(150);

            restartMsg.setText("Press R to restart");
            restartMsg.setFont(Font.font("Verdana", FontWeight.BOLD, 35));
            restartMsg.setLayoutX(310);
            restartMsg.setLayoutY(250);

            root.getChildren().add(msg);
            root.getChildren().add(restartMsg);
            return;
        } else {
            root.getChildren().remove(msg);
            root.getChildren().remove(restartMsg);
        }

        if (gameState == "Menu") {
            try {
                root.getChildren().remove(imageView);
                root.getChildren().remove(msg);

                image = new Image(new FileInputStream("src/main/resources/wallpaper.jpg"));
                imageView = new ImageView(image);

                imageView.setX(0);
                imageView.setY(0);

                imageView.setFitWidth(WIDTH * Sprite.SCALED_SIZE);
                imageView.setFitHeight((HEIGHT + 1) * Sprite.SCALED_SIZE);

                root.getChildren().add(imageView);

                msg.setText("Press any key to start and Q to quit !");
                msg.setFont(Font.font("Verdana", FontWeight.BOLD, 35));
                msg.setLayoutX(150);
                msg.setLayoutY(150);

                root.getChildren().add(msg);
            } catch (Exception e) {};
            return;
        } else {
            root.getChildren().remove(imageView);
            root.getChildren().remove(msg);
        }

        if (gameState == "Start") {
            createMap();
            buildEntities();
            gameState = "continue";
            return;
        }

        if (gameState == "Load Stage") {
            root.getChildren().remove(currentStage);

            currentStage.setText("Stage " + String.valueOf(idLevel + 1));
            currentStage.setFont(new Font(50));
            currentStage.setLayoutX(440);
            currentStage.setLayoutY(170);

            root.getChildren().add(currentStage);

            if (countdownStage == 0) {
                if (idLevel == 0) {
                    gameState = "Start";
                } else {
                    gameState = "continue";
                }
            }

            if (countdownStage > 0) {
                countdownStage--;
            }

            return;
        } else {
            root.getChildren().remove(currentStage);
        }

        if (gameState != "continue") {
            return;
        }

        root.getChildren().remove(imageView);
        root.getChildren().remove(currentStage);
        root.getChildren().remove(labelLife);
        root.getChildren().remove(labelStage);

        // Tao text Level
        String level = String.valueOf(idLevel + 1);
        labelStage.setFont(new Font(23));
        labelStage.setText("Stage : " + level);
        labelStage.setLayoutX(30);
        labelStage.setLayoutY(415);
        root.getChildren().add(labelStage);

        // Tao text HP
        String HP = String.valueOf(bomberLifeRemain);
        labelLife.setFont(new Font(23));
        labelLife.setText("Life : " + HP);
        labelLife.setLayoutX(180);
        labelLife.setLayoutY(415);
        root.getChildren().add(labelLife);

        // Tao text Monster Left
        root.getChildren().remove(labelMonster);
        String monsterRemain = String.valueOf(numberOfMonster);
        labelMonster.setFont(new Font(23));
        labelMonster.setText("Monster Left : " + monsterRemain);
        labelMonster.setLayoutX(320);
        labelMonster.setLayoutY(415);
        root.getChildren().add(labelMonster);

        count++;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        try {
            stillObjects.forEach(g -> g.render(gc));
            stillObjects.forEach(Entity::update);

            entities.forEach(g -> g.render(gc));
            entities.forEach(Entity::update);
        } catch (Exception e) {};

        entities.removeAll( removeList );
        entities.addAll( addList );
        addList.clear();
        removeList.clear();
        lastUpdate = System.nanoTime();

        if( System.nanoTime() - pre_count > 1000000000 ) {
            count = 0;
            pre_count = System.nanoTime();
        }
    }
}
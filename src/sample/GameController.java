package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Canvas img;
    @FXML private Button gotoNextLevel;

    private static BooleanProperty gameFinished = new SimpleBooleanProperty(false);

    private static GraphicsContext gc;

    private static Alert alert;
    private static Alert alertFinish;

    private static HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();
    private static String keyCode;

    private static Image gHero;
    private static Image gTile;
    private static Image gWall;
    private static Image gGoal;
    private static Image gBox;

    private static final int sizeX = 32;
    private static final int sizeY = 32;

    private Main application;

    public void setApp(Main application){
        this.application = application;
    }

    @FXML private void keyPress(KeyEvent event) {
        String codeString = event.getCode().toString();
        if (!currentlyActiveKeys.containsKey(codeString)) {
            currentlyActiveKeys.put(codeString, true);
            keyCode = codeString;
            //System.out.println("key pressed");
        }
    }

    @FXML private void keyRelease(KeyEvent event) {
        currentlyActiveKeys.remove(event.getCode().toString());
        //System.out.println("key release");
    }

    public void processExitGame() {
        if (application == null){
            System.out.print("there is null buddy");
            return;
        }
        //gameFinished.set(false);
        application.exitGame();
    }

    public void processRestartLevel() {
        System.out.println("Restarting level");
        if (application == null){
            System.out.print("there is null buddy");
            return;
        }
        //gameFinished.set(false);
        application.restartLevel(application.getGameLevelToLoad());
    }

    public void gotoNextLevel() {
        System.out.println("going to next level!");
        application.setGameLevelToLoad(application.getGameLevelToLoad()+1);
        application.nextLevel();
        gameFinished.set(false);
    }

    public static String getKeyCode() {
        return keyCode;
    }

    public static void setKeyCode(String keyCode) {
        GameController.keyCode = keyCode;
    }

    public static void winWindowUnderLevel2() {
        alert.show();
        gameFinished.set(true);
    }

    public static void winWindowFinish() {
        alertFinish.show();
        gameFinished.set(false);
    }

    public static void tickAndRender(char[][] temp_mapa) {

        for(int i = 0; i < temp_mapa.length; i++) {
            for(int j = 0; j < temp_mapa[i].length; j++) {
                if(temp_mapa[i][j] == 'X') {
                    gc.drawImage(gWall, sizeX + j*sizeX, sizeY + i*sizeY);
                } else if(temp_mapa[i][j] == '.') {
                    gc.drawImage(gTile, sizeX + j*sizeX, sizeY + i*sizeY);
                } else if(temp_mapa[i][j] == 'M') {
                    gc.drawImage(gHero, sizeX + j*sizeX, sizeY + i*sizeY);
                } else if(temp_mapa[i][j] == 'J') {
                    gc.drawImage(gBox, sizeX + j*sizeX, sizeY + i*sizeY);
                } else if(temp_mapa[i][j] == 'G') {
                    gc.drawImage(gGoal, sizeX + j*sizeX, sizeY + i*sizeY);
                }
            }
        }
    }

    private static void gameFinished() {
        alertFinish = new Alert(Alert.AlertType.INFORMATION);
        alertFinish.setTitle("CONGRATULATIONS!");
        alertFinish.setHeaderText(null);
        alertFinish.setContentText("You won and finished the game! You achieved nothing!");
    }

    private static void levelFinished() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CONGRATULATIONS!");
        alert.setHeaderText(null);
        alert.setContentText("You won! Get ready for next level!");
    }

    private static void loadGraphics() {
        gHero = new Image("file:hero.jpg");
        gBox = new Image("file:box.jpg");
        gTile = new Image("file:tile.jpg");
        gWall = new Image("file:wall.jpg");
        gGoal = new Image("file:goal.jpg");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gameFinished();
        levelFinished();
        loadGraphics();
        gc = img.getGraphicsContext2D();
        img.setFocusTraversable(true);
        gotoNextLevel.visibleProperty().bind(gameFinished);
    }
}

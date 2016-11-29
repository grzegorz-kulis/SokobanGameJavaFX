package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private Stage stage;

    private static ArrayList<String> gameLevels;

    private static Integer gameLevelToLoad = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            stage = primaryStage;
            stage.setTitle("Sokoban v1.0");
            stage.setResizable(false);
            gotoStart();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getGameLevelToLoad() {
        return gameLevelToLoad;
    }

    public void setGameLevelToLoad(Integer gameLevelToLoad) {
        this.gameLevelToLoad = gameLevelToLoad;
    }

    public void exitGame() {
        gotoStart();
    }

    public void newGame() {
        gotoNewGame();
    }

    public void restartLevel(Integer restart) { gotoRestartLevel(restart);   }

    public void nextLevel() {
        gotoNextLevel();
    }

    private void gotoRestartLevel(Integer restart) {
        try {
            GameController newGame = (GameController) replaceSceneContent("board.fxml");
            newGame.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            NewGame game = new NewGame(gameLevels.get(restart), restart);
            game.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoNextLevel() {
        try {
            GameController newGame = (GameController) replaceSceneContent("board.fxml");
            newGame.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            NewGame game = new NewGame(gameLevels.get(gameLevelToLoad), gameLevelToLoad);
            game.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoStart() {
        try {
            StartController start = (StartController) replaceSceneContent("start.fxml");
            start.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoNewGame() {
        gameLevelToLoad = 0;
        try {
            GameController newGame = (GameController) replaceSceneContent("board.fxml");
            newGame.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            NewGame game = new NewGame(gameLevels.get(0), gameLevelToLoad);
            game.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Pane page;
        try {
            page = (Pane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private static void loadGameLevels() {
        gameLevels = new ArrayList<String>();
        gameLevels.add("level1.txt");
        gameLevels.add("level2.txt");
        gameLevels.add("level3.txt");
    }

    public static void main(String[] args) {
        loadGameLevels();
        launch(args);
    }
}

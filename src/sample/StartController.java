package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML private Button newGame;
    @FXML private Button chooseLevel;
    @FXML private Button levelBuilder;
    @FXML private Button solver;
    @FXML private Button exitGame;

    private Alert exitConfirmation;

    private Main application;

    public void setApp(Main application){
        this.application = application;
    }

    @FXML private void newGame(ActionEvent event) throws IOException {
        application.newGame();
    }

    @FXML private void chooseLevel(ActionEvent event) {
        System.out.println("choose level");
    }

    @FXML private void levelBuilder(ActionEvent event) {
        System.out.println("level builder");
    }

    @FXML private void solver(ActionEvent event) {
        System.out.println("solver");
    }

    @FXML private void exitGame(ActionEvent event) {
        Stage stage = (Stage) exitGame.getScene().getWindow();
        Optional<ButtonType> result = exitConfirmation.showAndWait();
        if (result.get() == ButtonType.OK){
            stage.close();
        } else {
            System.out.println("not exiting");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirmation.setTitle("Exiting game");
        exitConfirmation.setHeaderText("Are you sure you want to exit?");
    }
}

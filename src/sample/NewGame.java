package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;

public class NewGame extends Application {

    private static Board board;
    private static Player player;
    private static Box tempBox;

    private static char[][] mapa;
    private static char[][] temp_mapa;

    private static HashMap<String, Box> boxHashMap = new HashMap<>();
    private static LinkedList<Goal> goalLinkedList = new LinkedList<>();
    private static StringBuilder goalHash = new StringBuilder();

    private static GameController gameController;

    private static Integer gameLevel = 0;

    private static AnimationTimer anim;
    private static Boolean gameWon;

    public NewGame(String level, Integer gameLevel) {

        gameController = new GameController();

        this.gameLevel = gameLevel;
        board = new Board(level);
        gameWon = new Boolean(false);

        try {
            board.openFile();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }

        player = board.getPlayer();
        boxHashMap = board.getBoxHashMap();
        goalHash = board.getGoalSB();
        mapa = board.getMapa();

        temp_mapa = deepCopy2DArray(mapa);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        anim = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //System.out.println("game is running!");
                gameController.tickAndRender(temp_mapa);

                if(gameWon) anim.stop();

                if (gameController.getKeyCode() == "LEFT"){
                    //System.out.println("left");
                    makeMove('l');
                    gameController.setKeyCode(null);
                }
                if (gameController.getKeyCode() == "RIGHT"){
                    //System.out.println("right");
                    makeMove('r');
                    gameController.setKeyCode(null);
                }
                if (gameController.getKeyCode() == "UP"){
                    //System.out.println("up");
                    makeMove('u');
                    gameController.setKeyCode(null);
                }
                if (gameController.getKeyCode() == "DOWN"){
                    //System.out.println("down");
                    makeMove('d');
                    gameController.setKeyCode(null);
                }
            }
        };
        anim.start();
    }

    public static Boolean getGameWon() {
        return gameWon;
    }

    public static void setGameWon(Boolean gameWon) {
        NewGame.gameWon = gameWon;
    }

    private static void makeMove(char c) {
        int moveX = 0, moveY = 0;

        switch (c) {
            case 'l': {
                moveX = 0;
                moveY = -1;
                break;
            }
            case 'r': {
                moveX = 0;
                moveY = 1;
                break;
            }
            case 'u': {
                moveX = -1;
                moveY = 0;
                break;
            }
            case 'd': {
                moveX = 1;
                moveY = 0;
                break;
            }
            default: {
                moveX = 0;
                moveY = 0;
                break;
            }
        }

        //here is a move, it handles all directions
        if(temp_mapa[player.getX()+moveX][player.getY()+moveY] == '.' || temp_mapa[player.getX()+moveX][player.getY()+moveY] == 'G') {
            if(mapa[player.getX()][player.getY()] == 'G') {
                temp_mapa[player.getX()][player.getY()] = 'G';
                player.setX(player.getX()+moveX);
                player.setY(player.getY()+moveY);
                temp_mapa[player.getX()][player.getY()] = 'M';
            } else {
                temp_mapa[player.getX()][player.getY()] = '.';
                player.setX(player.getX()+moveX);
                player.setY(player.getY()+moveY);
                temp_mapa[player.getX()][player.getY()] = 'M';
            }

        } else if(temp_mapa[player.getX()+moveX][player.getY()+moveY] == 'X') {
            System.out.println("Wall ahead!");

        } else if(temp_mapa[player.getX()+moveX][player.getY()+moveY] == 'J') {
            if(temp_mapa[player.getX()+2*moveX][player.getY()+2*moveY] != 'X' && temp_mapa[player.getX()+2*moveX][player.getY()+2*moveY] != 'J') {
                tempBox = new Box(player.getX()+moveX, player.getY()+moveY);

                if(mapa[player.getX()][player.getY()] == 'G') {
                    temp_mapa[player.getX()][player.getY()] = 'G';
                    player.setX(player.getX()+moveX);
                    player.setY(player.getY()+moveY);
                    temp_mapa[player.getX()][player.getY()] = 'M';
                    boxHashMap.remove(Integer.toString(tempBox.getX()) + Integer.toString(tempBox.getY()));
                    tempBox.setX(tempBox.getX()+moveX);
                    tempBox.setY(tempBox.getY()+moveY);
                    boxHashMap.put(Integer.toString(tempBox.getX()) + Integer.toString(tempBox.getY()), tempBox);
                    temp_mapa[tempBox.getX()] [tempBox.getY()] = 'J';
                } else {
                    temp_mapa[player.getX()][player.getY()] = '.';
                    player.setX(player.getX()+moveX);
                    player.setY(player.getY()+moveY);
                    temp_mapa[player.getX()][player.getY()] = 'M';
                    boxHashMap.remove(Integer.toString(tempBox.getX()) + Integer.toString(tempBox.getY()));
                    tempBox.setX(tempBox.getX()+moveX);
                    tempBox.setY(tempBox.getY()+moveY);
                    boxHashMap.put(Integer.toString(tempBox.getX()) + Integer.toString(tempBox.getY()), tempBox);
                    temp_mapa[tempBox.getX()][tempBox.getY()] = 'J';
                }
            } else {
                System.out.println("Wall or box is ahead!");
            }
        }
        checkWin(boxHashMap, goalHash);
    }

    private static boolean checkWin(HashMap<String, Box> hBox, StringBuilder sGoal) {
        List<String> sortedList = new ArrayList(hBox.keySet());
        Collections.sort(sortedList);

        StringBuilder sb = new StringBuilder();
        for(String x:sortedList) sb.append(x);

        if((sb.toString()).equals(sGoal.toString())) {
            System.out.println("You won!");
            gameWon = true;
            if(gameLevel < 2) {
                gameController.winWindowUnderLevel2();
            } else if(gameLevel == 2){
                gameController.winWindowFinish();
            }

            return true;
        } else return false;
    }

    private static char[][] deepCopy2DArray(char[][] temp) {
        if (temp == null) return null;

        final char[][] result = new char[temp.length][];
        for (int i = 0; i < temp.length; i++)
            result[i] = Arrays.copyOf(temp[i], temp[i].length);

        return result;
    }
}

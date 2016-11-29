package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Board {

    private int boardWidth;
    private int boardHeight;
    private int boardBoxes;
    private String file;

    private static char[] lineToArray;
    private static char[] firstLine;
    private char[][] mapa;

    private HashMap<String, Box> boxHashMap;
    private LinkedList<Goal> goalLinkedList;
    private StringBuilder goalSB;

    private Player player;
    private Box box;
    private Goal goal;

    private int counter = -1;

    public Board(String file) {
        boxHashMap = new HashMap<>();
        goalLinkedList = new LinkedList<>();
        goalSB = new StringBuilder();
        this.file = file;
    }

    private static int getMapHeight(char[] ch) {
        int h = Integer.parseInt(new String().copyValueOf(ch,0,2));
        //System.out.println("Map height is: " + h);
        return h;
    }

    private static int getMapWidth(char[] ch) {
        int w = Integer.parseInt(new String().copyValueOf(ch,3,2));
        //System.out.println("Map width is: " + w);
        return w;
    }

    private static int getMapBoxes(char[] ch) {
        int b = Integer.parseInt(new String().copyValueOf(ch,6,2));
        //System.out.println("Number of boxes is: " + b);
        return b;
    }

    public void openFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            //System.out.println(line);
            firstLine = line.toCharArray();
            boardHeight = getMapHeight(firstLine);
            boardWidth = getMapWidth(firstLine);
            boardBoxes = getMapBoxes(firstLine);

            mapa = new char[boardWidth][boardHeight];

            while (line != null) {
                sb.append(line);
                lineToArray = line.toCharArray();

                if(counter >= 0) {
                    for(int i = 0; i < lineToArray.length; i++) mapa[counter][i] = lineToArray[i];
                }
                //System.out.println(lineToArray);
                readTile(lineToArray, counter);

                sb.append(System.lineSeparator());
                line = br.readLine();
                counter++;
            }
            //String everything = sb.toString();
        }
    }

    private void readTile(char[] arr, int i) {
        for(int k = 0; k < arr.length; k++) {
            if(arr[k]=='M') {
                player = new Player(i,k);
                //hashStatePlayer += 'm' + Integer.toString(i) + Integer.toString(k);
                //System.out.println("Player address is: " + player.getX() + " , " + player.getY());
            } else if(arr[k]=='J') {
                box = new Box(i,k);
                //hashState += Integer.toString(i) + Integer.toString(k);
                boxHashMap.put(Integer.toString(i) + Integer.toString(k), box);
                //System.out.println("Box address is: " + box.getX() + " , " + box.getY());
            } else if(arr[k]=='G') {
                goal = new Goal(i,k);
                //goalHash += Integer.toString(i) + Integer.toString(k);
                goalSB.append(new String(Integer.toString(i) + Integer.toString(k)));
                goalLinkedList.add(goal);
                //System.out.println("Goal address is: " + goal.getX() + " , " + goal.getY());
            }
        }
    }

    public Player getPlayer() { return player; }

    public HashMap<String, Box> getBoxHashMap() {
        return boxHashMap;
    }

    public StringBuilder getGoalSB() {
        return goalSB;
    }

    public char[][] getMapa() { return mapa; }
}

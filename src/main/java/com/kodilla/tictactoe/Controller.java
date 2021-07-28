package com.kodilla.tictactoe;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Controller {

    private static final Random random = new Random();
    private static final Controller instance = new Controller();
    private List<MyButton> myButtons = new ArrayList<>();
    private char myChar = 0;
    private char compChar = 0;
    private char[][] array = new char[3][3];
    private int liczbaRuchow = 0;
    private Label status = new Label();
    private boolean gameOver = false;
    private boolean isHardOn = true;
    private Choose_O chooseO;
    private Choose_X chooseX;

    private Controller() {
    }

    public static Controller getInstance() {
        return instance;
    }

    void addButton(MyButton button) {
        myButtons.add(button);
    }
    void addChoose_X(Choose_X chooseX) {this.chooseX = chooseX;}
    void addChoose_O(Choose_O chooseO) {this.chooseO = chooseO;}

    public void click(MyButton button) {

        // validate move
        if (!validateMove(button)) {
            return;
        }

        // make player move
        makePlayerMove(button);

        // check if player won the game
        if (!gameOver) {
            checkIfPlayerWon();
        }

        // make computer move
        makeComputerMove();

        // check if computer won the game
        if (!gameOver) {
            checkIfComputerWon();
        }
    }

    public void setColor() {
        if (myChar == 'X') {
            chooseX.setStyle("-fx-border-color: #4BFAC0;" + "-fx-border-width: 3;" + "-fx-border-insets: 5;");
            chooseO.setStyle("-fx-border-color:transparent");
        } else if (myChar == 'O') {
            chooseO.setStyle("-fx-border-color: #4BFAC0;" + "-fx-border-width: 3;" + "-fx-border-insets: 5;");
            chooseX.setStyle("-fx-border-color:transparent");
        } else {
            chooseX.setStyle("-fx-border-color:transparent");
            chooseO.setStyle("-fx-border-color:transparent");
        }
    }

    public void setXChar(Choose_X chooseX) {
        if (liczbaRuchow != 0) {
            status.setText("Zakończ obecną rozgrywkę");
        } else {
            myChar = 'X';
            compChar = 'O';
            setColor();
        }
    }

    public void setOChar(Choose_O chooseO) {
        if (liczbaRuchow != 0) {
            status.setText("Zakończ obecną rozgrywkę");
        } else {
            myChar = 'O';
            compChar = 'X';
            setColor();
        }
    }

    public void newGame(NewGameButton nGmButton) {
        gameOver = false;
        myChar = 0;
        compChar = 0;
        setColor();
        array = new char[3][3];
        liczbaRuchow = 0;
        myButtons.stream()
                .forEach(el -> el.setChar(null));
        myButtons.stream()
                .forEach(el -> el.setFill(Color.web("#ADB3BC")));
        status.setText("Nowa gra - wybierz znak");
    }

    private void checkIfComputerWon() {
        if (endComp()) {
            status.setText("WYGRAŁ KOMPUTER!");
            gameOver = true;
        } else if (liczbaRuchow == 9) {
            status.setText("Brak możliwości ruchu - REMIS!");
            gameOver = true;
        } else {
            status.setText("Twój ruch!");
        }
    }

    private void makeComputerMove() {
        if (!isHardOn) {
            makeSimpleCompMove();
        } else {
            makeComplexCompMove();
        }
    }

    private void makeSimpleCompMove() {
        if (!gameOver) {

            List<MyButton> emptyButtons = myButtons.stream()
                    .filter(button -> button.getMyChar() == null)
                    .collect(Collectors.toList());
            MyButton button = emptyButtons.get(random.nextInt(emptyButtons.size()));
            array[button.getCol()][button.getRow()] = compChar;
            button.setFill(new ImagePattern(changeImage(compChar)));
            button.setChar(compChar);
            liczbaRuchow++;
        }
    }

    private void makeComplexCompMove() {
        //compMiddleMOve();
        //compCornerMove();
        checkCompRow();
    }

    private void checkIfPlayerWon() {
        if (endPlayer()) {
            status.setText("GRATULACJE ZWYCIĘŻYŁEŚ!");
            gameOver = true;
        } else if (liczbaRuchow == 9) {
            status.setText("Brak możliwości ruchu - REMIS!");
            gameOver = true;
        } else {
            status.setText("Ruch komputera!");
        }
    }

    private void makePlayerMove(MyButton button) {
        if (!gameOver) {
            array[button.getCol()][button.getRow()] = myChar;
            button.setFill(new ImagePattern(changeImage(myChar)));
            button.setChar(myChar);
            liczbaRuchow++;
        }
    }

    private boolean validateMove(MyButton button) {
        if (array[button.getCol()][button.getRow()] != 0 && !gameOver) {
            status.setText("Wrong move!");
            return false;
        } else if (myChar == 0 || compChar == 0) {
            status.setText("Nie wybrano znaku");
            return false;
        }
        return true;
    }

    public static Image changeImage(char myChar) {
        Image x = new Image("file:src/main/resources/X.png");
        Image o = new Image("file:src/main/resources/O.png");
        if (myChar == 'X') {
            return x;
        } else {
            return o;
        }
    }

    public boolean checkRow(char myChar) {
        int wymiar = array.length;
        for (int wiersz = 0; wiersz < wymiar; wiersz++) {
            boolean wygrana = true;
            for (int kolumna = 0; kolumna < wymiar; kolumna++) {
                if (array[wiersz][kolumna] != myChar){
                    wygrana = false;
                    break;
                }
            }
            if (wygrana) return true;
        }
        return false;
    }

    public boolean checkCol(char myChar) {
        int wymiar = array.length;
        for (int kolumna = 0; kolumna < wymiar; kolumna++) {
            boolean wygrana = true;
            for (int wiersz = 0; wiersz < wymiar; wiersz++) {
                if (array[wiersz][kolumna] != myChar){
                    wygrana = false;
                    break;
                }
            }
            if (wygrana) return true;
        }
        return false;
    }

    public boolean checkSkos1(char myChar) {
        int wymiar = array.length;
        for (int i = 0; i < wymiar; i++) {
            if (array[i][i] != myChar) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSkos2(char myChar) {
        int wymiar = array.length;
        int pom = 2;
        for (int i = 0; i < wymiar; i++) {
            if (array[i][pom] != myChar) {
                return false;
            }
            pom--;
        }
        return true;
    }

    public boolean endPlayer() {
        boolean end = false;
        if (checkRow(myChar) || checkCol(myChar) || checkSkos1(myChar) || checkSkos2(myChar)) {
            end = true;
        }
        return end;
    }

    public boolean endComp() {
        boolean end = false;
        if (checkRow(compChar) || checkCol(compChar) || checkSkos1(compChar) || checkSkos2(compChar)) {
            end = true;
        }
        return end;
    }

    public void compMiddleMOve() {
            List<MyButton> emptyButtons = myButtons.stream()
                    .filter(button -> button.getMyChar() == null)
                    .filter(button -> button.getRow() == 1 && button.getCol() == 1)
                    .collect(Collectors.toList());
            MyButton button = emptyButtons.get(0);
            array[button.getCol()][button.getRow()] = compChar;
            button.setFill(new ImagePattern(changeImage(compChar)));
            button.setChar(compChar);
            liczbaRuchow++;
    }

    public void compCornerMove() {
        List<MyButton> emptyButtons = myButtons.stream()
                .filter(button -> button.getMyChar() == null)
                .filter(button -> button.getRow() == 0 && button.getCol() == 0 || button.getRow() == 0 && button.getCol() == 2
                || button.getRow() == 2 && button.getCol() == 0 || button.getRow() == 2 && button.getCol() == 2)
                .collect(Collectors.toList());
        MyButton button = emptyButtons.get(random.nextInt(emptyButtons.size()));
        array[button.getCol()][button.getRow()] = compChar;
        button.setFill(new ImagePattern(changeImage(compChar)));
        button.setChar(compChar);
        liczbaRuchow++;
    }

    public void checkCompRow() {
        
    }

    public Label getStatus() {
        return status;
    }
}

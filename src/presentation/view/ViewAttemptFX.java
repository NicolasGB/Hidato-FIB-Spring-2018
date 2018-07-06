package presentation.view;

import data.CtrlGenericData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import javafx.util.Duration;
import presentation.controllers.CtrlPresentation;

import java.io.IOException;
import java.util.Optional;

/**
 * ViewFX to display the Attempt
 */
public class ViewAttemptFX extends GenericViewFX {

    /**
     * Instance of the presentation controller
     */
    private CtrlPresentation ctrlPresentation;
    /**
     * Grid Pane where the map will be displayed
     */
    @FXML
    private GridPane gridPane;

    /**
     * The problem's type
     */
    private String type;

    /**
     * The difficulty of the problem
     */
    private String difficulty;

    /**
     * The adjacency used in the problem
     */
    private String adjacency;

    /**
     * Problem's identifier
     */
    private String problemId;

    /**
     * Attempt's identifier
     */
    private String attemptId;

    /**
     * Number of rows that the problem's map has
     */
    private int rows;

    /**
     * Number of columns that the problem's map has
     */
    private int columns;

    /**
     * The map's template of the problem
     */
    private String[][] map;

    /**
     * An observable list of the size of the GridPane's columns
     */
    private ObservableList<ColumnConstraints> columnConstraints;

    /**
     * An observable list of the size of the GridPane's rows
     */
    private ObservableList<RowConstraints> rowConstraints;

    /**
     * Attribute to display the timer
     */
    private Timeline timeline = new Timeline();;


    /**
     * The validate button
     */
    @FXML
    private Button validate;

    /**
     * Button to solve the attempt
     */
    @FXML
    private Button solveButton;

    /**
     * The value to be inserted in the solution
     */
    private String value;

    /**
     * The current player's solution
     */
    private String[][] solution;

    /**
     * The bigger number in the map
     */
    private int max;

    /**
     * Text that displays the type of the problem
     */
    @FXML
    private Text pType;

    /**
     * Text that displays the number of rows of the problem
     */
    @FXML
    private Text pRows;

    /**
     * Text that displays  the number of columns of the problem
     */
    @FXML
    private Text pColumns;

    /**
     * Text that displays the adjacency used in the problem
     */
    @FXML
    private Text pAdj;

    /**
     * Text that displays the difficulty of the problem
     */
    @FXML
    private Text pDiff;

    /**
     * Button to save the current attempt
     */
    @FXML
    private Button save;

    /**
     * Label to display the timer
     */
    @FXML
    private Label timerLabel;

    /**
     * The seconds from the timer
     */
    private Integer seconds;

    /**
     * The last number added in the solution
     */
    private int lastNumber;

    /**
     * The correctness of the solution
     */
    private boolean correct = false;

    /**
     * If set, the attempt will be saved on exit
     */
    private boolean saveAttempt = false;

    /**
     * Text field with the following number of the solution
     */
    @FXML
    TextField modifyLastNumber;

    /**
     * Button to clear the map
     */
    @FXML
    private Button clearButton;

    /**
     * Button to get a hint
     */
    @FXML
    private Button hintButton;


    /**
     * Initializes the view and links it to the given ctrlPresentation
     * @param ctrlPresentation the given ctrlPresentation to link
     */
    public ViewAttemptFX(CtrlPresentation ctrlPresentation){ super(ctrlPresentation);this.ctrlPresentation=super.ctrlPresentation;}

    /**
     * Sets all the attributes of the problem into the view ones to be displayed
     * @param problemInfo the info from the problem to be used or displayed
     * @param adj the adjacency used in the problem
     */
    public void setProblemInfo(String[] problemInfo, String adj){
        problemId = problemInfo[0];
        type = problemInfo[1];
        rows = Integer.parseInt(problemInfo[2]);
        columns = Integer.parseInt(problemInfo[3]);
        difficulty = problemInfo[4];
        adjacency = adj;
        solution = new String[rows][columns];
        try {
            map = ctrlPresentation.getProblemMap(problemId);
        } catch (Exception e) {
            showFileReadErrorAlert(e);
        }
        saveAttempt = false;
    }

    /**
     * Sets all attributes connected to the attempt to be displayed, including changing the map
     *
     * Method called when loading an already started attempt, setting autosave to true.
     *
     * @param attemptId The {@link domain.game.Attempt#id} of the Attempt to be displayed
     * @param seconds   The seconds stored in the attempt
     * @param savedMap  The map of numbers that the user entered that is stored in the attempt
     */
    public void setAttemptInfo(String attemptId, int seconds, String[][] savedMap) throws IOException, ClassNotFoundException {
        this.attemptId = attemptId;
        setTimer(seconds);
        this.map = savedMap; setAttemptMap();
        saveAttempt = true;
    }

    /**
     * Sets the timer with the given time
     * @param time to be set
     */
    public void setTimer(int time) {
        correct = false;
        seconds = time;
    }

    /**
     * Initializes the information of the attempt
     * @throws IOException could not read from Problem file
     * @throws ClassNotFoundException Outdated Problem file
     */
    private void setAttemptMap() throws IOException, ClassNotFoundException {
        String[][] problemMap = ctrlPresentation.getProblemMap(problemId);
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns; j++) {
                if(!map[i][j].equals(problemMap[i][j])){
                    Button b = (Button) getNodeFromGridPane(gridPane, j, i);
                    setSavedValue(map[i][j],b);
                }
            }

        }
    }

    /**
     * Initializes the view
     */
    @Override
    public void initialize() {
        super.initialize();
        pType.setText("Type: " + type);
        pRows.setText("Rows: " + rows);
        pColumns.setText("Columns: " + columns);
        pAdj.setText("Adjacency: " + adjacency);
        pDiff.setText("Difficulty: " + difficulty);

        timeline.setCycleCount(Timeline.INDEFINITE);

        columnConstraints = gridPane.getColumnConstraints();
        rowConstraints = gridPane.getRowConstraints();
        gridPane.getColumnConstraints().removeAll(columnConstraints);
        gridPane.getRowConstraints().removeAll(rowConstraints);
        gridPane.getChildren().clear();


        calculateColumnConstraint();
        calculateRowConstraint();
        max = 0;
        for (int i = 0; i <rows ; i++) {
            for (int j = 0; j < columns; j++) {
                solution[i][j] = map[i][j];
                if (isNumeric(map[i][j]) && Integer.parseInt(map[i][j]) > max){
                    max = Integer.parseInt(map[i][j]);
                }
            }
        }
        fillGridPaneWithButtons();

        try {
            setAttemptMap();
            checkLastNumber();
            modifyLastNumber.setText(""+ (lastNumber+1));
        } catch (Exception e) {
            showFileReadErrorAlert(e);
        }
    }

    /**
     * Displays the view
     */
    @Override
    public void display() {
        doTimer();
    }

    /**
     * Hides the view
     */
    @Override
    public void hide() {
        timeline.getKeyFrames().clear(); // To stop the timer
        if (saveAttempt) {
            try {
                ctrlPresentation.saveAttempt(problemId, solution, seconds);
            } catch (Exception e) {
                showFileWriteErrorAlert(e);
            }
        }
    }

    /**
     * Finds the last number added to the solution and sets
     * the lastnumber attribute with it
     */
    private void checkLastNumber() {
        lastNumber = 1;
        boolean exists = true;
        while(exists) {
            exists = false;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (solution[i][j].equals(String.valueOf((1 + lastNumber)))) {
                        lastNumber = Integer.parseInt(solution[i][j]);
                        exists = true;
                    }

                }
            }
        }
    }

    /**
     * Calculates the size of the row constraint
     */
    private void calculateRowConstraint() {
        gridPane.setPrefHeight(800);
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            if (type.equals("Hexagon") ){
                rowConst.setPrefHeight(800/rows);
            }
            else rowConst.setPrefHeight(800/rows);
            gridPane.getRowConstraints().add(rowConst);
        }
    }

    /**
     * Calculates the size of the column constraint
     */
    private void calculateColumnConstraint() {
        gridPane.setPrefWidth(800);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            if (type.equals("Hexagon")){
                cc.setPrefWidth(800/columns);
            }
            else  cc.setPrefWidth(800/columns);
            gridPane.getColumnConstraints().add(cc);
        }
    }

    /**
     * Fills the GridPane with buttons corresponding with
     * the map template, initializes the actions of each button,
     * sets the displayable shapes of the buttons depending on
     * the problem type
     */
    private void fillGridPaneWithButtons() {
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                Button button = new Button();
                ViewGenerateFX.setButtonId(i, j, button);
                button.setStyle("-fx-background-color: white;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1;");

                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);
                setPopUp(map[i][j], button);
                if(!map[i][j].equals("#") && !map[i][j].equals("*")) {
                    if(!map[i][j].equals("?")) {
                        if(map[i][j].equals("1") || map[i][j].equals(String.valueOf(max))){
                            button.setText(map[i][j]);
                            button.setStyle("-fx-background-color: turquoise;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-text-fill: black;"
                            );
                        }
                        else {
                            button.setText(map[i][j]);
                            button.setStyle("-fx-background-color: springgreen;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-text-fill: black;"
                            );
                        }
                    }
                    gridPane.add(button, j, i);
                }
                else if (map[i][j].equals("*")){
                    button.setStyle("-fx-background-color: mediumseagreen;" +
                            "-fx-border-color: black;" +
                            "-fx-border-width: 1;" +
                            "-fx-text-fill: black;"
                    );
                    gridPane.add(button,j,i);
                }

                if(type.equals("Triangle")) {
                    Polygon triangle = new Polygon();
                    String idButton = button.getId();
                    int    x =  Integer.parseInt(idButton.substring(0,2));
                    int    y = Integer.parseInt(idButton.substring(2,4));
                    triangle.getPoints().addAll(new Double[]{
                            0.5, 0.0,
                            0.0, 1.0,
                            1.0, 1.0 });
                    Polygon triangleDown = new Polygon();
                    triangleDown.getPoints().addAll(new Double[]{
                            0.0, 0.0,
                            1.0, 0.0,
                            0.5, 1.0 });
                    if((x%2 == 0 && y%2 == 0) || (x%2== 1 && y%2 == 1)){
                        button.setShape(triangle);
                    }
                    else {
                        button.setShape(triangleDown);
                    }
                    button.setPickOnBounds(false);

                    Platform.runLater(() -> {
                        int n = Integer.parseInt(idButton.substring(2,4));
                        button.setTranslateX(-(n % 20 - 1) * ((800/columns)/2));
                    });
                }

                else if(type.equals("Hexagon")) {
                    double[] path = new double[12];
                    for (int q = 0; q < 6; q++) {
                        double x = Math.cos(Math.PI / 3.0 * q + Math.PI / 2.0);
                        double y = Math.sin(Math.PI / 3.0 * q + Math.PI / 2.0);
                        path[q * 2] = x;
                        path[q * 2 + 1] = y;
                    }
                    Polygon aPoly = new Polygon(path);
                    button.setShape(aPoly);
                    button.setPickOnBounds(false);

                    Platform.runLater(() -> {
                        String idButton = button.getId();
                        int x =  Integer.parseInt(idButton.substring(0,2));
                        if (x % 2 == 1) {
                            button.setTranslateX((800/columns)/2);
                        }
                        button.setTranslateY(-(x % 20 - 1) * (((800/rows)/2 * 0.577350269)-1));
                    });
                }
            }
        }
    }

    /**
     * Sets the given value to the button text and changes the css of it
     * when loading a already started attempt
     * @param s the given value to be set
     * @param button the button displayed
     */
    private void setSavedValue(String s, Button button){
        button.setText(s);
        button.setStyle("-fx-background-color: #C1FFC1;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-text-fill: black;");
        setButtonAction(button);
    }

    /**
     * Sets the actions event of the given button
     * @param button the displayed button
     */
    private void setButtonAction(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    String idButton = button.getId();
                    int x = Integer.parseInt(idButton.substring(0, 2));
                    int y = Integer.parseInt(idButton.substring(2, 4));
                    if(!solution[x][y].equals("?")) {
                        solution[x][y] = "?";
                        button.setText("");
                        checkLastNumber();
                        modifyLastNumber.setText(""+ (lastNumber+1));
                    }
                    button.setStyle(
                            "-fx-background-color: white;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 1;"
                    );
                }
                else if (event.getButton() == MouseButton.PRIMARY){
                    lastNumber = Integer.parseInt(modifyLastNumber.getText());
                    if(isNumeric(String.valueOf(lastNumber))) {
                        String idButton = button.getId();
                        int x = Integer.parseInt(idButton.substring(0, 2));
                        int y = Integer.parseInt(idButton.substring(2, 4));
                        value = String.valueOf(lastNumber);
                        solution[x][y] = value;
                        button.setText(value);
                        checkLastNumber();
                        modifyLastNumber.setText("" + (lastNumber + 1));
                        button.setStyle(
                                "-fx-background-color: #C1FFC1;" +
                                        "-fx-border-color: black;" +
                                        "-fx-border-width: 1;" +
                                        "-fx-text-fill: black;"
                        );
                    }
                    else{
                        checkLastNumber();
                        modifyLastNumber.setText(""+ (lastNumber+1));
                    }
                }
            }
        });
    }

    /**
     * Calls the fucntion to set an action event of the displayed button
     * if needed
     * @param s the value of the button
     * @param button the displayed button
     */
    private void setPopUp(String s, Button button) {
        if(s.equals("?")){
            setButtonAction(button);
        }
    }

    /**
     * Checks if the given string is a numeric value
     * @param str the string to be checked
     * @return true if the string is numeric, false otherwise
     */
    private boolean isNumeric(String str) {
        try {
            int n = Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Function to save the started attempt and swap to the menu view
     */
    @FXML
    private void onSaveButtonClick(){
        saveAttempt = true;
        onBackButtonClick();
    }

    /**
     * Shows the solution to the current attempt,
     * if the attempt has no solution asks the player if this one
     * wants to see a solution to the problem
     */
    @FXML
    private void onSolveButtonClick(){
        String [][] s  = ctrlPresentation.getSolution(solution, adjacency, columns, rows, type);
        if(s.length > 0 && s[0].length > 0){
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    solution[i][j] = s[i][j];
                    if(!s[i][j].equals("#")) {
                        Button b = (Button) getNodeFromGridPane(gridPane, j, i);
                        if(!s[i][j].equals("*")) {
                            b.setText(s[i][j]);
                        }
                        checkLastNumber();
                        modifyLastNumber.setText("" + (lastNumber + 1));
                        if(!s[i][j].equals(map[i][j])) {
                            b.setStyle(
                                    "-fx-background-color: #C1FFC1;" +
                                            "-fx-border-color: black;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-text-fill: black;"
                            );
                        }
                        validate.setDisable(true);
                        clearButton.setDisable(true);
                        save.setDisable(true);
                        hintButton.setDisable(true);
                        clearButton.setDisable(true);
                        solveButton.setDisable(true);
                    }
                }
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No solution to the current hidato");
            alert.setHeaderText(null);
            alert.setContentText("Would you want to see a solution of the problem?");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                s  = ctrlPresentation.getSolution(map, adjacency, columns, rows, type);
                if(s.length > 0){
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            solution[i][j] = s[i][j];
                            if(!s[i][j].equals("#")) {
                                Button b = (Button) getNodeFromGridPane(gridPane, j, i);
                                if(!s[i][j].equals("*")) {
                                    b.setText(s[i][j]);
                                }
                                checkLastNumber();
                                modifyLastNumber.setText("" + (lastNumber + 1));
                                if(!s[i][j].equals(map[i][j])) {
                                    b.setStyle(
                                            "-fx-background-color: #C1FFC1;" +
                                                    "-fx-border-color: black;" +
                                                    "-fx-border-width: 1;" +
                                                    "-fx-text-fill: black;"
                                    );
                                }
                            }
                        }
                    }
                    validate.setDisable(true);
                    clearButton.setDisable(true);
                    save.setDisable(true);
                    hintButton.setDisable(true);
                    clearButton.setDisable(true);
                    solveButton.setDisable(true);
                }
            }
        }
    }

    /**
     * Function to check if a finished problem is correct,
     * if so, swaps to corresponding Ranking by Attempt
     */
    @FXML
    private void onValidateClick(){
        boolean finished = true;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(solution[i][j].equals("?")) finished = false;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        if(finished){
            correct = ctrlPresentation.validateProblem(solution,type,adjacency);
            if(correct){
                int points = 0;
                switch (difficulty){
                    case "Beginner":
                        points = 1;
                        break;
                    case "Easy":
                        points = 2;
                        break;
                    case "Medium":
                        points = 4;
                        break;
                    case "Hard":
                        points = 8;
                        break;
                    case "Pro":
                        points = 16;
                        break;
                    case "Insane":
                        points = 32;
                        break;
                }
                try {
                    ctrlPresentation.updateRankingPlayerPoints(points);
                    ctrlPresentation.updateRankingAttempt(problemId,getTimeLabel());
                    alert.setTitle("You solved the hidato!");
                    if (attemptId != null && ctrlPresentation.existsAttempt(attemptId) && !deleteAttempt()) {
                        alert.setContentText("Well Done! You got some points, check the ranking – " +
                                "but there was a problem when deleting the attempt file, please delete it manually" +
                                "(problemId: " + problemId + ").");
                    }
                    else alert.setContentText("Well Done! You got some points, check the ranking");
                    try {
                        ctrlPresentation.swapRankingAttemptFX(problemId);
                    } catch (IOException e) {
                        showSwapErrorAlert(e);
                    }
                } catch (Exception e) {
                    showFileWriteErrorAlert(e);
                }
            } else {
                alert.setTitle("Almost there, try again");
                alert.setContentText("Sorry try again, you have some errors :(!");
            }
        }
        else {
            alert.setTitle("OOPS...");
            alert.setContentText("You need to finish the Hidato");
        }
        alert.showAndWait();
    }

    /**
     * Function to swap to the menu view
     */
    @FXML
    private void onBackButtonClick(){
        try {
            if (!saveAttempt) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Exit without save", ButtonType.YES, ButtonType.CANCEL);
                a.setTitle("Exit without save");
                a.setHeaderText("Are you sure that you don't want to save your game?");
                a.showAndWait();
                if (a.getResult() == ButtonType.YES) ctrlPresentation.swapMenuFX();
            }
            else ctrlPresentation.swapMenuFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Function that gives the player a hint,
     * if the current solution up to the point
     * the player clicks on the button, is solvable it
     * will add the following number in the correct position,
     * otherwise it will tell the player that the current solution
     * is not solvable.
     */
    @FXML
    private void onHintButtonClick(){
        penalize();
        String [] s  = ctrlPresentation.getHint(solution, adjacency, columns, rows, type, lastNumber);
        if(s.length > 0){
            int x = Integer.parseInt(s[0]);
            int y = Integer.parseInt(s[1]);
            solution[x][y] = String.valueOf((lastNumber +1));
            Button b = (Button) getNodeFromGridPane(gridPane, y, x);
            b.setText(""+(lastNumber+1));
            checkLastNumber();
            modifyLastNumber.setText(""+ (lastNumber+1));
            b.setStyle(
                    "-fx-background-color: #C1FFC1;" +
                            "-fx-border-color: black;" +
                            "-fx-border-width: 1;" +
                            "-fx-text-fill: black;"
            );
        }
        else{
            noSolution();
        }
    }

    /**
     * Alert displaying that the current solution is not solvable
     */
    private void noSolution() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Can't find a solution");
        alert.setContentText("If you follow the current solution you won't solve the problem, check your map!");
        alert.showAndWait();
    }

    /**
     * TODO
     * @param gridPane
     * @param col
     * @param row
     * @return
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Clears the map
     */
    @FXML
    private void onClearButtonClick(){
        int time = seconds;
        initialize();
        seconds = time;
    }

    /**
     * Swaps to Ranking by Attempt view
     */
    @FXML
    private void onRankingButtonClick(){
        try {
            if (!saveAttempt) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Exit without save", ButtonType.YES, ButtonType.CANCEL);
                a.setTitle("Exit without save");
                a.setHeaderText("Are you sure that you don't want to save your game?");
                a.showAndWait();
                if (a.getResult() == ButtonType.YES) ctrlPresentation.swapRankingAttemptFX(problemId);
            }
            else ctrlPresentation.swapRankingAttemptFX(problemId);
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Starts the timer
     */
    @FXML
    private void doTimer() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            if (!correct) {
                ++seconds;
            }
            String time = convert(seconds);
            timerLabel.setText(time);
        });
        if (timeline.getKeyFrames().isEmpty()) timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }

    /**
     * Delete the currently displayed attempt
     *
     * @return whether or not the attempt was successfully deleted
     * @see CtrlGenericData#delete(String)
     */
    private boolean deleteAttempt() {
        return ctrlPresentation.deleteAttempt(attemptId);
    }

    /**
     * Returns the seconds passed solving the attempt
     * @return the seconds passed
     */
    private Integer getTimeLabel() {
        return seconds;
    }

    /**
     * Converts an int to a displayable text for the timer
     * @param a seconds passed
     * @return displayable string for the timer
     */
    public static String convert(int a) {
        int sec = a, min = 0, hour = 0;
        String s = "";
        String m = "";
        String h = "";
        String time;
        if (sec > 59) {
            min = sec/60;
            sec = sec%60;
        }
        if (min > 59) {
            hour = min/60;
            min = min%60;
        }
        if (sec < 10) s = ("0"+sec);
        else s = String.valueOf(sec);
        if (min < 10) m = ("0"+min);
        else m = String.valueOf(min);
        if (hour < 10) h = ("0"+hour);
        else h = String.valueOf(hour);
        time = (h+":"+m+":"+s);

        return time;
    }

    /**
     * Adds seconds to the timer after using a hint depending on the difficulty of the problem
     */
    private void penalize () {
        if (difficulty.equalsIgnoreCase("Beginner")|| difficulty.equalsIgnoreCase("Easy")) seconds+= 20;
        else if (difficulty.equalsIgnoreCase("Medium")|| difficulty.equalsIgnoreCase("Hard")) seconds+= 40;
        else seconds+= 60;
    }

}

package presentation.view;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import presentation.controllers.CtrlPresentation;

import java.io.IOException;
import java.util.Optional;

import static java.lang.Double.MAX_VALUE;

/**
 * Generate view of the Hidato problems
 */
public class ViewGenerateFX extends GenericViewFX {

    /**
     * Instance of the presentation controller
     */
    private CtrlPresentation ctrlPresentation;

    /**
     * BorderPane of the stage
     */
    @FXML
    private BorderPane borderPane;

    /**
     * Vertical box in the left section of the borderpane
     */
    @FXML
    private VBox leftVbox;

    /**
     * GridPane used to display the map
     */
    @FXML
    private GridPane gridPane;

    /**
     * Choice box of the type of the problem
     */
    @FXML
    private ChoiceBox<String> cbType;

    /**
     * Choice box of the difficulty of the problem
     */
    @FXML
    private ChoiceBox<String> cbDifficulty;

    /**
     * Choice box of the number of rows of the problem
     */
    @FXML
    private ChoiceBox<String> cbRows;

    /**
     * Choice box of the number of columns of the problem
     */
    @FXML
    private ChoiceBox<String> cbColumns;

    /**
     * Toggle button of the adjacency of the problem
     */
    @FXML
    private ToggleButton Adjacency;

    /**
     * Button to generate a problem
     */
    @FXML
    private Button Generate;

    /**
     * Button to generate a personalized problem
     */
    @FXML
    private Button Personalize;

    /**
     * Observable list containing the column constraints
     */
    private ObservableList<ColumnConstraints> columnConstraints;

    /**
     * Observable list containing the row constraints
     */
    private ObservableList<RowConstraints> rowConstraints;

    /**
     * Button to go back
     */
    @FXML
    private Button Back;

    /**
     * Type of the problem
     */
    private String type;

    /**
     * Difficulty of the problem
     */
    private String difficulty;

    /**
     * Adjacency of the problem
     */
    private String adjacency;

    /**
     * Number of rows of the map
     */
    private int rows;

    /**
     * Number of columns of the map
     */
    private int columns;

    /**
     * Map personalized by the player
     */
    private String[][] personalizedMap;

    /**
     * The value to be added to the map
     */
    private String value;

    /**
     * Vertical box containing the gridpane
     */
    @FXML
    private VBox VboxGp;

    /**
     * Maximum number in order to be able to find a solution
     * in a personalized map
     */
    private int maxNumber;

    /**
     * Initializes the view and links the presentation controller
     * @param ctrlPresentation
     */
    public ViewGenerateFX(CtrlPresentation ctrlPresentation){super(ctrlPresentation); this.ctrlPresentation=super.ctrlPresentation;}

    /**
     * Initializes the view
     */
    @Override
    public void initialize() {
        super.initialize();
        type = "type";
        adjacency = "C";
        difficulty = "difficulty";
        rows = 0;
        columns = 0;

        columnConstraints = gridPane.getColumnConstraints();
        rowConstraints = gridPane.getRowConstraints();
        gridPane.getColumnConstraints().removeAll(columnConstraints);
        gridPane.getRowConstraints().removeAll(rowConstraints);

        stage.setResizable(true);

        cbType.getItems().add("Triangle");
        cbType.getItems().add("Square");
        cbType.getItems().add("Hexagon");

        Adjacency.setText("Sides");

        cbDifficulty.getItems().add("Beginner");
        cbDifficulty.getItems().add("Easy");
        cbDifficulty.getItems().add("Medium");
        cbDifficulty.getItems().add("Hard");
        cbDifficulty.getItems().add("Pro");
        cbDifficulty.getItems().add("Insane");

        for (int i = 3; i < 21 ; i++) {
            cbRows.getItems().add(""+ i);
            cbColumns.getItems().add(""+ i);
        }
        cbColumns.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    columns = Integer.parseInt(newValue);
                    gridPane.getColumnConstraints().removeAll(columnConstraints);
                    gridPane.getChildren().clear();
                    calculateColumnConstraint();
                    checkMapSize();
                    if(!type.equals("type")){
                        fillGridPaneWithButtons();
                    }
                    maxNumber = rows * columns;
                });

        cbRows.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    rows = Integer.parseInt(newValue);
                    gridPane.getRowConstraints().removeAll(rowConstraints);
                    gridPane.getChildren().clear();
                    calculateRowConstraint();
                    checkMapSize();
                    if(!type.equals("type")){
                        fillGridPaneWithButtons();
                    }
                    maxNumber = rows * columns;
                });

        cbType.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                    type = newValue;
                    gridPane.getRowConstraints().removeAll(rowConstraints);
                    gridPane.getColumnConstraints().removeAll(columnConstraints);
                    gridPane.getChildren().clear();
                    calculateColumnConstraint();
                    calculateRowConstraint();
                    fillGridPaneWithButtons();


                });

        cbDifficulty.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> difficulty = newValue);

        gridPane.setGridLinesVisible(true);

    }

    /**
     * Checks the size of the displayed map in order to add
     * or delete the Insane difficulty
     */
    private void checkMapSize() {
        if(columns == 3 && rows == 3){
            cbDifficulty.getItems().remove("Insane");
        }
        else{
            int   index = cbDifficulty.getItems().indexOf("Insane");
            if(index != 5){
                cbDifficulty.getItems().add("Insane");
            }
        }
    }

    /**
     * Calculates the size of the rows
     */
    private void calculateRowConstraint() {
        gridPane.setPrefHeight(800);
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            if (type.equals("Hexagon")){
                rowConst.setPrefHeight(800/rows);
            }
            else rowConst.setPrefHeight(800/rows);
            gridPane.getRowConstraints().add(rowConst);
        }
    }

    /**
     * Calculates the size of the columns
     */
    private void calculateColumnConstraint() {
        gridPane.setPrefWidth(800);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            if (type.equals("Hexagon")){
                cc.setPrefWidth(800/columns);
            }
            else cc.setPrefWidth(800/columns);
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
        personalizedMap = new String[rows][columns];
        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                Button button = new Button();
                setButtonId(i, j, button);
                button.setMaxHeight(MAX_VALUE);
                button.setMaxWidth(MAX_VALUE);
                setPopUp(button);
                gridPane.add(button, j, i);
                button.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 1;"
                );

                if(type.equals("Triangle")) {
                    stage.show();
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
                    stage.show();
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
                personalizedMap[i][j] = "?";
            }
        }
    }

    /**
     * Sets the button id corresponding to the position it represents in the GridPane
     * @param i X position in the GridPane
     * @param j Y position in the GridPane
     * @param button displayed button
     */
    static void setButtonId(int i, int j, Button button) {
        if(i < 10){
            if(j < 10){
                button.setId("0"+ i+"0"+j);
            }
            else {
                button.setId("0" + i + "" + j);
            }
        }
        else if(j < 10){
                button.setId(""+i+"0"+j);
        }
        else{
            button.setId(""+ i + "" + j);
        }
    }

    /**
     * Sets the action of the given button and binds it to a
     * dialog in order for the player to change the attributes of the
     * button (value for personalized maps)
     * @param button displayed button
     */
    private void setPopUp(Button button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Input the value");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter a number, # or * :");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    value = result.get();
                    String idButton = button.getId();

                    int    x =  Integer.parseInt(idButton.substring(0,2));
                    int    y = Integer.parseInt(idButton.substring(2,4));
                    if(isNumeric(value) || value.equals("*") || value.equals("#")){
                        personalizedMap[x][y] = value;
                        button.setText(value);
                        if(value.equals("*")){
                            button.setStyle(
                                    "-fx-background-color: mediumseagreen;"+
                                            "-fx-border-color: black;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-text-fill: black;"
                            );
                            maxNumber--;
                        }
                        else if (value.equals("#")){
                            button.setStyle("-fx-background-color: purple;"+
                                            "-fx-border-color: black;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-text-fill: black;"
                            );
                            maxNumber--;
                        }
                        else{
                            button.setStyle(
                                    "-fx-background-color: springgreen;"+
                                            "-fx-border-color: black;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-text-fill: black;"
                            );
                            if(Integer.parseInt(value) > maxNumber ){
                                invalidNumberAlert();
                                button.setText("");
                                personalizedMap[x][y] = "?";
                                button.setStyle(
                                        "-fx-background-color: white;" +
                                                "-fx-border-color: black;" +
                                                "-fx-border-width: 1;"
                                );
                            }
                        }
                    }
                    else if(value.equals("")){
                        button.setText("");
                        personalizedMap[x][y] = "?";
                        button.setStyle(
                                "-fx-background-color: white;" +
                                        "-fx-border-color: black;" +
                                        "-fx-border-width: 1;"
                        );
                        maxNumber++;
                    }
                }
            }
        });
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    String idButton = button.getId();
                    int x = Integer.parseInt(idButton.substring(0, 2));
                    int y = Integer.parseInt(idButton.substring(2, 4));
                    if (!personalizedMap[x][y].equals("?")) {
                        personalizedMap[x][y] = "?";
                        button.setText("");
                    }
                    button.setStyle(
                            "-fx-background-color: white;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 1;"
                    );
                }
            }
        });
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
     * Swaps to menu view
     */
    @FXML
    private void onBackButtonClick(){
        try {
            ctrlPresentation.swapMenuFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Sets the adjacency of the Problem
     */
    @FXML
    private void onToggleButtonClick(){
        if(Adjacency.getText().equals("Sides") && !type.equals("Hexagon") && !type.equals("Type")){
            Adjacency.setText("Sides & Angles");
            this.adjacency = "CA";
        }
        else{
            Adjacency.setText("Sides");
            this.adjacency = "C";
        }
    }

    /**
     * Generates a problem with the selected attributes and swaps to the attempt view
     */
    @FXML
    private void onGenerateButtonClick() {
        if(!type.equals("type") && !adjacency.equals("adjacency") && !difficulty.equals("difficulty") && rows != 0 && columns != 0){
            boolean adj = false;
            if(adjacency.equals("CA")) adj = true;
            String[] mapInfo = new String[0];
            try {
                mapInfo = ctrlPresentation.generateProblem(type, adj, difficulty, rows, columns);
                try {
                    ctrlPresentation.swapNewAttemptFX(mapInfo[0]);
                } catch (ClassNotFoundException e) {
                    showSwapErrorAlert(e);
                }
            } catch (IOException e) {
                showFileWriteErrorAlert(e);
            }
        }
        else {
            missingParametersAlert();
        }
    }

    /**
     * Generates a personalized problem with the selected attributes
     * and the personalized map made by the player then swaps to Attempt view
     * if Personalized problem has no solution or cant be generated , displays an alert
     */
    @FXML
    private void onPersonalizeButtonClick() {
        boolean firstInserted = false;
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns; j++) {
                if(personalizedMap[i][j].equals("1")) firstInserted = true;;
            }
        }
        if(firstInserted){
            if(!type.equals("type") && !adjacency.equals("adjacency") && !difficulty.equals("difficulty")) {
                try {
                    boolean adj = false;
                    if (adjacency.equals("CA")) adj = true;
                    String[] mapInfo = ctrlPresentation.generatePersonalizedProblem(type, adj, difficulty, rows, columns, personalizedMap);
                    if (mapInfo.length > 0) {
                        try {
                            ctrlPresentation.swapNewAttemptFX(mapInfo[0]);
                        } catch (ClassNotFoundException e) {
                            showSwapErrorAlert(e);
                        }
                    } else {
                        invalidHidatoAlert();
                    }
                } catch (IOException e) {
                    showFileWriteErrorAlert(e);
                }
            }
            else {
                missingParametersAlert();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Missing starting point");
            alert.setContentText("Looks like you forgot to insert the number 1");
            alert.showAndWait();
        }
    }

    /**
     * Alert for an invalid personalized hidato
     */
    private void invalidHidatoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Invalid Hidato");
        alert.setContentText("The personalized Hidato you are trying to create has no solution.");
        alert.showAndWait();
    }

    /**
     * Alert displayed when trying to add a number which will make
     * the hidato impossible to generate
     */
    private void invalidNumberAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Invalid Number");
        alert.setContentText("The number you are trying to insert is too big, the Hidato wouldn't have a solution");
        alert.showAndWait();
    }

    /**
     * Alert displayed when a player tries to generate a problem
     * with missing parameters
     */
    private void missingParametersAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Bad Parameters");
        alert.setContentText("Remember to set all parameters.");
        alert.showAndWait();
    }
}

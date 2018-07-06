package presentation.view;

import domain.controllers.CtrlDomain;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import presentation.controllers.CtrlPresentation;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * View for displaying a list of {@link domain.game.Problem}s.
 * @author Samuel Hoenle
 */
public class ViewProblemListFX extends GenericViewFX {

    /**
     * Inner class for holding information to be displayed in one row in the list of problems.
     *
     * Attributes:
     * id: id of the problem
     * type: type of the problem cells
     * numRows: number of rows of the problem
     * numColumns: number of columns of the problem
     * difficulty: difficulty of the problem
     * creator: creator of the problem
     */
    private static class ProblemView {

        /**
         * The id of the problem
         */
        private SimpleStringProperty id;
        /**
         * The type of the problem
         */
        private SimpleStringProperty type;
        /**
         * The number of rows of the problem
         */
        private SimpleStringProperty numRows;
        /**
         * The number of columns of the problem
         */
        private SimpleStringProperty numColumns;
        /**
         * The difficulty of the problem
         */
        private SimpleStringProperty difficulty;
        /**
         * The creator of the problem
         */
        private SimpleStringProperty creator;

        /**
         * The constructor of the holding class
         * @param list A {@code String} array of 6 fields containing return values of {@link CtrlDomain#getDisplayableProblemInfo(String)}
         */
        ProblemView(String[] list) {
            if (list.length != 6) throw new IllegalArgumentException("Wrong number of fields");

            this.id = new SimpleStringProperty(list[0]);
            this.type = new SimpleStringProperty(list[1]);
            this.numRows = new SimpleStringProperty(list[2]);
            this.numColumns = new SimpleStringProperty(list[3]);
            this.difficulty = new SimpleStringProperty(list[4]);
            this.creator = new SimpleStringProperty(list[5]);
        }

        /**
         * Returns the id of the stored problem
         * @return The id of the stored problem
         */
        public String getId() {
            return id.get();
        }

        /**
         * To get the {@link #type} property of the stored problem
         * @return the {@link #type} property of the stored problem
         */
        SimpleStringProperty typeProperty() {
            return type;
        }

        /**
         * To get the {@link #numRows} property of the stored problem
         * @return the {@link #numRows} property of the stored problem
         */
        SimpleStringProperty numRowsProperty() {
            return numRows;
        }

        /**
         * To get the {@link #numColumns} property of the stored problem
         * @return the {@link #numColumns} property of the stored problem
         */
        SimpleStringProperty numColumnsProperty() {
            return numColumns;
        }

        /**
         * To get the {@link #difficulty} property of the stored problem
         * @return the {@link #difficulty} property of the stored problem
         */
        SimpleStringProperty difficultyProperty() {
            return difficulty;
        }

        /**
         * To get the {@link #creator} property of the stored problem
         * @return the {@link #creator} property of the stored problem
         */
        SimpleStringProperty creatorProperty() {
            return creator;
        }
    }

    /**
     * Stores information about problems returned by {@link CtrlDomain#getDisplayableProblemInfo(String)}
     */
    private List<String[]> problemList;

    /**
     * Is the source linked to the table in the view
     */
    private ObservableList<ProblemView> data;

    /**
     * The table that displays the problems
     */
    @FXML
    private TableView<ProblemView> tableView;

    /**
     * Column in which the types of the problems are displayed
     */
    @FXML
    private TableColumn<ProblemView,String> typeColumn;

    /**
     * Column in which the number of rows of the problems are displayed
     */
    @FXML
    private TableColumn<ProblemView,String> rowsColumn;

    /**
     * Column in which the number of columns of the problems are displayed
     */
    @FXML
    private TableColumn<ProblemView,String> columnsColumn;

    /**
     * Column in which the difficulties of the problems are displayed
     */
    @FXML
    private TableColumn<ProblemView,String> difficultyColumn;

    /**
     * Column in which the creators of the problems are displayed
     */
    @FXML
    private TableColumn<ProblemView,String> creatorColumn;

    /**
     * Column containing buttons to display the rankings of a problem
     */
    @FXML
    private TableColumn<ProblemView,Button> rankingColumn;

    /**
     * Column containing buttons to start playing a game
     */
    @FXML
    private TableColumn<ProblemView,Button> playColumn;

    /**
     * Button to go back to the menu
     */
    @FXML
    private Button backButton;

    /**
     * Button to load a problem from a file
     */
    @FXML
    private Button loadButton;

    /**
     * Button containing options to filter problems after types
     */
    @FXML
    private MenuButton selectType;

    /**
     * Button containing options to filter problems after difficulties
     */
    @FXML
    private MenuButton selectDifficulty;

    /**
     * Button containing options to filter problems after their creator
     */
    @FXML
    private MenuButton selectPlayer;

    /**
     * Button to clear all filters
     */
    @FXML
    private Button clearButton;

    /**
     * List of types to filter after
     */
    private List<String> selectedTypes;

    /**
     * List of difficulties to filter after
     */
    private List<String> selectedDifficulties;

    /**
     * List of player names to filter after
     */
    private List<String> selectedPlayers;

    /**
     * Constructor linking a {@link CtrlPresentation} to this view
     * @param ctrlPresentation An instance of {@link CtrlPresentation} that should be linked to this view
     * @see GenericViewFX#GenericViewFX(CtrlPresentation)
     */
    public ViewProblemListFX(CtrlPresentation ctrlPresentation) {
        super(ctrlPresentation);
    }

    /**
     * Initializes the view
     */
    @FXML
    public void initialize() {
        super.initialize();
        //Initialize choice boxes to select problems
        selectedTypes = new ArrayList<>();
        for (String type : ctrlPresentation.getAllTypes()) {
            CheckMenuItem item = new CheckMenuItem(type);
            sortButtonListener(item, selectedTypes);
            selectType.getItems().add(item);
        }

        selectedDifficulties = new ArrayList<>();
        for (String difficulty : ctrlPresentation.getAllDifficulties()) {
            CheckMenuItem item = new CheckMenuItem(difficulty);
            sortButtonListener(item, selectedDifficulties);
            selectDifficulty.getItems().add(item);
        }

        selectedPlayers = new ArrayList<>();

        //Initialize the problem table with all problems
        data = FXCollections.observableArrayList();
        loadAllProblems();

        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        rowsColumn.setCellValueFactory(cellData -> cellData.getValue().numRowsProperty());
        columnsColumn.setCellValueFactory(cellData -> cellData.getValue().numColumnsProperty());
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());
        creatorColumn.setCellValueFactory(cellData -> cellData.getValue().creatorProperty());

        Callback<TableColumn<ProblemView, Button>, TableCell<ProblemView, Button>> cellFactoryRanking =
                new Callback<TableColumn<ProblemView, Button>, TableCell<ProblemView, Button>>() {
                    @Override
                    public TableCell call(final TableColumn<ProblemView, Button> param) {
                        final TableCell<ProblemView, Button> cell = new TableCell<ProblemView, Button>() {

                            final Button btn = new Button("Ranking");

                            @Override
                            public void updateItem(Button item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ProblemView problem = getTableView().getItems().get(getIndex());
                                        try {
                                            ctrlPresentation.swapRankingAttemptFX(problem.getId());
                                        } catch (IOException e) {
                                            showSwapErrorAlert(e);
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        rankingColumn.setCellFactory(cellFactoryRanking);

        Callback<TableColumn<ProblemView, Button>, TableCell<ProblemView, Button>> cellFactoryPlay =
                new Callback<TableColumn<ProblemView, Button>, TableCell<ProblemView, Button>>() {
                    @Override
                    public TableCell call(final TableColumn<ProblemView, Button> param) {
                        final TableCell<ProblemView, Button> cell = new TableCell<ProblemView, Button>() {

                            final Button btn = new Button("Play");

                            @Override
                            public void updateItem(Button item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ProblemView problem = getTableView().getItems().get(getIndex());
                                        try {
                                            ctrlPresentation.swapNewAttemptFX(problem.getId());
                                        } catch (Exception e) {
                                            showSwapErrorAlert(e);
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        playColumn.setCellFactory(cellFactoryPlay);
        tableView.setItems(data);
    }

    /**
     * Displays the view
     */
    @FXML
    public void display() {
        // Set names here every time so that newly signed-up players are also included
        selectPlayer.getItems().clear();
        List<String> allPlayers = ctrlPresentation.getAllCreatorNames();
        for (String name : allPlayers) {
            CheckMenuItem item = new CheckMenuItem(name);
            sortButtonListener(item, selectedPlayers);
            selectPlayer.getItems().add(item);
        }

        for (String selected : selectedPlayers) {
            if (!allPlayers.contains(selected)) selectedPlayers.remove(selected);
        }
    }

    /**
     * Helper method to build listeners for sort buttons
     * @param item      The {@link CheckMenuItem} to which the listener should be added
     * @param selected  The list that should be changed as a result
     */
    private void sortButtonListener(CheckMenuItem item, List<String> selected) {
        item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                selected.add(item.getText());
            } else {
                selected.remove(item.getText());
            }
            loadSelected();
        });
    }

    /**
     * Clears data in the table and reloads it from current content in {@link #problemList}
     */
    private void reloadTableData(){
        data.clear();
        for (String[] problem : problemList) {
            data.add(new ProblemView(problem));
        }
    }

    /**
     * Loads all problems that are stored in the Game/Problems folder
     * @see CtrlPresentation#getListOfAllProblems()
     */
    private void loadAllProblems() {
        problemList = this.ctrlPresentation.getListOfAllProblems();
        reloadTableData();
    }

    /**
     * Loads filtered problems after the information stored in {@link #selectedDifficulties}, {@link #selectedTypes}, {@link #selectedPlayers}
     * @see CtrlPresentation#getListOfSelectedProblems(List, List, List)
     */
    private void loadSelected() {
        try {
            problemList = this.ctrlPresentation.getListOfSelectedProblems(selectedDifficulties, selectedTypes, selectedPlayers);
            reloadTableData();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Filter incorrect", "Cannot filter after these arguments", e.getMessage());
        }
    }

    /**
     * Swap back to the {@link ViewMenuFX}
     * @see CtrlPresentation#swapMenuFX()
     */
    @FXML
    public void onBackButtonClick(){
        try {
            ctrlPresentation.swapMenuFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Displays a file chooser and lets the user load a problem from a file
     * @see CtrlPresentation#loadProblemFromPath(String)
     */
    @FXML
    public void onLoadFile() {
        boolean run = true;
        File initialDirectory = new File(System.getProperty("user.home"));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files", ".txt");
        FileChooser chooser = new FileChooser();

        while (run) {
            chooser.setTitle("Choose a text file containing an Hidato game");
            chooser.setSelectedExtensionFilter(filter);
            chooser.setInitialDirectory(initialDirectory);
            File selectedFile = chooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    data.add(new ProblemView(ctrlPresentation.loadProblemFromPath(selectedFile.getPath())));
                    run = false;
                } catch (IOException e) {
                    showFileReadErrorAlert(e);
                } catch (ParseException e) {
                    showAlert(Alert.AlertType.ERROR,"Wrong format","The content of your file is not in the right format","Make sure that the file you selected follows the format of an Hidato problem file");
                } catch (CtrlDomain.ProblemExistsException e) {
                    showAlert(Alert.AlertType.ERROR,"Already loaded","Your Hidato is already in the list of games","Please select a new Hidato.");
                } catch (CtrlDomain.ProblemNotValidException e) {
                    showAlert(Alert.AlertType.ERROR,"Problem not valid","Your Hidato does not have a solution","Please select a valid Hidato.");
                }
            }
            else {
                run = false;
            }
        }
    }

    /**
     * Clears all filters stored in {@link #selectedTypes}, {@link #selectedDifficulties} and {@link #selectedPlayers}
     */
    @FXML
    public void onClearFilters(){
        selectType.getItems().forEach((menuItem -> ((CheckMenuItem)menuItem).setSelected(false)));
        selectDifficulty.getItems().forEach((menuItem -> ((CheckMenuItem)menuItem).setSelected(false)));
        selectPlayer.getItems().forEach((menuItem -> ((CheckMenuItem)menuItem).setSelected(false)));

        loadAllProblems();
    }
}

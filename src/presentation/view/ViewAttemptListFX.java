package presentation.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import presentation.controllers.CtrlPresentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewFX to display the AttemptList
 */
public class ViewAttemptListFX extends GenericViewFX {

    /**
     *  AttemptView inner class of the ViewAttemptListFX
     */
    private static class AttemptView {

        /**
         * Attributes of the inner class.
         * attemptId: id of the current attempt
         * id: id of the problem
         * type: type of the problem cells
         * numRows: number of rows of the problem
         * numColumns: number of columns of the problem
         * difficulty: difficulty of the problem
         * creator: creator of the problem
         */
        private SimpleStringProperty attemptId, id, type, numRows, numColumns, difficulty, creator;

        /**
         * Creator function of the inner class
         * @param list list of strings that contains all the information of the problem and the current attempt
         */
        AttemptView(String[] list) {
            if (list.length != 7) throw new IllegalArgumentException("Wrong number of fields");

            this.attemptId = new SimpleStringProperty(list[0]);
            this.id = new SimpleStringProperty(list[1]);
            this.type = new SimpleStringProperty(list[2]);
            this.numRows = new SimpleStringProperty(list[3]);
            this.numColumns = new SimpleStringProperty(list[4]);
            this.difficulty = new SimpleStringProperty(list[5]);
            this.creator = new SimpleStringProperty(list[6]);
        }

        /**
         * Id getter function of the inner class
         * @return id of the AttemptView
         */
        public String getId() {
            return id.get();
        }

        /**
         * Type getter function of the inner class
         * @return type of the AttemptView
         */
        SimpleStringProperty typeProperty() {
            return type;
        }

        /**
         * numRows getter function of the inner class
         * @return numRows of the AttemptView
         */
        SimpleStringProperty numRowsProperty() {
            return numRows;
        }

        /**
         * numColumns getter function of the inner class
         * @return numColumns of the AttemptView
         */
        SimpleStringProperty numColumnsProperty() {
            return numColumns;
        }

        /**
         * Difficulty getter function of the inner class
         * @return difficulty of the AttemptView
         */
        SimpleStringProperty difficultyProperty() {
            return difficulty;
        }

        /**
         * Creator getter function of the inner class
         * @return creator of the AttemptView
         */
        SimpleStringProperty creatorProperty() {
            return creator;
        }

        /**
         * attemptId getter function of the inner class
         * @return attemptId of the AttemptView
         */
        public String getAttemptId() {
            return attemptId.get();
        }
    }

    /**
     * List where are stored the current saved attempts of a player
     */
    private List<String[]> attemptList;

    /**
     * Observable list where the saved attempts with all the information of an AttemptView
     */
    private ObservableList<AttemptView> data;

    /**
     * Table view where the saved attempts will be displayed
     */
    @FXML
    private TableView<AttemptView> tableView;

    /**
     * tableView column where the problem type will be displayed
     */
    @FXML
    private TableColumn<AttemptView,String> typeColumn;

    /**
     * tableView column where the problem rows will be displayed
     */
    @FXML
    private TableColumn<AttemptView,String> rowsColumn;

    /**
     * tableView column where the problem columns will be displayed
     */
    @FXML
    private TableColumn<AttemptView,String> columnsColumn;

    /**
     * tableView column where the problem difficulty will be displayed
     */
    @FXML
    private TableColumn<AttemptView,String> difficultyColumn;

    /**
     * tableView column where the problem creator will be displayed
     */
    @FXML
    private TableColumn<AttemptView,String> creatorColumn;

    /**
     * tableView column with a button that redirects the player to AttemptViewFX
     */
    @FXML
    private TableColumn<AttemptView,Button> playColumn;

    /**
     * Button that redirects the player to the main menu
     */
    @FXML
    private Button backButton;

    /**
     * Initializes the view and links it to the given ctrlPresentation
     * @param ctrlPresentation the given ctrlPresentation to link
     */
    public ViewAttemptListFX(CtrlPresentation ctrlPresentation) {
        super(ctrlPresentation);
    }

    /**
     * Initializes the view
     */
    @FXML
    public void initialize() {
        super.initialize();
        data = FXCollections.observableArrayList();
        try {
            attemptList = this.ctrlPresentation.getListOfAllAttempts();
            for (String[] attempt : attemptList) {
                data.add(new AttemptView(attempt));
            }
        } catch (Exception e) {
            showFileReadErrorAlert(e);
        }

        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        rowsColumn.setCellValueFactory(cellData -> cellData.getValue().numRowsProperty());
        columnsColumn.setCellValueFactory(cellData -> cellData.getValue().numColumnsProperty());
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());
        creatorColumn.setCellValueFactory(cellData -> cellData.getValue().creatorProperty());

        Callback<TableColumn<AttemptView, Button>, TableCell<AttemptView, Button>> cellFactoryPlay =
                new Callback<TableColumn<AttemptView, Button>, TableCell<AttemptView, Button>>() {
                    @Override
                    public TableCell call(final TableColumn<AttemptView, Button> param) {
                        final TableCell<AttemptView, Button> cell = new TableCell<AttemptView, Button>() {

                            final Button btn = new Button("Continue");

                            @Override
                            public void updateItem(Button item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        AttemptView problem = getTableView().getItems().get(getIndex());
                                        try {
                                            ctrlPresentation.swapStartedAttemptFX(problem.getId(), String.valueOf(problem.getAttemptId()));
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
     * Function to swap to the menu view
     */
    @FXML
    public void onBackButtonClick(){
        try {
            ctrlPresentation.swapMenuFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }
}

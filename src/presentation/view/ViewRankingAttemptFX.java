package presentation.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import presentation.controllers.CtrlPresentation;

import java.io.IOException;
import java.util.List;

/**
 * Attempt list of the logged player view
 */
public class ViewRankingAttemptFX extends GenericViewFX {
    /**
     * Class to represent the ranking entries
     */
    static class AttemptHolder{

        /**
         * Property to store the player's name
         */
        private SimpleStringProperty name;

        /**
         *Property to store the playe'rs to,e
         */
        private SimpleStringProperty time;

        /**
         * Constructs a ranking entry
         * @param name the name of the player in the entry
         * @param time time of the entry
         */
        AttemptHolder(String name, String time){
            this.name = new SimpleStringProperty(name);
            this.time = new SimpleStringProperty(ViewAttemptFX.convert(Integer.parseInt(time)));
        }
        /**
         * Gets the name of the entry
         * @return Name of the entry
         */
        public String getName() {
            return name.get();
        }

        /**
         * Gets the points of the entry
         * @return Points of the entry
         */
        public String getTime() {
            return time.get();
        }
        /**
         * Gets the property wrapping the name
         * @return Name Property
         */
        StringProperty getNameProperty() {
            return this.name;
        }

        /**
         * Gets the property wrapping the points
         * @return points Property
         */
        StringProperty getTimeProperty() {
            return this.time;
        }
    }

    /**
     * Instance of the presentation controller
     */
    private CtrlPresentation ctrlPresentation;

    /**
     * The id of the problem we want the ranking from
     */
    private String problemId;

    /**
     * Button to go back
     */
    @FXML
    Button button;

    /**
     * Table to store the ranking entries
     */
    @FXML
    private TableView<ViewRankingAttemptFX.AttemptHolder> rankingTable;

    /**
     * Column where the name will be stored
     */
    @FXML
    private TableColumn<ViewRankingAttemptFX.AttemptHolder, String> nameColumn;

    /**
     * Column where the points will be stored
     */
    @FXML
    private TableColumn<ViewRankingAttemptFX.AttemptHolder, String> timeColumn;

    /**
     * ObservableList to update the table
     */
    private ObservableList<ViewRankingAttemptFX.AttemptHolder> rankingAttemptData = FXCollections.observableArrayList();

    /**
     * Initializes the view and links the presentation controller
     * @param ctrlPresentation The presentation controller
     */
    public ViewRankingAttemptFX (CtrlPresentation ctrlPresentation){ super(ctrlPresentation);this.ctrlPresentation=super.ctrlPresentation;}

    /**
     * Initializes the view
     */
    @Override
    public void initialize() {
        super.initialize();
        try {
            List<String> rankingInfo = ctrlPresentation.getRankingAttempt(this.problemId);
            rankingAttemptData = passRanking(rankingInfo);
        } catch (Exception e) {
            showFileReadErrorAlert(e);
        }
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());

        rankingTable.setItems(rankingAttemptData);
    }

    /**
     * Sets the problem id of the displayed ranking
     * @param id problem's identifier
     */
    public void setProblemId(String id){
        this.problemId = id;
    }

    /**
     * Handles the click on the back button
     */
    @FXML
    public void onBackButtonClick() {
        try {
            ctrlPresentation.swapProblemListFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Returns an observable list of all the ranking entries
     * @param rankingInfo a list of the ranking entries
     * @return observable list of the ranking entries
     */
    private ObservableList<ViewRankingAttemptFX.AttemptHolder> passRanking(List<String> rankingInfo) {
        ObservableList<ViewRankingAttemptFX.AttemptHolder> ret = FXCollections.observableArrayList();
        for (int i = 0; i < rankingInfo.size(); i+= 2) {
            ViewRankingAttemptFX.AttemptHolder ah = new ViewRankingAttemptFX.AttemptHolder(rankingInfo.get(i), rankingInfo.get(i+1));
            ret.add(ah);
        }
        return ret;
    }

}

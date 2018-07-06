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
 * View FX to display the player's ranking
 */
public class ViewRankingPlayerFX extends GenericViewFX {

    static class RankingHolder{

        private SimpleStringProperty name;

        private SimpleStringProperty points;

        RankingHolder(String name, String points){
            this.name = new SimpleStringProperty(name);
            this.points = new SimpleStringProperty(points);
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
        public String getPoints() {
           return points.get();
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
        StringProperty getPointsProperty() {
            return this.points;
        }
    }

    /**
     * Instance of the presentation controller
     */
    private CtrlPresentation ctrlPresentation;

    /**
     * Back button
     */
    @FXML
    private Button BackButton;

    /**
     * Table where the ranking entries will be stored
     */
    @FXML
    private TableView<RankingHolder> rankingTable;

    /**
     * Column where the name will be stored
     */
    @FXML
    private TableColumn<RankingHolder, String> nameColumn;

    /**
     * Column where the points will be stored
     */
    @FXML
    private TableColumn<RankingHolder, String> pointsColumn;

    /**
     * ObservableList to update the table
     */
    private ObservableList<RankingHolder> rankingPlayerData = FXCollections.observableArrayList();

    /**
     * Initializes the view and links it to the given ctrlPresentation
     * @param ctrlPresentation the given ctrlPresentation to link
     */
    public ViewRankingPlayerFX(CtrlPresentation ctrlPresentation) {super(ctrlPresentation);
        this.ctrlPresentation=super.ctrlPresentation;
    }


    /**
     * Initializes the view
     */
    @Override
    @FXML
    public void initialize() {
        super.initialize();
        List<String> rankingInfo;
        try {
            rankingInfo = ctrlPresentation.getRankingPlayer();
            rankingPlayerData = passRanking(rankingInfo);
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            pointsColumn.setCellValueFactory(cellData -> cellData.getValue().getPointsProperty());
            rankingTable.setItems(rankingPlayerData);
        } catch (Exception e) {
            showFileReadErrorAlert(e);
        }
    }


    /**
     * When the user clicks the Back button swaps to the Menu view
     * @throws IOException
     */
    @FXML
    public void onBackButtonClick() {
        try {
            ctrlPresentation.swapMenuFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }


    /**
     * Returns an observable list of all the ranking entries
     * @param rankingInfo a list of the ranking entries
     * @return observable list of the ranking entries
     */
    private ObservableList<RankingHolder> passRanking(List<String> rankingInfo) {
        ObservableList<RankingHolder> ret = FXCollections.observableArrayList();
        for (int i = 0; i < rankingInfo.size(); i+= 2) {
            RankingHolder rh = new RankingHolder(rankingInfo.get(i), rankingInfo.get(i+1));
            ret.add(rh);
        }
        return ret;
    }


}

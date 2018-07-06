package presentation.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import presentation.controllers.CtrlPresentation;
import sun.security.krb5.internal.crypto.Des;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Menu of the application view
 */
public class ViewMenuFX extends GenericViewFX {

    /**
     * Button to swap to Ranking of Player
     */
    @FXML
    private Button showRankings;

    /**
     * Button to display help
     */
    @FXML
    private Button askForHelp;

    /**
     * Button to log out
     */
    @FXML
    private Button logOut;

    /**
     * Button to exit game
     */
    @FXML
    private Button exitGame;

    /**
     * Button to load a saved attempt
     */
    @FXML
    private Button loadAttempt;

    /**
     * Initializes the view and links the presentation controller
     * @param ctrlPresentation
     */
    public ViewMenuFX(CtrlPresentation ctrlPresentation) {
        super(ctrlPresentation);
    }

    /**
     * Exits the game
     */
    @FXML
    private void ExitGame() { stage.close();}

    /**
     * Swaps to Settings view
     */
    @FXML
    private void goToSettings(){
        try {
            ctrlPresentation.swapSettingsFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Swaps to Ranking by Player view
     */
    @FXML
    private void goToRankingPlayer(){
        try {
            ctrlPresentation.swapRankingPlayerFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Swaps to Log in view
     */
    @FXML
    private void goToLogIn(){
        try {
            ctrlPresentation.swapLoginFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Swaps to List of games view
     */
    @FXML
    private void goToListOfGames(){
        try {
            ctrlPresentation.swapProblemListFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Swaps to Generate view
     */
    @FXML
    private void goToGenerate(){
        try {
            ctrlPresentation.swapGenerateFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Swaps to Saved Attempts view
     */
    @FXML
    private  void goToListOfAttempts() {
        try {
            ctrlPresentation.swapAttemptListFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }


    /**
     * Displays a help alert
     */
    @FXML
    public void showHelp() throws IOException {
        try {
            Desktop.getDesktop().open(new File("UserManualHidato.pdf"));
        }
        catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Initializes the view
     */
    @Override
    public void initialize() {
        super.initialize();
    }
}

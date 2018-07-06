package presentation.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import presentation.controllers.CtrlPresentation;

import java.util.Optional;

/**
 * Abstract class of all the views
 */
public abstract class GenericViewFX {

    /**
     * Instance of the presentation controller
     */
    protected CtrlPresentation ctrlPresentation;

    /**
     * The displayed stage
     */
    protected Stage stage;

    /**
     * Initializes the view and links it to the given presentation controller
     * @param ctrlPresentation the presentation controller
     */

    public GenericViewFX(CtrlPresentation ctrlPresentation){
        this.ctrlPresentation = ctrlPresentation;
    }

    /**
     * Sets the stage from the view
     * @param stage The stage to be set
     */
    public void setStage(Stage stage){
        this.stage = stage;
        this.stage.centerOnScreen();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setMinWidth(primaryScreenBounds.getWidth());
        stage.setMinHeight(primaryScreenBounds.getHeight());
    }

    /**
     * Initializes the view
     */
    public void initialize() {
        stage.setMaximized(true);
    }

    /**
     * Displays the view
     */
    public void display() {

    }

    /**
     * Hides the view
     */
    public void hide() {

    }

    /**
     * Generic alert
     * @param type the type of the alert
     * @param title the title of the alert
     * @param header the header of the alert
     * @param content the content of the alert
     * @return the alert
     */
    public Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert newAlert = new Alert(type);
        newAlert.setTitle(title);
        newAlert.setHeaderText(header);
        newAlert.setContentText(content);
        newAlert.getDialogPane().getStyleClass().add("pane");
        return newAlert.showAndWait();
    }

    public Optional<ButtonType> showSwapErrorAlert(Exception e) {
        return showAlert(Alert.AlertType.ERROR, "Could not swap view", "Error when swapping view", e.getMessage());
    }

    public Optional<ButtonType> showFileReadErrorAlert(Exception e) {
        return showAlert(Alert.AlertType.ERROR, "Could not read file", "Error when reading a file", e.getMessage());
    }

    public Optional<ButtonType> showFileWriteErrorAlert(Exception e) {
        return showAlert(Alert.AlertType.ERROR, "Could not save file", "Error when saving a file", e.getMessage());
    }
}

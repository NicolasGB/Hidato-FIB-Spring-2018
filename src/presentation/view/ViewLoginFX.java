package presentation.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import presentation.controllers.CtrlPresentation;

import java.io.IOException;
import java.util.Optional;

/**
 * Users login view of the application
 */
public class ViewLoginFX extends GenericViewFX {

    /**
     * Username variable to store the logged player username
     */
    private String username;

    /**
     * Password variable to store the logged player password
     */
    private String password;

    /**
     * Text field to enter the username of the player to Login
     */
    @FXML
    private TextField usernameField;

    /**
     * Password field to enter the password of the player to Login
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Button that tries to Login the player
     */
    @FXML
    private Button loginButton;


    /**
     * Initializes the view and links it to the given ctrlPresentation
     * @param ctrlPresentation the given ctrlPresentation to link
     */
    public ViewLoginFX(CtrlPresentation ctrlPresentation)
    {
        super(ctrlPresentation);
    }

    /**
     * Initializes the view
     */
    @Override
    public void initialize() {
        super.initialize();
        handleKeyEvents();
        usernameField.requestFocus();
    }

    /**
     * Handles the key events on the TextFields
     */
    @FXML
    private void handleKeyEvents() {
        usernameField.setOnKeyPressed(
                event -> {
                    if(event.getCode() == KeyCode.ENTER){
                            passwordField.requestFocus();
                    }
                }
        );
        passwordField.setOnKeyPressed(
                event ->{
                    if(event.getCode() == KeyCode.ENTER) {
                        clickOnLogin();
                    }
                }
         );
    }

    /**
     * Tries to Login the player with the values from the text and password field
     */
    @FXML
    public void clickOnLogin(){
        if (usernameField.getText() == null || usernameField.getText().length() == 0 || passwordField.getText() == null || passwordField.getText().length() == 0) {
            showAlert(Alert.AlertType.ERROR, "There are empty fields", null, "Please, fill all the fields.");
            return;
        }

        username = usernameField.getText();
        password = passwordField.getText();

        if(!ctrlPresentation.existsPlayer(username)){
            showNoLoginAlert();
            return;
        }
        boolean loggedIn = false;
        try {
            loggedIn = ctrlPresentation.logInPlayer(username, password);
        } catch (Exception e) {
            showFileReadErrorAlert(e);
        }
        if (!loggedIn) {
            showNoLoginAlert();
        } else {
            try {
                ctrlPresentation.swapMenuFX();
            } catch (IOException e) {
                showSwapErrorAlert(e);
            }
        }
    }

    /**
     * Helper method to display an alert informing the user that the login failed
     * @return the result of {@link GenericViewFX#showAlert(Alert.AlertType, String, String, String)}
     */
    private Optional<ButtonType> showNoLoginAlert() {
        return showAlert(Alert.AlertType.ERROR, "Login failed", null, "The username or password is not correct");
    }

    /**
     * Goes to the register view
     */
    @FXML
    public void registerText()  {
        try {
            ctrlPresentation.swapRegisterFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

}

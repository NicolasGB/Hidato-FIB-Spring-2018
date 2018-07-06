package presentation.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import presentation.controllers.CtrlPresentation;

import java.io.IOException;

/**
 * Users register view of the application
 */
public class ViewRegisterFX extends GenericViewFX {

    /**
     * Variable used to store the values from the username TextField
     */
    private String username;

    /**
     * Variable used to store the password from the password field
     */
    private String password;

    /**
     * TextField to enter the username of the player to register
     */
    @FXML
    private TextField usernameField;

    /**
     * PasswordField to enter the password to register
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Initializes the view and links it to the given ctrlPresentation
     * @param ctrlPresentation the given ctrlPresentation to link
     */
    public ViewRegisterFX(CtrlPresentation ctrlPresentation) {
        super(ctrlPresentation);
    }
    //public ViewRegisterFX (){}

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
     * Handles all the key events from the view
     */
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
                    if(event.getCode() == KeyCode.ENTER){
                        clickOnReg();
                    }
                }
        );
    }

    /**
     * goes to logIn view
     */
    @FXML
    public void clickOnLogIn(){

        try {
            ctrlPresentation.swapLoginFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }

    /**
     * Trys to register the player with the values from the text and password fields
     */
    @FXML
    public void clickOnReg() {
        if (usernameField.getText() == null || usernameField.getText().length() == 0 || passwordField.getText() == null || passwordField.getText().length() == 0) {
            showAlert(Alert.AlertType.ERROR, "There are empty fields", null, "Please, fill all the fields.");
            return;
        }

        username = usernameField.getText();
        password = passwordField.getText();
        if(ctrlPresentation.existsPlayer(username)) {
            showAlert(Alert.AlertType.ERROR,"Existing Player",null,"This player already exists");
        }
        else {
            try {
                ctrlPresentation.savePlayer(username,password,"/presentation/view/Images/avatar.png",false);
                ctrlPresentation.logInPlayer(username,password);
                try {
                    ctrlPresentation.swapMenuFX();
                } catch (IOException e) {
                    showSwapErrorAlert(e);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Register failed", null, "The player could not be registered");
            }
        }
    }
}

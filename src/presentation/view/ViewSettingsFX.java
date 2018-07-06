package presentation.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import presentation.controllers.CtrlPresentation;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Settings view of the application
 */
public class ViewSettingsFX extends GenericViewFX {

    /**
     * Password field to enter the old password
     */
    @FXML
    private PasswordField oldPassword;

    /**
     * Password field to enter the new password
     */
    @FXML
    private PasswordField newPassword1;

    /**
     * Password field to enter the confirmation of the password
     */
    @FXML
    private PasswordField newPassword2;

    /**
     * Button to open the File chooser
     */
    @FXML
    private Button fileChooser;

    /**
     * The image view that shows the avatar
     */
    @FXML
    private ImageView avatar;

    /**
     * Button that returns to the menu
     */
    @FXML
    private Button back;


    /**
     * password variable to store the password from the logged Player
     */
    private String password;


    /**
     * Initializes the view
     */
    @Override
    public void initialize() {
        super.initialize();
        if(ctrlPresentation.isPathChanged()) {
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(new File(ctrlPresentation.getLoggedPlayerAvatar()));
            } catch (IOException e) {
                showFileReadErrorAlert(e);
            }
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            this.avatar.setImage(image);
        }
        else {
            this.avatar.setImage(new Image("/presentation/view/Images/default.png"));
            }
    }

    /**
     * Initializes the view and links it to the given ctrlPresentation
     * @param ctrlPresentation the given ctrlPresentation to link
     */
    public ViewSettingsFX(CtrlPresentation ctrlPresentation) {
        super(ctrlPresentation);
        
    }

    /**
     * Changes the password with the new one from the text fields
     */
    public void changePassword() {
        if (oldPassword.getText() == null || oldPassword.getText().length() == 0 || newPassword1.getText() == null
                || newPassword1.getText().length() == 0 || newPassword2.getText() == null || newPassword2.getText().length() == 0) {
            showAlert(Alert.AlertType.ERROR, "Empty fields", null, "Please fill all the fields");
            return;
        }

        try {
            password = ctrlPresentation.getPlayerPassword(ctrlPresentation.getLoggedPlayerName());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,"Change pass error",null,"The password could not been changed");;
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR,"Change pass error",null,"The password could not been changed");
        }
        if (!oldPassword.getText().equals(password)) {
            showAlert(Alert.AlertType.ERROR, "Wrong old Password", null, "The old password is not correct");
            return;
        }
        if(newPassword1.getText().equals(oldPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Same password", null, "The new password must be different than the old one");
            return;
        }
        if (!newPassword1.getText().equals(newPassword2.getText())) {
            showAlert(Alert.AlertType.ERROR, "No matching passwords", null, "The passwords do not match");
            return;
        }

        try {
            ctrlPresentation.savePlayer(ctrlPresentation.getLoggedPlayerName(), newPassword1.getText(),ctrlPresentation.getLoggedPlayerAvatar(),ctrlPresentation.isPathChanged());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,"Change pass error",null,"The password could not been changed");
        }
        showAlert(Alert.AlertType.CONFIRMATION, "Password Changed", null, "Your password has been changed");
        oldPassword.clear();
        newPassword1.clear();
        newPassword2.clear();
    }

    /**
     *Changes the avatar with the new path from the file chooser
     */
    public void changeAvatar() {
        ctrlPresentation.pathChanged();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ImageFiles", "*.png"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            String Lname = ctrlPresentation.getLoggedPlayerName();
            ctrlPresentation.setLoggedPlayerAvatar(f.getAbsolutePath());
            try {
                ctrlPresentation.savePlayer(Lname,ctrlPresentation.getPlayerPassword(Lname),ctrlPresentation.getLoggedPlayerAvatar(),ctrlPresentation.isPathChanged());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR,"Wrong File",null,"The chosen file is wrong");
            } catch (ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR,"Wrong File",null,"The chosen file is wrong");
            }
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(f);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR,"Wrong File",null,"The image could not be read");
            }
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            this.avatar.setImage(image);
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid file", null, "Please select a valid file");
        }
    }

    /**
     * Goes to menu view
     */
    public void returnToMenu() {
        try {
            ctrlPresentation.swapMenuFX();
        } catch (IOException e) {
            showSwapErrorAlert(e);
        }
    }
}

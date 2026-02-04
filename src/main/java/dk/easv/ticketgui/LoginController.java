package dk.easv.ticketgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {


    @FXML
    private PasswordField pswPassword;
    @FXML
    private TextField txtUsername;

    @FXML
    private void onSignInClick(ActionEvent actionEvent) {
        String username = "hej";
        String password = "bruh";

        if(txtUsername.getText().equals(username) && pswPassword.getText().equals(password)){
            return;
        }
    }
}

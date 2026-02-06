package dk.easv.ticketgui;


import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CoordinatorController {


    @FXML
    private void handleLogout() {
        JFXPanel totalEventsLabel = new JFXPanel(); // Dummy component to get the current stage
        Stage stage = (Stage) totalEventsLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickCreateEvent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateEvent.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle("Create Event");
        stage.setScene(scene);
        stage.show();

    }
}

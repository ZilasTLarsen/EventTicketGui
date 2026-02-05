package dk.easv.ticketgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class CoordinatorController {


    @FXML
    private Label totalEventsLabel;

    @FXML
    private Label upcomingEventsLabel;

    @FXML
    private Label totalTicketsLabel;


    @FXML
    private Button createEventButton;


    @FXML
    public void initialize() {

        updateStatistics(2, 2, 1701);


        setupEventHandlers();
    }


    public void updateStatistics(int totalEvents, int upcomingEvents, int totalTickets) {
        if (totalEventsLabel != null) {
            totalEventsLabel.setText(String.valueOf(totalEvents));
        }
        if (upcomingEventsLabel != null) {
            upcomingEventsLabel.setText(String.valueOf(upcomingEvents));
        }
        if (totalTicketsLabel != null) {
            totalTicketsLabel.setText(String.valueOf(totalTickets));
        }
    }


    private void setupEventHandlers() {
        if (createEventButton != null) {
            createEventButton.setOnAction(event -> handleCreateEvent());
        }
    }


    @FXML
    private void handleCreateEvent() {
        System.out.println("Create Event button clicked");

    }


    @FXML
    private void handleLogout() {
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

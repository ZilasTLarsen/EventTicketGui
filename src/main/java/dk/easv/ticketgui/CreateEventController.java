package dk.easv.ticketgui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateEventController {


    @FXML
    private Button cancelButton;
    @FXML
    private Button createEventButton;
    @FXML
    private ComboBox categoryComboBox;
    @FXML
    private TextField capacityField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextArea notesField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField timeField;
    @FXML
    private DatePicker dateField;

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
    }

    @FXML
    private void handleCreateEvent(ActionEvent actionEvent) {
    }
}

package dk.easv.ticketgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller for the Create Event Dialog
 * Handles form validation and event creation
 */
public class CreateEventController {

    // Required Fields
    @FXML
    private TextField eventNameField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField locationField;

    @FXML
    private TextArea notesField;

    // Optional Fields
    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField capacityField;

    @FXML
    private ComboBox<String> categoryComboBox;

    // Buttons
    @FXML
    private Button closeButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button createEventButton;

    // Time formatter
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Initialize method called after FXML loading
     */
    @FXML
    public void initialize() {
        // Setup category options
        setupCategories();

        // Setup time field validation
        setupTimeFieldValidation();

        // Setup capacity field to accept only numbers
        setupCapacityFieldValidation();

        // Setup real-time validation
        setupFormValidation();
    }

    /**
     * Setup category dropdown options
     */
    private void setupCategories() {
        categoryComboBox.getItems().addAll(
                "Conference",
                "Workshop",
                "Concert",
                "Festival",
                "Seminar",
                "Webinar",
                "Sports",
                "Exhibition",
                "Networking",
                "Other"
        );
        categoryComboBox.setPromptText("Select category");
    }

    /**
     * Setup time field validation and formatting
     */
    private void setupTimeFieldValidation() {
        timeField.textProperty().addListener((obs, oldVal, newVal) -> {
            // Remove non-digit and non-colon characters
            if (newVal != null && !newVal.matches("[0-9:]*")) {
                timeField.setText(newVal.replaceAll("[^0-9:]", ""));
            }

            // Auto-format time as user types (e.g., 1400 becomes 14:00)
            if (newVal != null && newVal.length() == 4 && !newVal.contains(":")) {
                String formatted = newVal.substring(0, 2) + ":" + newVal.substring(2);
                timeField.setText(formatted);
            }
        });

        // Set placeholder behavior
        timeField.setPromptText("HH:MM");
    }

    /**
     * Setup capacity field to accept only numbers
     */
    private void setupCapacityFieldValidation() {
        capacityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.matches("\\d*")) {
                capacityField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Setup real-time form validation
     */
    private void setupFormValidation() {
        // Add listeners to required fields
        eventNameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        dateField.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        timeField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        locationField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        notesField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }

    /**
     * Validate the entire form and enable/disable create button
     */
    private void validateForm() {
        boolean isValid = isFormValid();
        createEventButton.setDisable(!isValid);
    }

    /**
     * Check if all required fields are filled
     */
    private boolean isFormValid() {
        // Check required fields
        if (eventNameField.getText() == null || eventNameField.getText().trim().isEmpty()) {
            return false;
        }

        if (dateField.getValue() == null) {
            return false;
        }

        if (timeField.getText() == null || timeField.getText().trim().isEmpty()) {
            return false;
        }

        if (!isValidTime(timeField.getText())) {
            return false;
        }

        if (locationField.getText() == null || locationField.getText().trim().isEmpty()) {
            return false;
        }

        if (notesField.getText() == null || notesField.getText().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Validate time format (HH:MM)
     */
    private boolean isValidTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            return false;
        }

        try {
            LocalTime.parse(time, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Handle create event button click
     */
    @FXML
    private void handleCreateEvent() {
        if (!isFormValid()) {
            showError("Please fill in all required fields correctly.");
            return;
        }

        try {
            // Create event object with form data
            Event newEvent = createEventFromForm();

            // TODO: Save event to database
            System.out.println("Creating event: " + newEvent);

            // Show success message
            showSuccess("Event created successfully!");

            // Close dialog
            closeDialog();

        } catch (Exception e) {
            showError("Failed to create event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create Event object from form data
     */
    private Event createEventFromForm() {
        Event event = new Event();

        // Required fields
        event.setName(eventNameField.getText().trim());
        event.setDate(dateField.getValue());
        event.setTime(timeField.getText().trim());
        event.setLocation(locationField.getText().trim());
        event.setNotes(notesField.getText().trim());

        // Optional fields
        if (descriptionField.getText() != null && !descriptionField.getText().trim().isEmpty()) {
            event.setDescription(descriptionField.getText().trim());
        }

        if (capacityField.getText() != null && !capacityField.getText().trim().isEmpty()) {
            try {
                event.setCapacity(Integer.parseInt(capacityField.getText().trim()));
            } catch (NumberFormatException e) {
                // Ignore invalid capacity
            }
        }

        if (categoryComboBox.getValue() != null) {
            event.setCategory(categoryComboBox.getValue());
        }

        return event;
    }

    /**
     * Handle cancel button click
     */
    @FXML
    private void handleCancel() {
        // Check if form has been modified
        if (isFormModified()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Discard Changes");
            confirmation.setHeaderText("Are you sure you want to cancel?");
            confirmation.setContentText("All entered information will be lost.");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    closeDialog();
                }
            });
        } else {
            closeDialog();
        }
    }

    /**
     * Handle close button (X) click
     */
    @FXML
    private void handleClose() {
        handleCancel(); // Same behavior as cancel
    }

    /**
     * Check if form has been modified
     */
    private boolean isFormModified() {
        return (eventNameField.getText() != null && !eventNameField.getText().trim().isEmpty()) ||
                (dateField.getValue() != null) ||
                (timeField.getText() != null && !timeField.getText().trim().isEmpty()) ||
                (locationField.getText() != null && !locationField.getText().trim().isEmpty()) ||
                (notesField.getText() != null && !notesField.getText().trim().isEmpty()) ||
                (descriptionField.getText() != null && !descriptionField.getText().trim().isEmpty()) ||
                (capacityField.getText() != null && !capacityField.getText().trim().isEmpty()) ||
                (categoryComboBox.getValue() != null);
    }

    /**
     * Close the dialog
     */
    private void closeDialog() {
        Stage stage = (Stage) createEventButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Show error alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to Create Event");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show success alert
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clear all form fields
     */
    public void clearForm() {
        eventNameField.clear();
        dateField.setValue(null);
        timeField.clear();
        locationField.clear();
        notesField.clear();
        descriptionField.clear();
        capacityField.clear();
        categoryComboBox.setValue(null);
    }

    /**
     * Simple Event model class
     */
    public static class Event {
        private String name;
        private LocalDate date;
        private String time;
        private String location;
        private String notes;
        private String description;
        private Integer capacity;
        private String category;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        @Override
        public String toString() {
            return "Event{" +
                    "name='" + name + '\'' +
                    ", date=" + date +
                    ", time='" + time + '\'' +
                    ", location='" + location + '\'' +
                    ", category='" + category + '\'' +
                    ", capacity=" + capacity +
                    '}';
        }
    }
}

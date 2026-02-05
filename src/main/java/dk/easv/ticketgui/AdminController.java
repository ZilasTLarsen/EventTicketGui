package dk.easv.ticketgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.time.LocalDate;


public class AdminController {


    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label coordinatorsLabel;

    @FXML
    private Label staffUsersLabel;


    @FXML
    private Button addUserButton;

    @FXML
    private Button logoutButton;


    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> userColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> createdColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;


    @FXML
    public void initialize() {

        setupTableColumns();


        loadSampleData();


        updateStatistics();
    }


    private void setupTableColumns() {

        userColumn.setCellFactory(column -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String username, boolean empty) {
                super.updateItem(username, empty);
                if (empty || username == null) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());

                    HBox container = new HBox(10);
                    container.setAlignment(Pos.CENTER_LEFT);


                    StackPane avatar = new StackPane();
                    String avatarClass = user.getRole().equals("Coordinator")
                            ? "user-avatar user-avatar-blue"
                            : "user-avatar user-avatar-purple";
                    avatar.getStyleClass().addAll(avatarClass.split(" "));

                    Label initial = new Label(username.substring(0, 1).toUpperCase());
                    initial.getStyleClass().add("label");
                    avatar.getChildren().add(initial);


                    Label nameLabel = new Label(username);
                    nameLabel.getStyleClass().add("username-text");

                    container.getChildren().addAll(avatar, nameLabel);
                    setGraphic(container);
                }
            }
        });


        emailColumn.setCellFactory(column -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String email, boolean empty) {
                super.updateItem(email, empty);
                if (empty || email == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label emailLabel = new Label(email);
                    emailLabel.getStyleClass().add("email-text");
                    setGraphic(emailLabel);
                }
            }
        });


        roleColumn.setCellFactory(column -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) {
                    setGraphic(null);
                } else {
                    Label roleBadge = new Label(role);
                    String badgeClass = role.equals("Coordinator")
                            ? "role-badge role-badge-coordinator"
                            : "role-badge role-badge-staff";
                    roleBadge.getStyleClass().addAll(badgeClass.split(" "));
                    setGraphic(roleBadge);
                }
            }
        });


        createdColumn.setCellFactory(column -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label dateLabel = new Label(date);
                    dateLabel.getStyleClass().add("date-text");
                    setGraphic(dateLabel);
                }
            }
        });


        actionsColumn.setCellFactory(column -> new TableCell<User, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());

                    HBox actionBox = new HBox(10);
                    actionBox.setAlignment(Pos.CENTER_LEFT);

                    if (user.getRole().equals("Coordinator")) {

                        Button removeBtn = new Button("Remove Coordinator");
                        removeBtn.getStyleClass().add("action-button-remove");
                        removeBtn.setOnAction(e -> handleRemoveCoordinator(user));
                        actionBox.getChildren().add(removeBtn);
                    } else {

                        Button makeBtn = new Button("Make Coordinator");
                        makeBtn.getStyleClass().add("action-button-make");
                        makeBtn.setOnAction(e -> handleMakeCoordinator(user));
                        actionBox.getChildren().add(makeBtn);
                    }

                    // Delete button
                    Button deleteBtn = new Button("Delete");
                    deleteBtn.getStyleClass().add("action-button-delete");
                    deleteBtn.setOnAction(e -> handleDeleteUser(user));
                    actionBox.getChildren().add(deleteBtn);

                    setGraphic(actionBox);
                }
            }
        });
    }


    private void loadSampleData() {
        userTable.getItems().clear();
        userTable.getItems().addAll(
                new User("john_coordinator", "john@example.com", "Coordinator", "2024-01-15"),
                new User("jane_smith", "jane@example.com", "Coordinator", "2024-01-20"),
                new User("bob_staff", "bob@example.com", "Staff", "2024-01-25")
        );
    }


    private void updateStatistics() {
        int totalUsers = userTable.getItems().size();
        long coordinators = userTable.getItems().stream()
                .filter(u -> u.getRole().equals("Coordinator"))
                .count();
        long staff = userTable.getItems().stream()
                .filter(u -> u.getRole().equals("Staff"))
                .count();

        totalUsersLabel.setText(String.valueOf(totalUsers));
        coordinatorsLabel.setText(String.valueOf(coordinators));
        staffUsersLabel.setText(String.valueOf(staff));
    }


    @FXML
    private void handleAddUser() {
        System.out.println("Add User button clicked");
        // TODO: Open add user dialog

        // Example: Add a new user
        // User newUser = new User("new_user", "newuser@example.com", "Staff",
        //                         LocalDate.now().toString());
        // userTable.getItems().add(newUser);
        // updateStatistics();
    }


    private void handleRemoveCoordinator(User user) {
        System.out.println("Remove coordinator: " + user.getUsername());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Remove Coordinator Role");
        confirmation.setHeaderText("Remove coordinator role from " + user.getUsername() + "?");
        confirmation.setContentText("This user will be changed to Staff role.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                user.setRole("Staff");
                userTable.refresh();
                updateStatistics();
            }
        });
    }


    private void handleMakeCoordinator(User user) {
        System.out.println("Make coordinator: " + user.getUsername());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Make Coordinator");
        confirmation.setHeaderText("Grant coordinator role to " + user.getUsername() + "?");
        confirmation.setContentText("This user will gain coordinator privileges.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                user.setRole("Coordinator");
                userTable.refresh();
                updateStatistics();
            }
        });
    }


    private void handleDeleteUser(User user) {
        System.out.println("Delete user: " + user.getUsername());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete User");
        confirmation.setHeaderText("Delete user " + user.getUsername() + "?");
        confirmation.setContentText("This action cannot be undone.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userTable.getItems().remove(user);
                updateStatistics();
            }
        });
    }


    @FXML
    private void handleLogout() {
        System.out.println("Logout clicked");
        // TODO: Implement logout functionality

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Logging out...");
        alert.showAndWait();
    }


    public static class User {
        private final SimpleStringProperty username;
        private final SimpleStringProperty email;
        private final SimpleStringProperty role;
        private final SimpleStringProperty createdDate;

        public User(String username, String email, String role, String createdDate) {
            this.username = new SimpleStringProperty(username);
            this.email = new SimpleStringProperty(email);
            this.role = new SimpleStringProperty(role);
            this.createdDate = new SimpleStringProperty(createdDate);
        }

        public String getUsername() { return username.get(); }
        public void setUsername(String value) { username.set(value); }
        public SimpleStringProperty usernameProperty() { return username; }

        public String getEmail() { return email.get(); }
        public void setEmail(String value) { email.set(value); }
        public SimpleStringProperty emailProperty() { return email; }

        public String getRole() { return role.get(); }
        public void setRole(String value) { role.set(value); }
        public SimpleStringProperty roleProperty() { return role; }

        public String getCreatedDate() { return createdDate.get(); }
        public void setCreatedDate(String value) { createdDate.set(value); }
        public SimpleStringProperty createdDateProperty() { return createdDate; }
    }
}

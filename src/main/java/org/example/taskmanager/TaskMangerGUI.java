package org.example.taskmanager;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TaskMangerGUI extends Application {

    private final TaskManager taskManager = new TaskManager();
    private final TableView<Task> taskTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Task Manager(Java FX)");

        setupTaskTable();
        VBox addForm = setupAddForm();
        HBox controlPanel = setupControlPanel();

        BorderPane root = new BorderPane();
        root.setCenter(taskTable);

        VBox bottomContainer = new VBox(20, addForm, controlPanel);
        bottomContainer.setPadding(new Insets(10));
        bottomContainer.setBackground(new Background(new BackgroundFill(Color.web(
                "#f4f7f9"), CornerRadii.EMPTY, Insets.EMPTY)));

        root.setBottom(bottomContainer);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f4f7f9;");

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> taskManager.saveTasks());
    }

    private void setupTaskTable() {
        taskTable.setItems(taskManager.getSortedTasks());
        taskTable.setEditable(true);
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Task, Boolean> doneCol = new TableColumn<>("Done");
        doneCol.setCellValueFactory(cellData -> cellData
                .getValue().isCompletedProperty());
        doneCol.setCellFactory(CheckBoxTableCell.forTableColumn(completed -> {
            taskManager.updateTaskStatus(taskTable.getSelectionModel().getSelectedItem());
            return null;
        }));
        doneCol.setEditable(true);
        doneCol.setPrefWidth(50);
        doneCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Task, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(90);
        priorityCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Task, String> titleCol = new TableColumn<>("Name");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        titleCol.setPrefWidth(250);

        TableColumn<Task, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(450);

        taskTable.getColumns().addAll(doneCol, priorityCol, titleCol, descCol);
    }

    private VBox setupAddForm() {
        Label header = new Label("Add Task");
        header.setStyle("-fx-font-size: 1.2em; -fx-font-weight: bold; -fx-text-fill: #334155;");

        TextField titleInput = new TextField();
        titleInput.setPromptText("Title of task(Important)");

        TextField descriptionInput = new TextField();
        descriptionInput.setPromptText("Description of task(Not important)");

        Spinner<Integer> prioritySpinner = new Spinner<>(1, 5, 3);
        prioritySpinner.setPrefWidth(100);

        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color: #0d9488; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-border-radius: 5;");
        addButton.setOnAction(e -> {
            String title = titleInput.getText().trim();
            String description = descriptionInput.getText().trim();
            int priority = prioritySpinner.getValue();

            if (!title.isEmpty()) {
                taskManager.addTask(title, description, priority);
                titleInput.clear();
                descriptionInput.clear();
                prioritySpinner.getValueFactory().setValue(3);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Title of task cannot be empty!", ButtonType.OK);
                alert.setTitle("Invalid input");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
        HBox inputFields = new HBox(10, titleInput, descriptionInput, prioritySpinner, addButton);
        inputFields.setAlignment(javafx.geometry.Pos.CENTER);
        return new VBox(10, header, inputFields);
    }

    private HBox setupControlPanel() {
        Button deleteButton = new Button("Delete Highlighted Task");
        deleteButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-radius: 5;");

        deleteButton.setOnAction(e -> {
            Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete this task" + selectedTask.getName() + "\"?", ButtonType.YES, ButtonType.NO);
                confirmation.setTitle("Confirmation of deleting task");
                confirmation.setHeaderText(null);

                confirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        taskManager.removeTask(selectedTask);
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chose a task to delete", ButtonType.OK);
                alert.setTitle("Task don't exist");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
        Button saveButton = new Button("Save now");
        saveButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-radius: 5;");
        saveButton.setOnAction(e -> taskManager.saveTasks());

        HBox controlPanel = new HBox(15, deleteButton, saveButton);
        controlPanel.setAlignment(Pos.CENTER_LEFT);

        return controlPanel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

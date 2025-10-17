package org.example.taskmanager;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.io.*;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TaskManager {

    private final ObservableList<Task> rawTasks = FXCollections.observableArrayList();
    private final SortedList<Task> sortedTasks;
    private static final String FILENAME = "tasks.txt";

    public TaskManager() {
        this.sortedTasks = new SortedList<>(rawTasks, Comparator.naturalOrder());
        loadTasks();
    }

    private void loadTasks() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("File " + FILENAME + " don't exist");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            rawTasks.addAll(reader.lines()
                    .map(Task::fromFileString)
                    .collect(Collectors.toList()));
            System.out.println("Loaded " + rawTasks.size() + " tasks");
        } catch (IOException e) {
            System.err.println("Error reading file " + e.getMessage());
        }
    }

    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Task task : rawTasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
            System.out.println("Tasks saved to " + FILENAME);
        }catch (IOException e) {
            System.err.println("Error writing file " + e.getMessage());
        }
    }

    public void addTask(String title, String description, int priority) {
        Task newTask = new Task(title, description, priority);
        rawTasks.add(newTask);
        saveTasks();
    }

    public void removeTask(Task task) {
        rawTasks.remove(task);
        saveTasks();
    }

    public void updateTaskStatus(Task task) {
        saveTasks();
    }

    public ObservableList<Task> getSortedTasks() {
        return sortedTasks;
    }
}

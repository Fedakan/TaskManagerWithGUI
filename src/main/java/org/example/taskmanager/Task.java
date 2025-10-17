package org.example.taskmanager;

import javafx.beans.property.*;

import java.util.Objects;

public class Task implements Comparable<Task> {

    private final StringProperty name;
    private final StringProperty description;
    private final IntegerProperty priority;
    private final BooleanProperty isCompleted;

    public Task(String name, String description, int priority) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleIntegerProperty(priority);
        this.isCompleted = new SimpleBooleanProperty(false);
    }

    public Task(String name, String description, int priority, boolean isCompleted) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleIntegerProperty(priority);
        this.isCompleted = new SimpleBooleanProperty(isCompleted);
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split("; ");
        if (parts.length == 4) {
            String name = parts[0];
            String description = parts[1];
            try {
                int priority = Integer.parseInt(parts[2]);
                boolean isCompleted = Boolean.parseBoolean(parts[3]);
                return new Task(name, description, priority, isCompleted);
            } catch (NumberFormatException e) {
                System.err.println("Error with parsing: " + parts[2]);
            }
        }
        return new Task("Download error", "Invalid format", 999, false);
    }

    public String toFileString() {
        return String.format("%s; %s; %d; %s", getName(), getDescription(), getPriority(), isCompleted());
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty descriptionProperty() {
        return description;
    }


    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public IntegerProperty priorityProperty() {
        return priority;
    }

    public int getPriority() {
        return priority.get();
    }

    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    public BooleanProperty isCompletedProperty() {
        return isCompleted;
    }

    public boolean isCompleted() {
        return isCompleted.get();
    }

    public void setCompleted(boolean completed) {
        this.isCompleted.set(completed);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", isCompleted=" + isCompleted +
                '}';
    }

    @Override
    public int compareTo(Task otherTask) {
        int priorityComparison = Integer.compare(this.getPriority(), otherTask.getPriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        return this.getName().compareTo(otherTask.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return priority == task.priority &&
                isCompleted == task.isCompleted &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, priority, isCompleted);
    }
}

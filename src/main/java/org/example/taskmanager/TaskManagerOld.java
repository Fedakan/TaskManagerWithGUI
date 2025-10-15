package org.example.taskmanager;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class TaskManagerOld {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = loadTasks("tasks.txt");

        while (true) {
            System.out.println("--- Task Manager ---");
            System.out.println("1. Add Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. View Particular Task");
            System.out.println("4. Change Task Priority");
            System.out.println("5. Delete Task");
            System.out.println("6. Mark Task as Done");
            System.out.println("7. Sort by Priority(Low to High)");
            System.out.println("8. Sort by Name(A - Z)");
            System.out.println("9. Sort by Priority(High to Low)");
            System.out.println("10. Sort by Name(Z - A)");
            System.out.println("11. Copy Task");
            System.out.println("0. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter Task Name: ");
                    String taskName = scanner.nextLine();
                    System.out.println("Enter Task Description: ");
                    String taskDescription = scanner.nextLine();
                    System.out.println("Enter Task Priority: ");
                    int taskPriority = scanner.nextInt();
                    scanner.nextLine();
                    tasks.add(new Task(taskName, taskDescription, taskPriority));
                    System.out.println(taskName + " added successfully!");
                    break;
                case 2:
                    System.out.println("All Tasks: ");
                    if (tasks.isEmpty()) {
                        System.out.println("No Tasks found!");
                    } else {
                        for (Task task : tasks) {
                            System.out.println(task);
                        }
                    }
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Enter Task Name or Task Description or Task Priority: ");
                    String userInput = scanner.nextLine();
                    ArrayList<Task> foundTasks = new ArrayList<>();
                    boolean isNumeric = userInput.matches("-?\\d+");
                    int searchPriority = -1;
                    if (isNumeric) {
                        searchPriority = Integer.parseInt(userInput);
                    }
                    for (Task task : tasks) {
                        if (task.getName().equalsIgnoreCase(userInput) ||
                                task.getDescription().equalsIgnoreCase(userInput) ||
                                (isNumeric && task.getPriority() == searchPriority)) {
                            foundTasks.add(task);
                        }
                    }
                    if (foundTasks.isEmpty()) {
                        System.out.println("No Tasks found!");
                    } else {
                        System.out.println("Found " + foundTasks.size() + " task(s)");
                        for (Task task : foundTasks) {
                            System.out.println(task);
                        }
                    }
                    break;
                case 4:
                    System.out.println("Enter Task Name or Task Description: ");
                    String userInputForPriorityChanging = scanner.nextLine();
                    boolean taskFound = false;
                    for (Task task : tasks) {
                        if (task.getName().equalsIgnoreCase(userInputForPriorityChanging) ||
                                task.getDescription().equalsIgnoreCase(userInputForPriorityChanging)) {
                            System.out.println("Task Found! Enter new priority: ");
                            int newPriority = scanner.nextInt();
                            scanner.nextLine();
                            task.setPriority(newPriority);
                            System.out.println("Task updated. New task information: " + task);
                            taskFound = true;
                            break;
                        }
                    }
                    if (!taskFound) {
                        System.out.println("Task Not Found!");
                    }
                    break;
                case 5:
                    System.out.println("Enter Task Name or Task Description: ");
                    String userChoiceForDeleting = scanner.nextLine();
                    boolean found = false;
                    Iterator<Task> taskIterator = tasks.iterator();
                    while (taskIterator.hasNext()) {
                        Task task = taskIterator.next();
                        if (task.getName().equalsIgnoreCase(userChoiceForDeleting)
                                || task.getDescription().equalsIgnoreCase(userChoiceForDeleting)) {
                            taskIterator.remove();
                            found = true;
                            System.out.println("Task " + task.getName() + " deleted successfully!");
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Task Not Found!");
                    }
                    break;
                case 6:
                    System.out.println("Enter Task Name or Task Description: ");
                    String userChoiceForMarking = scanner.nextLine();
                    boolean foundAndUpdated = false;
                    for (Task task : tasks) {
                        if (task.getName().equalsIgnoreCase(userChoiceForMarking)
                                || task.getDescription().equalsIgnoreCase(userChoiceForMarking)) {
                            System.out.println("Task Found! Mark as Done? (yes/no): ");
                            String taskStatus = scanner.nextLine();
                            if (taskStatus.equalsIgnoreCase("yes")) {
                                task.setCompleted(true);
                                System.out.println("Task status " + task.getName() + " changed successfully!");
                                foundAndUpdated = true;
                            } else {
                                System.out.println("Status change cancelled!");
                                foundAndUpdated = true;
                            }
                            break;
                        }
                    }
                    if (!foundAndUpdated) {
                        System.out.println("Task Not Found!");
                    }
                    break;
                case 7:
                    System.out.println("Sorted by Priority(Low to High)");
                    Collections.sort(tasks);
                    for (Task task : tasks) {
                        System.out.println(task);
                    }
                    break;
                case 8:
                    Collections.sort(tasks, Comparator.comparing(Task::getName));
                    System.out.println("Tasks sorted by name(A - Z)");
                    for (Task task : tasks) {
                        System.out.println(task);
                    }
                    break;
                case 9:
                    Collections.sort(tasks, Collections.reverseOrder());
                    System.out.println("Tasks sorted by priority(High to Low)");
                    for (Task task : tasks) {
                        System.out.println(task);
                    }
                    break;
                case 10:
                    Collections.sort(tasks, Comparator.comparing(Task::getName).reversed());
                    System.out.println("Tasks sorted by name(Z - A)");
                    for (Task task : tasks) {
                        System.out.println(task);
                    }
                    break;
                case 11:
                    System.out.println("Enter Task Name which you want to copy: ");
                    String copyName = scanner.nextLine();
                    boolean foundCopy = false;
                    for (Task task : tasks) {
                        if (task.getName().equalsIgnoreCase(copyName)) {
                            Task copiedTask = new Task(task.getName() + " (Copy)", task.getDescription(), task.getPriority());
                            tasks.add(copiedTask);
                            System.out.println("Task " + task.getName() + " copied successfully as: " + copiedTask.getName());
                            foundCopy = true;
                            break;
                        }
                    }
                    if (!foundCopy) {
                        System.out.println("Task Not Found!");
                    }
                    break;
                case 0:
                    System.out.println("Saving tasks...");
                    saveTask(tasks, "tasks.txt");
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void saveTask(ArrayList<Task> tasks, String fileName) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(fileName))) {
            for (Task task : tasks) {
                printWriter.println(task.getName() + "," + task.getDescription() + "," + task.getPriority() + "," + task.isCompleted());
            }
            System.out.println("Tasks saved successfully!");
        } catch (IOException e) {
            System.out.println("Error while saving tasks!");
        }
    }

    public static ArrayList<Task> loadTasks(String fileName) {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String taskName = parts[0];
                    String description = parts[1];
                    int priority = Integer.parseInt(parts[2]);
                    boolean completed = Boolean.parseBoolean(parts[3]);
                    Task loadedTask = new Task(taskName, description, priority);
                    loadedTask.setCompleted(completed);
                    loadedTasks.add(loadedTask);
                }
            }
            System.out.println("Tasks loaded successfully!");
        } catch (IOException e) {
            System.out.println("Task file not found!");
            ;
        }
        return loadedTasks;
    }

    public ObservableList<Task> getSortedTasks() {
    }

    public void saveTasks() {

    }

    public void addTask(String title, String description, int priority) {
    }

    public void removeTask(Task selectedTask) {
    }

    public void updateTaskStatus(Task selectedItem) {
    }
}


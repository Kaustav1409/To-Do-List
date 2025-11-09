import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Task {
    public final String description;
    public final boolean isCompleted;

    public Task(String description, boolean isCompleted) {
        this.description = description;
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return (isCompleted ? "\u2714 " : "\u274C ") + description;
    }
}

public class ToDoListGUI extends JFrame {
    private DefaultListModel<Task> taskModel;
    private JList<Task> taskList;
    private JTextField taskField;
    private static final String FILE_NAME = "tasks.txt";

    public ToDoListGUI() {
        setTitle("🌸 To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 248, 250));

        // Top panel for input
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(new Color(255, 248, 250));
        taskField = new JTextField();
        JButton addButton = new JButton("Add Task");
        addButton.setBackground(new Color(247, 207, 214));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addTask());
        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // Task list
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setFont(new Font("Poppins", Font.PLAIN, 15));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons below list
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 248, 250));
        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Task");
        JButton saveButton = new JButton("Save & Exit");

        completeButton.setBackground(new Color(207, 235, 180));
        deleteButton.setBackground(new Color(250, 170, 170));
        saveButton.setBackground(new Color(173, 216, 230));

        completeButton.addActionListener(e -> markCompleted());
        deleteButton.addActionListener(e -> deleteTask());
        saveButton.addActionListener(e -> {
            saveTasks();
            JOptionPane.showMessageDialog(this, "✅ Tasks saved successfully!");
            System.exit(0);
        });

        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        // Add everything
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load tasks
        loadTasks();

        setVisible(true);
    }

    private void addTask() {
        String text = taskField.getText().trim();
        if (!text.isEmpty()) {
            taskModel.addElement(new Task(text, false));
            taskField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a task description!");
        }
    }

    private void markCompleted() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            Task oldTask = taskModel.get(index);
            taskModel.set(index, new Task(oldTask.description, true));
            taskList.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "⚠ Select a task to mark as completed!");
        }
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            taskModel.remove(index);
        } else {
            JOptionPane.showMessageDialog(this, "⚠ Select a task to delete!");
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskModel.size(); i++) {
                Task t = taskModel.getElementAt(i);
                writer.write(t.description + ";" + t.isCompleted);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "⚠ Error saving tasks: " + e.getMessage());
        }
    }

    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    String desc = parts[0];
                    boolean done = Boolean.parseBoolean(parts[1]);
                    taskModel.addElement(new Task(desc, done));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListGUI::new);
    }
}
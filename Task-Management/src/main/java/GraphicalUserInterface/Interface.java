package GraphicalUserInterface;

import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataAccess.Serialization;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;
    
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Interface extends JPanel {

    private JTable employeeTable;
    private JTable taskTable;
    private DefaultTableModel employeeTableModel;
    private DefaultTableModel taskTableModel;
    private TasksManagement taskManagement;

    public Interface() {

        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9,1,10,10));

        JButton addEmployeeButton = new JButton("Add Employee");
        JButton addTaskButton = new JButton("Add Task");
        JButton assignTaskButton = new JButton("Assign Task");
        JButton viewEmployeesButton = new JButton("View Employees");
        JButton modifyTaskStatusButton = new JButton("Modify Task Status");
        JButton stats1Button = new JButton("Filter Employees");
        JButton stats2Button = new JButton("Count Tasks");
        JButton reloadButton = new JButton("Reload Data");
        JButton resetButton = new JButton("Reset Data");

        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(addTaskButton);
        buttonPanel.add(assignTaskButton);
        buttonPanel.add(viewEmployeesButton);
        buttonPanel.add(modifyTaskStatusButton);
        buttonPanel.add(stats1Button);
        buttonPanel.add(stats2Button);
        buttonPanel.add(reloadButton);
        buttonPanel.add(resetButton);

        employeeTableModel = new DefaultTableModel(new String[]{"Employee", "Tasks"}, 0);
        employeeTable = new JTable(employeeTableModel);

        taskTableModel = new DefaultTableModel(new String[]{"Task", "Duration"}, 0);
        taskTable = new JTable(taskTableModel);

        JScrollPane employeeTableScrollPane = new JScrollPane(employeeTable);
        JScrollPane taskTableScrollPane = new JScrollPane(taskTable);

        employeeTableScrollPane.setBorder(new TitledBorder("Employees"));
        taskTableScrollPane.setBorder(new TitledBorder("Tasks"));

        JSplitPane tableSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, employeeTableScrollPane, taskTableScrollPane);
        tableSplitPane.setResizeWeight(0.2);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanel, tableSplitPane);
        mainSplitPane.setResizeWeight(0.5);

        this.add(mainSplitPane, BorderLayout.CENTER);

        taskManagement = new TasksManagement();

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        assignTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignTask();
            }
        });

        viewEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEmployee();
            }
        });

        modifyTaskStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyTaskStatus();
            }
        });

        stats1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEmployeeByWorkDuration();
            }
        });

        stats2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countTasks();
            }
        });

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataIntoTables();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });

    }

    private void loadDataIntoTables() {
        employeeTableModel.setRowCount(0);
        taskTableModel.setRowCount(0);

        for (Employee employee : taskManagement.getAllEmployees()) {
            String taskList = tasksToString(taskManagement.getEmployeeTasks(employee.getIdEmployee()));
            employeeTableModel.addRow(new Object[]{employee.getName(), taskList});
        }

        List<Task> allTasks = Serialization.loadTaskData();
        for (Task task : allTasks) {
            taskTableModel.addRow(new Object[]{task.getTaskName(), task.estimateDuration()});
        }
    }

    private String tasksToString(List<Task> tasks) {
        return tasks.isEmpty() ? "No tasks assigned" : tasks.toString();
    }

    private void resetData() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all data?", "Confirm Reset",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            taskManagement.clearData();
            loadDataIntoTables();
            JOptionPane.showMessageDialog(this, "All data has been deleted.");
        }
    }

    public void addEmployee() {
        String employeeName = JOptionPane.showInputDialog("Enter employee name:");
        if (employeeName != null && !employeeName.isEmpty()) {
            Employee employee = new Employee(employeeName);
            taskManagement.addEmployee(employee);
            employeeTableModel.addRow(new Object[]{employeeName});
            JOptionPane.showMessageDialog(this, "Employee Added");
        }
    }

    public void addTask() {
        String taskName = JOptionPane.showInputDialog("Enter task name:");
        if (taskName == null || taskName.isEmpty()) return;

        String[] options = {"Simple Task", "Complex Task"};
        int choice = JOptionPane.showOptionDialog(this, "Select task type:", "Task Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == -1) return;

        if (choice == 0) {
            addSimpleTask(taskName);
        } else {
            addComplexTask(taskName);
        }
        JOptionPane.showMessageDialog(this, "Task Added");
    }

    private void addSimpleTask(String taskName) {
        try {
            int startHour = Integer.parseInt(JOptionPane.showInputDialog("Start hour:"));
            int endHour = Integer.parseInt(JOptionPane.showInputDialog("End hour:"));

            if (startHour < 0 || endHour > 24) {
                JOptionPane.showMessageDialog(this, "Invalid hours!");
                return;
            }

            SimpleTask newTask = new SimpleTask("Uncompleted", taskName, startHour, endHour);
            taskManagement.addTask(newTask);
            taskTableModel.addRow(new Object[]{taskName, newTask.estimateDuration()});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter numbers.");
        }
    }

    private void addComplexTask(String taskName) {
        ComplexTask complexTask = new ComplexTask("Uncompleted", taskName, new ArrayList<>());

        while (true) {
            String subTaskName = JOptionPane.showInputDialog("Enter sub-task name:");
            if (subTaskName == null) break;

            String[] subTaskOptions = {"Simple Task", "Complex Task"};
            int subTaskChoice = JOptionPane.showOptionDialog(this, "Select sub-task type:", "Sub-task Type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, subTaskOptions, subTaskOptions[0]);

            if (subTaskChoice == -1) continue;

            if (subTaskChoice == 0) {
                SimpleTask subTask = createSimpleSubTask(taskName);
                if (subTask != null) {
                    complexTask.addTask(subTask);

                    int continueChoice = JOptionPane.showConfirmDialog(this, "Do you want to add another sub-task?",
                            "Add Another Sub-task?", JOptionPane.YES_NO_OPTION);
                    if (continueChoice == JOptionPane.NO_OPTION) {
                        break;
                    }
                }
            } else {
                addComplexSubTask(subTaskName, complexTask);
            }
        }

        taskManagement.addTask(complexTask);
        taskTableModel.addRow(new Object[]{taskName, complexTask.estimateDuration()});
    }

    private void addComplexSubTask(String subTaskName, ComplexTask parentComplexTask) {
        ComplexTask complexSubTask = new ComplexTask("Uncompleted", subTaskName, new ArrayList<>());

        while (true) {
            String subSubTaskName = JOptionPane.showInputDialog("Enter sub-task for " + subTaskName);
            if (subSubTaskName == null) break;

            String[] subTaskOptions = {"Simple Task", "Complex Task"};
            int subTaskChoice = JOptionPane.showOptionDialog(this, "Select sub-task type:", "Sub-task Type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, subTaskOptions, subTaskOptions[0]);

            if (subTaskChoice == -1) continue;

            if (subTaskChoice == 0) {
                SimpleTask subTask = createSimpleSubTask(subTaskName);
                if (subTask != null) {
                    complexSubTask.addTask(subTask);

                    int continueChoice = JOptionPane.showConfirmDialog(this, "Do you want to add another sub-task?",
                            "Add Another Sub-task?", JOptionPane.YES_NO_OPTION);
                    if (continueChoice == JOptionPane.NO_OPTION) {
                        break;
                    }
                }
            } else {
                addComplexSubTask(subSubTaskName, complexSubTask);
            }
        }

        parentComplexTask.addTask(complexSubTask);
    }

    private SimpleTask createSimpleSubTask(String taskName) {
        try {
            int startHour = Integer.parseInt(JOptionPane.showInputDialog("Start hour:"));
            int endHour = Integer.parseInt(JOptionPane.showInputDialog("End hour:"));

            if (startHour < 0 || endHour > 24) {
                JOptionPane.showMessageDialog(this, "Invalid hours!");
                return null;
            }

            return new SimpleTask("Uncompleted", taskName, startHour, endHour);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter numbers.");
            return null;
        }
    }

    public void assignTask() {
        String employeeName = selectItemFromTable(employeeTableModel, "Select employee:");
        if (employeeName == null) return;

        Employee employee = taskManagement.findEmployeeByName(employeeName);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found!");
            return;
        }

        List<String> selectedTasks = selectMultipleItemsFromTable(taskTableModel, "Select tasks:");
        if (selectedTasks == null || selectedTasks.isEmpty()) return;

        for (String taskName : selectedTasks) {
            Task task = taskManagement.findTaskByName(taskName);
            if (task != null) taskManagement.assignTaskToEmployee(employee, task);
        }

        Serialization.saveEmployeeData(taskManagement.getMap());
        updateEmployeeTasks(employeeName, selectedTasks);
    }

    private void updateEmployeeTasks(String employee, List<String> tasks) {
        for (int i = 0; i < employeeTableModel.getRowCount(); i++) {
            if (employeeTableModel.getValueAt(i, 0).equals(employee)) {
                String existingTasks = (String) employeeTableModel.getValueAt(i, 1);
                if (existingTasks == null || existingTasks.isEmpty()) {
                    existingTasks = "";
                }

                String updatedTasks = existingTasks.isEmpty() ? String.join(", ", tasks)
                        : existingTasks + ", " + String.join(", ", tasks);
                employeeTableModel.setValueAt(updatedTasks, i, 1);
                break;
            }
        }
    }

    private String selectItemFromTable(DefaultTableModel tableModel, String message) {
        Object[] items = getColumnValues(tableModel, 0);
        if (items.length == 0) return null;
        return (String) JOptionPane.showInputDialog(this, message, "Select", JOptionPane.QUESTION_MESSAGE, null, items, items[0]);
    }

    private List<String> selectMultipleItemsFromTable(DefaultTableModel tableModel, String message) {
        JList<Object> list = new JList<>(getColumnValues(tableModel, 0));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(list), message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (result == JOptionPane.OK_OPTION) ? list.getSelectedValuesList().stream().map(String::valueOf).toList() : null;
    }

    private Object[] getColumnValues(DefaultTableModel tableModel, int column) {
        int rowCount = tableModel.getRowCount();
        Object[] items = new Object[rowCount];
        for (int i = 0; i < rowCount; i++) {
            items[i] = tableModel.getValueAt(i, column);
        }
        return items;
    }

    public void viewEmployee() {
        JFrame viewFrame = new JFrame("Employees and Their Tasks");
        viewFrame.setSize(800, 600);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Employee", "Tasks", "Status", "Total Duration"}, 0);
        JTable table = new JTable(model);

        Map<Employee, List<Task>> employeeTaskMap = Serialization.loadEmployeeData();
        List<Task> allTasks = Serialization.loadTaskData();

        if (employeeTaskMap != null && allTasks != null) {
            for (Map.Entry<Employee, List<Task>> entry : employeeTaskMap.entrySet()) {
                Employee employee = entry.getKey();
                List<Task> tasks = entry.getValue();

                if (tasks == null) {
                    model.addRow(new Object[]{employee.getIdEmployee(), employee.getName(), "No tasks assigned", "-", "0h"});
                    continue;
                }

                List<String> taskNames = new ArrayList<>();
                List<String> statuses = new ArrayList<>();
                int totalDuration = 0;

                for (Task task : tasks) {
                    taskNames.add(task.getTaskName());
                    statuses.add(task.getStatusTask());
                    totalDuration += task.estimateDuration();
                }

                String taskListStr = String.join("; ", taskNames);
                String statusListStr = String.join("; ", statuses);

                model.addRow(new Object[]{employee.getIdEmployee(), employee.getName(), taskListStr, statusListStr, totalDuration + "h"});
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    public void modifyTaskStatus() {

        String selectedEmployeeName = selectItemFromTable(employeeTableModel, "Select employee:");
        if (selectedEmployeeName == null) return;

        Employee selectedEmployee = taskManagement.findEmployeeByName(selectedEmployeeName);
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found!");
            return;
        }

        List<Task> employeeTasks = taskManagement.getEmployeeTasks(selectedEmployee.getIdEmployee());
        if (employeeTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks assigned to this employee!");
            return;
        }

        String selectedTaskName = selectItemFromTable(taskTableModel, "Select task to modify status:");
        if (selectedTaskName == null) return;

        Task selectedTask = taskManagement.findTaskByName(selectedTaskName);
        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this, "Task not found!");
            return;
        }

        String[] statusOptions = {"Uncompleted", "Completed"};
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status for task:", "Modify Task Status",
                JOptionPane.QUESTION_MESSAGE, null, statusOptions, selectedTask.getStatusTask());

        if (newStatus == null || newStatus.equals(selectedTask.getStatusTask())) {
            JOptionPane.showMessageDialog(this, "Status is the same or invalid.");
            return;
        }

        taskManagement.modifyTaskStatus(selectedEmployee.getIdEmployee(), selectedTask.getIdTask(), newStatus);

        loadDataIntoTables();

        JOptionPane.showMessageDialog(this, "Task status updated successfully!");
    }

    public void filterEmployeeByWorkDuration() {

        Map<Employee, List<Task>> employeeTaskMap = Serialization.loadEmployeeData();
        List<Task> allTasks = Serialization.loadTaskData();

        if (employeeTaskMap.isEmpty() || allTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employee or task data loaded.");
            return;
        }

        JFrame viewFrame = new JFrame("Employees Who Worked More Than 40 Hours");
        viewFrame.setSize(800, 600);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Employee", "Total Duration"}, 0);
        JTable table = new JTable(model);

        Utility.filterEmployeeByWorkDuration(employeeTaskMap, taskManagement);

        for (Employee employee : employeeTaskMap.keySet()) {
            int totalDuration = taskManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());

            if (totalDuration > 40) {
                model.addRow(new Object[]{employee.getIdEmployee(), employee.getName(), totalDuration + "h"});
            }
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"", "No employees found who worked more than 40 hours", ""});
        }


        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    public void countTasks() {

        Map<Employee, List<Task>> employeeTaskMap = Serialization.loadEmployeeData();
        List<Task> allTasks = Serialization.loadTaskData();

        if (employeeTaskMap.isEmpty() || allTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employee or task data loaded.");
            return;
        }

        JFrame viewFrame = new JFrame("Task Count (Completed / Uncompleted)");
        viewFrame.setSize(800, 600);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Employee", "Completed", "Uncompleted"}, 0);
        JTable table = new JTable(model);

        Map<String, Map<String, Integer>> taskCountMap = Utility.countTasks(employeeTaskMap);

        for (String employeeName : taskCountMap.keySet()) {
            Map<String, Integer> taskStatusMap = taskCountMap.get(employeeName);
            int completedCount = taskStatusMap.getOrDefault("Completed", 0);
            int uncompletedCount = taskStatusMap.getOrDefault("Uncompleted", 0);

            model.addRow(new Object[]{employeeName, completedCount, uncompletedCount});
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"", "No tasks found", "No tasks found"});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Task Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.add(new Interface());
        frame.setVisible(true);
    }

}

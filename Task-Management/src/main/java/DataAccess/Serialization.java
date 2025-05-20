package DataAccess;

import DataModel.Employee;
import DataModel.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serialization {

    private static final String EMPLOYEE_FILE = "employees_data.ser";
    private static final String TASK_FILE = "tasks_data.ser";

    public static void saveEmployeeData(Map<Employee, List<Task>> map) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(EMPLOYEE_FILE))) {
            out.writeObject(map);
            System.out.println("Employee data saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Map<Employee, List<Task>> loadEmployeeData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(EMPLOYEE_FILE))) {
            Object obj = in.readObject();
            if (obj instanceof Map) {
                Map<Employee, List<Task>> map = (Map<Employee, List<Task>>) obj;
                return map;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No employee data found.");
        }
        return new HashMap<>();
    }

    public static void saveTaskData(List<Task> tasks) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TASK_FILE))) {
            out.writeObject(tasks);
            System.out.println("Task data saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Task> loadTaskData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TASK_FILE))) {
            Object obj = in.readObject();
            if (obj instanceof List) {
                List<Task> tasks = (List<Task>) obj;
                System.out.println("Task data loaded: " + tasks.size() + " tasks found.");
                return tasks;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No task data found.");
        }
        return new ArrayList<>();
    }

}

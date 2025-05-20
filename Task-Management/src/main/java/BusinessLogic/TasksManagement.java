package BusinessLogic;

import DataAccess.Serialization;
import DataModel.Employee;
import DataModel.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement {

    private Map<Employee, List<Task>> map;
    private List<Task> tasks;

    public TasksManagement() {
        Map<Employee, List<Task>> loadedData = Serialization.loadEmployeeData();
        List<Task> loadedTasks = Serialization.loadTaskData();

        this.map = (loadedData != null) ? loadedData : new HashMap<>();
        this.tasks = (loadedTasks != null) ? loadedTasks : new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        if (!map.containsKey(employee)) {
            map.put(employee, new ArrayList<>());
            Serialization.saveEmployeeData(map);
        }
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
            Serialization.saveTaskData(tasks);
        }
    }

    public void assignTaskToEmployee(Employee employee, Task task) {
        if (employee == null || task == null) return;

        if(!map.containsKey(employee)) {
            map.put(employee, new ArrayList<>());
        }

        if(!map.get(employee).contains(task)) {
            map.get(employee).add(task);
        }
        Serialization.saveEmployeeData(map);
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        List<Task> tasks = getEmployeeTasks(idEmployee);
        if (tasks == null || tasks.isEmpty()) return 0;

        int totalDuration = 0;
        for (Task task : tasks) {
            if ("Completed".equalsIgnoreCase(task.getStatusTask())) {
                totalDuration += task.estimateDuration();
            }
        }
        return totalDuration;
    }

    public void modifyTaskStatus(int idEmployee, int idTask, String newStatus) {
        List<Task> tasks = getEmployeeTasks(idEmployee);
        if (tasks == null || tasks.isEmpty()) {
            return;
        }

        for (Task task : tasks) {
            if (task.getIdTask() == idTask) {
                task.setStatusTask(newStatus);
                Serialization.saveTaskData(tasks);
                Serialization.saveEmployeeData(map);
                return;
            }
        }
    }

    public List<Task> getEmployeeTasks(int idEmployee) {
        for (Employee employee : map.keySet()) {
            if (employee.getIdEmployee() == idEmployee) {
                return new ArrayList<>(map.get(employee));
            }
        }
        return new ArrayList<>();
    }

    public Employee findEmployeeByName(String name) {
        for (Employee employee : map.keySet()) {
            if (employee.getName().equalsIgnoreCase(name)) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(map.keySet());
    }

    public void clearData() {
        map.clear();
        tasks.clear();
        Serialization.saveEmployeeData(map);
        Serialization.saveTaskData(tasks);
    }

    public Task findTaskByName(String taskName) {
        for (Task task : tasks) {
            if (task.getTaskName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }

        for (List<Task> taskList : map.values()) {
            for (Task task : taskList) {
                if (task.getTaskName().equalsIgnoreCase(taskName)) {
                    return task;
                }
            }
        }

        return null;
    }

    public Map<Employee, List<Task>> getMap() {
        return map;
    }

}

package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

    public static void filterEmployeeByWorkDuration(Map<Employee, List<Task>> map, TasksManagement tasksManagement)
    {
        for(Employee employee: map.keySet())
        {
            int totalDuration = tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());

            if(totalDuration > 40)
            {
                System.out.println(employee.getName() + " has worked " + totalDuration + " hours");
            }
        }
    }

    public static Map<String, Map<String, Integer>> countTasks(Map<Employee, List<Task>> map) {
        Map<String, Map<String, Integer>> employeeTaskStatusMap = new HashMap<>();

        for (Employee employee : map.keySet()) {
            int completed = 0;
            int uncompleted = 0;

            for (Task task : map.get(employee)) {
                if ("Completed".equals(task.getStatusTask())) {
                    completed++;
                } else if ("Uncompleted".equals(task.getStatusTask())) {
                    uncompleted++;
                }
            }

            Map<String, Integer> taskStatusCount = new HashMap<>();
            taskStatusCount.put("Completed", completed);
            taskStatusCount.put("Uncompleted", uncompleted);

            employeeTaskStatusMap.put(employee.getName(), taskStatusCount);
        }

        return employeeTaskStatusMap;
    }

}

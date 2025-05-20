package DataModel;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {

    private List<Task> subTask = new ArrayList<>();

    public ComplexTask(String statusTask,String taskName, List<Task> subTask) {
        super(statusTask, taskName);
        this.subTask = subTask;
    }

    @Override
    public int estimateDuration() {
        int totalDuratiom = 0;
        for (Task task : subTask) {
            totalDuratiom += task.estimateDuration();
        }
        return totalDuratiom;
    }

    public void addTask(Task task) {
        subTask.add(task);
    }

    public void deleteTask(Task task) {
        subTask.remove(task);
    }

    @Override
    public String toString() {
        return " " + getTaskName();
    }


}

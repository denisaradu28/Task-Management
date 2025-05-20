package DataModel;

import java.io.Serializable;

public sealed abstract class Task implements Serializable permits SimpleTask, ComplexTask{

    private int idTask;
    private String statusTask;
    private String taskName;

    public Task(String statusTask, String taskName) {
        this.idTask++;
        this.statusTask = statusTask;
        this.taskName = taskName;
    }

    public abstract int estimateDuration();

    public int getIdTask() {
        return idTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    public String getTaskName() {
        return taskName;
    }
}

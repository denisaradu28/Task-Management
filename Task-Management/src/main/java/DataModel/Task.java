package DataModel;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public sealed abstract class Task implements Serializable permits SimpleTask, ComplexTask {

    private static final AtomicInteger idGenerator = new AtomicInteger(1); // Generator global de ID-uri
    private final int idTask;
    private String statusTask;
    private String taskName;

    public Task(String statusTask, String taskName) {
        this.idTask = idGenerator.getAndIncrement(); // Incrementăm corect ID-ul
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

    @Override
    public String toString() {
        return taskName + " (ID: " + idTask + ", Status: " + statusTask + ")";
    }
}

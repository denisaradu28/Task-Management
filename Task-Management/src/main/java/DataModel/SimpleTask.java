package DataModel;

public final class SimpleTask extends Task {

    private int startHour;
    private int endHour;

    public SimpleTask(String statusTask,String taskName, int startHour, int endHour) {
        super(statusTask, taskName);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    @Override
    public int estimateDuration() {

        if(endHour < startHour)
            return (24 - startHour) + endHour;
        else
            return endHour - startHour;
    }

    @Override
    public String toString() {
        return " " + getTaskName();
    }
}

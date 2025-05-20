package DataModel;

import java.io.Serializable;

public class Employee implements Serializable {

    private static int idCounter = 1;
    private int idEmployee;
    private String name;

    public Employee(String name) {
        this.idEmployee = idCounter++;
        this.name = name;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public String getName() {
        return name;
    }

}

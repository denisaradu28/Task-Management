package DataModel;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee that = (Employee) o;
        return idEmployee == that.idEmployee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee);
    }

}

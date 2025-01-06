package telran.employees.db.jpa;
import org.json.JSONObject;

import jakarta.persistence.*;
import telran.employees.*;
@Table(name="employees")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
@DiscriminatorValue("Employee")
public class EmployeeEntity {
    @Id
    private long id;
    @Column(name="basic_salary")
    private int basicSalary;
    private String department;
    protected void fromEmployeeDto(Employee empl) {
        id = empl.getId();
        basicSalary = empl.getBasicSalary();
        department = empl.getDepartment();
    }
    protected void toJsonObject(JSONObject jsonObj) {
        jsonObj.put("department", department);
        jsonObj.put("basicSalary", basicSalary);
        jsonObj.put("id", id);
        
    }
    
}
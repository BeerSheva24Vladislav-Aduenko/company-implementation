package telran.employees.db.jpa;

import org.json.JSONObject;

import jakarta.persistence.*;
import telran.employees.*;

@Entity
@DiscriminatorValue("WageEmployee")
public class WageEmployeeEntity extends EmployeeEntity {
    int wage;
    int hours;

    @Override
    protected void fromEmployeeDto(Employee empl) {
        super.fromEmployeeDto(empl);
        wage = ((WageEmployee) empl).getWage();
        hours = ((WageEmployee) empl).getHours();
    }

    @Override
    protected void toJsonObject(JSONObject jsonObj) {
        super.toJsonObject(jsonObj);
        jsonObj.put("wage", wage);
        jsonObj.put("hours", hours);

    }

}
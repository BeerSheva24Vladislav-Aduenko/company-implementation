package telran.employees.db.jpa;

import java.lang.reflect.Constructor;

import org.json.JSONObject;

import telran.employees.Employee;

public class EmployeesMapper {
private static final String PACKAGE = "telran.employees.";
private static final String CLASS_NAME = "className";
private static final String PACKAGE_JPA = PACKAGE + "db." + "jpa.";

public static Employee toEmployeeDtoFromEntity(EmployeeEntity entity) {
    String entityClassName = entity.getClass().getSimpleName();
    String dtoClassName = PACKAGE + entityClassName.replaceAll("Entity", "");
    JSONObject jsonObj = new JSONObject();
    jsonObj.put(CLASS_NAME, dtoClassName);
    entity.toJsonObject(jsonObj);
    return Employee.getEmployeeFromJSON(jsonObj.toString());
}
public static EmployeeEntity toEmployeeEntityFromDto(Employee empl) {
    String entityClassName = PACKAGE_JPA + empl.getClass().getSimpleName() + "Entity";
    try {
        @SuppressWarnings("unchecked")
        Class<EmployeeEntity> clazz = (Class<EmployeeEntity>) Class.forName(entityClassName);
        Constructor<EmployeeEntity> constructor = clazz.getConstructor();
        EmployeeEntity resEntity = constructor.newInstance();
        resEntity.fromEmployeeDto(empl);
        return resEntity;
        
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
}
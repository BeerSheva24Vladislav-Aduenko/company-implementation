package telran.employees.db.jpa;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import telran.employees.Company;
import telran.employees.db.CompanyDbImpl;
import telran.employees.db.CompanyRepository;
import telran.employees.db.jpa.config.EmployeesPersistenceUnitInfo;

public class EmployeesInitialJpaTest {
    @Test
    void getEmployeesTest() {
        HashMap<String, Object> hibernateProperties = new HashMap<String, Object>(
            
        ) {
            {
                put("hibernate.hbm2ddl.auto", "update");
            }
        };
        CompanyRepository repository =
         new CompanyRepositoryJpaImpl(new EmployeesPersistenceUnitInfo(), hibernateProperties);
         Company company = new CompanyDbImpl(repository);
         company.forEach(System.out::println);

    }
}
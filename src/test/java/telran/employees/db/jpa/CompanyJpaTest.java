package telran.employees.db.jpa;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;

import telran.employees.CompanyTest;
import telran.employees.db.CompanyDbImpl;
import telran.employees.db.CompanyRepository;

public class CompanyJpaTest extends CompanyTest {
    HashMap<String, Object> properties = new HashMap<>() {
        {
            put("hibernate.hbm2ddl.auto", "create");
        }
    };
    CompanyRepository repository = new CompanyRepositoryJpaImpl(new EmployeesTestPersistenceUnitInfo(), properties);

    @BeforeEach
    @Override
    protected void setCompany() {
        company = new CompanyDbImpl(repository);
        super.setCompany();
    }
}
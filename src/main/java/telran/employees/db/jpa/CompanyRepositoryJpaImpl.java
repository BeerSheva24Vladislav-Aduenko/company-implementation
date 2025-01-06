package telran.employees.db.jpa;

import java.lang.reflect.Constructor;
import java.util.*;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceUnitInfo;
import telran.employees.Employee;
import telran.employees.db.CompanyRepository;

public class CompanyRepositoryJpaImpl implements CompanyRepository {
    private EntityManager em;

    public CompanyRepositoryJpaImpl(PersistenceUnitInfo persistenceUnit,
            HashMap<String, Object> properties) {
        try {
            String providerName = persistenceUnit.getPersistenceProviderClassName();
            @SuppressWarnings("unchecked")
            Class<PersistenceProvider> clazz = (Class<PersistenceProvider>) Class.forName(providerName);
            Constructor<PersistenceProvider> constructor = clazz.getConstructor();
            PersistenceProvider provider = constructor.newInstance();
            EntityManagerFactory emf = provider.createContainerEntityManagerFactory(persistenceUnit, properties);
            em = emf.createEntityManager();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Employee> getEmployees() {
        TypedQuery<EmployeeEntity> query = em.createQuery("select empl from EmployeeEntity empl",
                EmployeeEntity.class);
        return toEmployeeList(query.getResultList());
    }

    private List<Employee> toEmployeeList(List<EmployeeEntity> resultList) {
        return resultList.stream().map(EmployeesMapper::toEmployeeDtoFromEntity).toList();
    }

    @Override
    public void insertEmployee(Employee empl) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();

            var emplEntity = em.find(EmployeeEntity.class, empl.getId());
            if (emplEntity != null) {
                throw new IllegalStateException("Employee already exists");

            }
            EmployeeEntity employee = EmployeesMapper.toEmployeeEntityFromDto(empl);
            em.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

    }

    @Override
    public Employee findEmployee(long id) {
        EmployeeEntity emplEntity = em.find(EmployeeEntity.class, id);
        return emplEntity == null ? null : EmployeesMapper.toEmployeeDtoFromEntity(emplEntity);
    }

    @Override
    public Employee removeEmployee(long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            EmployeeEntity emplEntity = em.find(EmployeeEntity.class, id);
        if (emplEntity == null) {
            throw new NoSuchElementException("Employee doesn't exist");
        }
        em.remove(emplEntity);
        transaction.commit();
        return EmployeesMapper.toEmployeeDtoFromEntity(emplEntity);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
        
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
       TypedQuery<EmployeeEntity> query = em.createQuery("select empl from EmployeeEntity empl where department=?1", EmployeeEntity.class);
       query.setParameter(1, department);
       return toEmployeeList(query.getResultList());
    }

    @Override
    public List<String> findDepartments() {
        TypedQuery<String> query = em.createQuery("select distinct department from EmployeeEntity", String.class);
        return query.getResultList();
    }

}
package telran.employees.db;

import java.util.Iterator;
import java.util.List;

import telran.employees.*;

public class CompanyDbImpl implements Company{
    private CompanyRepository repository;
    
    public CompanyDbImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterator<Employee> iterator() {
        return repository.getEmployees().iterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        repository.insertEmployee(empl);
    }

    @Override
    public Employee getEmployee(long id) {
        return repository.findEmployee(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        return repository.removeEmployee(id);
    }

    @Override
    public int getDepartmentBudget(String department) {
        List<Employee> employees = repository.getEmployeesByDepartment(department);
        return employees.stream().mapToInt(Employee::computeSalary).sum();
    }

    @Override
    public String[] getDepartments() {
       List<String> listDepartments = repository.findDepartments();
       return listDepartments.toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getManagersWithMostFactor'");
    }

}
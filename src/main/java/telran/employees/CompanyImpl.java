package telran.employees;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.*;

import telran.io.Persistable;

public class CompanyImpl implements Company, Persistable {

    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managersFactor = new TreeMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    private class CompanyIterator implements Iterator<Employee> {
        Iterator<Employee> iterator = employees.values().iterator();
        Employee lastIterated;

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Employee next() {
            lastIterated = iterator.next();
            return lastIterated;
        }

        @Override
        public void remove() {
            iterator.remove();
            removeFromIndexMaps(lastIterated);
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyIterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        try {
            writeLock.lock();
            long id = empl.getId();
            if (employees.putIfAbsent(id, empl) != null) {
                throw new IllegalStateException("Already exists employee " + id);
            }
            addIndexMaps(empl);
        } finally {
            writeLock.unlock();
        }

    }

    private void addIndexMaps(Employee empl) {
        employeesDepartment.computeIfAbsent(empl.getDepartment(), k -> new ArrayList<>()).add(empl);
        if (empl instanceof Manager manager) {
            managersFactor.computeIfAbsent(manager.getFactor(), k -> new ArrayList<>()).add(manager);
        }
    }

    @Override
    public Employee getEmployee(long id) {
        try {
            readLock.lock();
            return employees.get(id);
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public Employee removeEmployee(long id) {
        try {
            writeLock.lock();
            Employee empl = employees.remove(id);
            if (empl == null) {
                throw new NoSuchElementException("Not found employee " + id);
            }
            removeFromIndexMaps(empl);
            return empl;
        } finally {
            writeLock.unlock();
        }

    }

    private void removeFromIndexMaps(Employee empl) {
        removeIndexMap(empl.getDepartment(), employeesDepartment, empl);
        if (empl instanceof Manager manager) {
            removeIndexMap(manager.getFactor(), managersFactor, manager);
        }
    }

    private <K, V extends Employee> void removeIndexMap(K key, Map<K, List<V>> map, V empl) {
        List<V> list = map.get(key);
        list.remove(empl);
        if (list.isEmpty()) {
            map.remove(key);
        }
    }

    @Override
    public int getDepartmentBudget(String department) {
        try {
            readLock.lock();
            return employeesDepartment.getOrDefault(department, Collections.emptyList())
                    .stream().mapToInt(Employee::computeSalary).sum();
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public String[] getDepartments() {
        try {
            readLock.lock();
            return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        try {
            readLock.lock();
            Manager[] res = new Manager[0];
            if (!managersFactor.isEmpty()) {
                res = managersFactor.lastEntry().getValue().toArray(res);
            }
            return res;
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public void saveToFile(String fileName) {
        try {
            readLock.lock();
            try (PrintWriter writer = new PrintWriter(fileName)) {
                forEach(writer::println);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public void restoreFromFile(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            reader.lines().map(Employee::getEmployeeFromJSON).forEach(this::addEmployee);
        } catch (NoSuchFileException e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
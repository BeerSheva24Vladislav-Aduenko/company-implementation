package telran.employees.db.jpa;


import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import telran.employees.db.jpa.config.EmployeesPersistenceUnitInfo;

public class EmployeesTestPersistenceUnitInfo extends EmployeesPersistenceUnitInfo{
 @Override
 public DataSource getNonJtaDataSource() {
    HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:h2:mem:testdb");
		ds.setPassword("");
		ds.setUsername("sa");
		ds.setDriverClassName("org.h2.Driver");
		
		return ds;
 }
}
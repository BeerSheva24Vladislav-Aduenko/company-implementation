package telran.employees.db.jpa.config;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class EmployeesPersistenceUnitInfo implements PersistenceUnitInfo {

    @Override
    public String getPersistenceUnitName() {
        return "employees-persistence-unit";
    }

    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    @Override
    public String getScopeAnnotationName() {
        return null;
    }

    @Override
    public List<String> getQualifierAnnotationNames() {
        return null;
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return null;
    }

    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:postgresql://%s:5432/postgres",
                System.getenv("POSTGRES_HOST")));
        ds.setPassword(System.getenv("POSTGRES_PASSWORD"));
        ds.setUsername("postgres");
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    @Override
    public List<String> getMappingFileNames() {
        return null;
    }
    @Override
    public List<URL> getJarFileUrls() {
        return null;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of("telran.employees.db.jpa.EmployeeEntity","telran.employees.db.jpa.WageEmployeeEntity",
         "telran.employees.db.jpa.ManagerEntity","telran.employees.db.jpa.SalesPersonEntity");
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;

    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return null;
    }
    @Override
    public ValidationMode getValidationMode() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
       
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }

}
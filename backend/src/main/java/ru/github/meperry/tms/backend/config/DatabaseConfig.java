package ru.github.meperry.tms.backend.config;

import java.util.Properties;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Islam Khabibullin
 */
@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {

  @Value("${ds.url}")
  private String url;

  @Value("${ds.driver}")
  private String driver;

  @Value("${ds.username}")
  private String username;

  @Value("${ds.password}")
  private String password;

  @Value("${jpa.ddl-auto}")
  private String ddlAuto;

  @Value("${jpa.generate-ddl}")
  private boolean generateDdl;

  @Value("${jpa.database-platform}")
  private String databasePlatform;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(url);
    dataSource.setDriverClassName(driver);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setJpaProperties(jpaProperties());
    em.setPackagesToScan("ru.github.meperry.tms.backend.model", "ru.github.meperry.tms.backend.security.model");
    return em;
  }

  private Properties jpaProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
    properties.setProperty("hibernate.ddl-auto", ddlAuto);
    properties.setProperty("hibernate.generate-ddl", String.valueOf(generateDdl));
    properties.setProperty("hibernate.dialect", databasePlatform);
    return properties;
  }
}

package com.github.echological.akint.datasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.avrist.urlshortener.datasource.db1.repository",
        entityManagerFactoryRef = "urlShortenerEntityManager",
        transactionManagerRef = "urlShortenerTrxManager"
)
public class UrlShortenerDBConfig {

    private final Environment env;

    @Autowired
    public UrlShortenerDBConfig(Environment env) {
        this.env = env;
    }

    private String prop(String key){
        return env.getProperty(String.format("db.urlshortener.%s", key));
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean urlShortenerEntityManager(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(urlShortenerDatasource());
        em.setPackagesToScan(("com.avrist.urlshortener.datasource.db1.entity"));

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, String> prop = new HashMap<>();
        prop.put("hibernate.hbm2ddl.auto", prop("hibernate.ddl-auto"));
        prop.put("hibernate.dialect", prop("hibernate.dialect"));
        prop.put("hibernate.show_sql", prop("hibernate.show-sql"));
        prop.put("hibenate.format_sql", prop("hibernate.format-sql"));
        em.setJpaPropertyMap(prop);
        return em;
    }

    @Bean
    public DataSource urlShortenerDatasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(prop("hikari.driver-class-name"));
        dataSource.setJdbcUrl(prop("hikari.url"));
        dataSource.setUsername(prop("hikari.username"));
        dataSource.setPassword(prop("hikari.password"));
        dataSource.setMaximumPoolSize(Integer.parseInt(prop("hikari.max-pool-size")));
        dataSource.addDataSourceProperty("encrypt", true);
        dataSource.addDataSourceProperty("trustServerCertificate", true);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager urlShortenerTrxManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(urlShortenerEntityManager().getObject());
        return transactionManager;
    }
}

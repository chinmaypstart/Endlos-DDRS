package com.endlosiot.common.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

/**
 * This is a hikari connection pool configuration class where all database related configuration
 * will be done.
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration(
        exclude = { //
                DataSourceAutoConfiguration.class, //
                DataSourceTransactionManagerAutoConfiguration.class
        })
@PropertySource(value = {"application.properties"})  //file:${catalina.base}/conf/we-plus-api/db.properties
public class HikariCPConfiguration {

    @Value("${datasource.dataSourceClassName}")
    private String dataSourceClassName;

    @Value("${datasource.user}")
    private String user;

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.databaseName}")
    private String databaseName;

    @Value("${datasource.portNumber}")
    private String portNumber;

    @Value("${datasource.hikaricp.autoCommit}")
    private String autoCommit;

    @Value("${datasource.hikaricp.connectionTimeout}")
    private int connectionTimeout;

    @Value("${datasource.hikaricp.idleTimeout}")
    private int idleTimeout;

    @Value("${datasource.hikaricp.maxLifetime}")
    private int maxLifetime;

    @Value("${datasource.hikaricp.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${datasource.hikaricp.minimumIdle}")
    private int minimumIdle;

    @Bean(destroyMethod = "close")
    public HikariDataSource primaryDataSource() {
        Properties dataSourceProperty = new Properties();
        dataSourceProperty.put("user", user);
        dataSourceProperty.put("password", password);
        dataSourceProperty.put("databaseName", databaseName);
        dataSourceProperty.put("portNumber", portNumber);

        Properties connectionPoolProperty = new Properties();
        connectionPoolProperty.put("dataSourceClassName", dataSourceClassName);
        connectionPoolProperty.put("maximumPoolSize", maximumPoolSize);
        connectionPoolProperty.put("connectionTimeout", connectionTimeout);
        connectionPoolProperty.put("idleTimeout", idleTimeout);
        connectionPoolProperty.put("dataSourceProperties", dataSourceProperty);
        connectionPoolProperty.put("autoCommit", autoCommit);
        connectionPoolProperty.put("maxLifetime", maxLifetime);
        connectionPoolProperty.put("minimumIdle", minimumIdle);

        HikariConfig hikariConfig = new HikariConfig(connectionPoolProperty);
        hikariConfig.addDataSourceProperty("reWriteBatchedInserts", true);

        return new HikariDataSource(hikariConfig);
    }
}

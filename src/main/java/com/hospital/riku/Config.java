package com.hospital.riku;

import org.hibernate.cfg.Configuration;


public class Config {
    private Configuration config;
    public Configuration buildConfig(){
        config = new Configuration()
                .setProperty("hibernate.connection.url","jdbc:postgresql://localhost:5432/hospital")
                .setProperty("hibernate.connection.driver_class","org.postgresql.Driver")
                .setProperty("hibernate.connection.user","manabendu")
                .setProperty("hibernate.connection.password","Mk7riku@23")
                .setProperty("hibernate.show_sql","false6")
                .setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.format_sql","false")
                .setProperty("hibernate.hbm2ddl.auto","update");

        return config;
    }
}

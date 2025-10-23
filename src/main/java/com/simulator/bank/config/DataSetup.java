package com.simulator.bank.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataSetup {
    private static BasicDataSource ds;

    static {
        ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/bankdb");
        ds.setUsername("root");
        ds.setPassword("password");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static DataSource getDataSource() {
        return ds;
    }
}

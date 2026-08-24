package com.pe.advanced.dao.impl.hibernate;

public class H2QueryFactory implements DbQueryFactory {

    public static final H2QueryFactory instance = new H2QueryFactory();

    private H2QueryFactory() {
        // Singleton
    }

    @Override
    public String disableForeignKeys() {
        return "SET REFERENTIAL_INTEGRITY FALSE";
    }

    @Override
    public String enableForeignKeys() {
        return "SET REFERENTIAL_INTEGRITY TRUE";
    }

    @Override
    public String selectAllTableNames() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'";
    }
}
package com.pe.advanced.dao.impl.hibernate;

public class HsqldbQueryFactory implements DbQueryFactory {

    public static final HsqldbQueryFactory instance = new HsqldbQueryFactory();

    private HsqldbQueryFactory() {
        // Singleton
    }

    @Override
    public String disableForeignKeys() {
        return "SET DATABASE REFERENTIAL INTEGRITY FALSE";
    }

    @Override
    public String enableForeignKeys() {
        return "SET DATABASE REFERENTIAL INTEGRITY TRUE";
    }

    @Override
    public String selectAllTableNames() {
        return "SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_SCHEMA) = 'PUBLIC'";
    }
}

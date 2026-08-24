package com.pe.advanced.dao.impl.hibernate;

public interface DbQueryFactory {

    String disableForeignKeys();

    String enableForeignKeys();

    String selectAllTableNames();
}

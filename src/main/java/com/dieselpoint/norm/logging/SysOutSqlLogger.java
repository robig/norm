package com.dieselpoint.norm.logging;

public class SysOutSqlLogger implements ISqlLogger {

    @Override
    public void logSelect(String sql) {
	System.out.println("Select: " + sql);
    }

    @Override
    public void logInsert(String sql) {
	System.out.println("Insert: " + sql);
    }

    @Override
    public void logUpdate(String sql) {
	System.out.println("Update: " + sql);
    }

    @Override
    public void logCreateTable(String sql) {
	System.out.println("createTable: " + sql);
    }

    @Override
    public void logDelete(String sql) {
	System.out.println("delete: " + sql);
    }

    @Override
    public void logAny(String sql) {
	System.out.println("SQL: " + sql);
    }

}

package com.dieselpoint.norm.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4JSqlLogger implements ISqlLogger {

    private static final Logger LOG = LoggerFactory.getLogger("NormSQL");

    @Override
    public void logSelect(String sql) {
	LOG.debug("Select: {}", sql);
    }

    @Override
    public void logInsert(String sql) {
	LOG.debug("Insert: {}", sql);
    }

    @Override
    public void logUpdate(String sql) {
	LOG.debug("Update: {}", sql);
    }

    @Override
    public void logCreateTable(String sql) {
	LOG.debug("createTable: {}", sql);
    }

    @Override
    public void logDelete(String sql) {
	LOG.debug("delete: {}", sql);
    }

    @Override
    public void logAny(String sql) {
	LOG.debug("SQL: {}", sql);
    }

}

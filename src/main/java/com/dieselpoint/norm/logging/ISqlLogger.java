package com.dieselpoint.norm.logging;

public interface ISqlLogger {
    public void logSelect(String sql);

    public void logInsert(String sql);

    public void logUpdate(String sql);

    public void logDelete(String sql);

    public void logCreateTable(String sql);

    public void logAny(String sql);
}

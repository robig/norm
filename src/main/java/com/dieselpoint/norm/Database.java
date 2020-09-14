package com.dieselpoint.norm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dieselpoint.norm.logging.ISqlLogger;
import com.dieselpoint.norm.sqlmakers.SqlMaker;
import com.dieselpoint.norm.sqlmakers.StandardSqlMaker;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Provides methods to access a database.
 */
public class Database {

    private static final Logger LOG = LoggerFactory.getLogger(Database.class);

    protected SqlMaker sqlMaker = new StandardSqlMaker();
    protected DataSource ds;
    protected ISqlLogger sqlLogger = null;

    protected String dataSourceClassName = System.getProperty("norm.dataSourceClassName");
    protected String driverClassName = System.getProperty("norm.driverClassName");
    protected String jdbcUrl = System.getProperty("norm.jdbcUrl");
    protected String serverName = System.getProperty("norm.serverName");
    protected String databaseName = System.getProperty("norm.databaseName");
    protected String user = System.getProperty("norm.user");
    protected String password = System.getProperty("norm.password");
    protected String logger = System.getProperty("norm.logger", "com.dieselpoint.norm.logging.SLF4JSqlLogger");
    protected int maxPoolSize = 10;

    protected Map<String, String> dataSourceProperties = new HashMap<>();

    /**
     * Set the maker object for the particular flavor of sql.
     */
    public void setSqlMaker(SqlMaker sqlMaker) {
	this.sqlMaker = sqlMaker;
    }

    public SqlMaker getSqlMaker() {
	return sqlMaker;
    }

    /**
     * Provides the DataSource used by this database. Override this method to change
     * how the DataSource is created or configured.
     */
    protected DataSource getDataSource() throws SQLException {
	HikariConfig config = new HikariConfig();
	config.setMaximumPoolSize(maxPoolSize);

	if (logger != null) {
	    try {
		sqlLogger = (ISqlLogger) Class.forName(logger).newInstance();
	    } catch (Exception e) {

	    }
	}

	if (dataSourceClassName != null) {
	    config.setDataSourceClassName(dataSourceClassName);
	}

	if (driverClassName != null) {
	    config.setDriverClassName(driverClassName);
	}

	if (jdbcUrl != null) {
	    config.setJdbcUrl(jdbcUrl);
	}

	addDataSourceProperty("serverName", serverName);
	addDataSourceProperty("databaseName", databaseName);
	addDataSourceProperty("user", user);
	addDataSourceProperty("password", password);

	for (Map.Entry<String, String> entry : dataSourceProperties.entrySet()) {
	    config.addDataSourceProperty(entry.getKey(), entry.getValue());
	}

	/*
	 * addConfigProperty(config, "serverName", serverName);
	 * addConfigProperty(config, "databaseName", databaseName);
	 * addConfigProperty(config, "user", user); addConfigProperty(config,
	 * "password", password);
	 */

	config.setLeakDetectionThreshold(30000);

	LOG.debug("connecting to %s", jdbcUrl);
	return new HikariDataSource(config);
    }

    public void addDataSourceProperty(String name, String value) {
	dataSourceProperties.put(name, value);
    }

    /*
     * private void addConfigProperty(HikariConfig config, String name, String
     * value) { if (value != null) { config.addDataSourceProperty(name, value); } }
     */

    /**
     * Create a query using straight SQL. Overrides any other methods like .where(),
     * .orderBy(), etc.
     * 
     * @param sql  The SQL string to use, may include ? parameters.
     * @param args The parameter values to use in the query.
     */
    public Query sql(String sql, Object... args) {
	return new Query(this).sql(sql, args);
    }

    /**
     * Create a query with the given where clause.
     * 
     * @param where Example: "name=?"
     * @param args  The parameter values to use in the where, example: "Bob"
     */
    public Query where(String where, Object... args) {
	return new Query(this).where(where, args);
    }

    /**
     * Create a query with the given "order by" clause.
     */
    public Query orderBy(String orderBy) {
	return new Query(this).orderBy(orderBy);
    }

    /**
     * Returns a JDBC connection. Can be useful if you need to customize how
     * transactions work, but you shouldn't normally need to call this method. You
     * must close the connection after you're done with it.
     */
    public Connection getConnection() {
	try {

	    if (ds == null) {
		ds = getDataSource();
	    }
	    return ds.getConnection();

	} catch (Throwable t) {
	    throw new DbException(t);
	}
    }

    /**
     * Simple, primitive method for creating a table based on a pojo. Does not add
     * indexes or implement complex data types. Probably not suitable for production
     * use.
     */
    public Query createTable(Class<?> clazz) {
	return new Query(this).createTable(clazz);
    }

    /**
     * Insert a row into a table. The row pojo can have a @Table annotation to
     * specify the table, or you can specify the table with the .table() method.
     */
    public Query insert(Object row) {
	return new Query(this).insert(row);
    }

    /**
     * See {@link com.dieselpoint.norm.Query#generatedKeyReceiver(Object, String...)
     * generateKeyReceiver} method.
     */
    public Query generatedKeyReceiver(Object generatedKeyReceiver, String... generatedKeyNames) {
	return new Query(this).generatedKeyReceiver(generatedKeyReceiver, generatedKeyNames);
    }

    /**
     * Delete a row in a table. This method looks for an @Id annotation to find the
     * row to delete by primary key, and looks for a @Table annotation to figure out
     * which table to hit.
     */
    public Query delete(Object row) {
	return new Query(this).delete(row);
    }

    /**
     * Execute a "select" query and get some results. The system will create a new
     * object of type "clazz" for each row in the result set and add it to a List.
     * It will also try to extract the table name from a @Table annotation in the
     * clazz.
     */
    public <T> List<T> results(Class<T> clazz) {
	return new Query(this).results(clazz);
    }

    /**
     * Returns the first row in a query in a pojo. Will return it in a Map if a
     * class that implements Map is specified.
     */
    public <T> T first(Class<T> clazz) {
	return new Query(this).first(clazz);
    }

    /**
     * Update a row in a table. It will match an existing row based on the primary
     * key.
     */
    public Query update(Object row) {
	return new Query(this).update(row);
    }

    /**
     * Upsert a row in a table. It will insert, and if that fails, do an update with
     * a match on a primary key.
     */
    public Query upsert(Object row) {
	return new Query(this).upsert(row);
    }

    /**
     * Create a query and specify which table it operates on.
     */
    public Query table(String table) {
	return new Query(this).table(table);
    }

    /**
     * Start a database transaction. Pass the transaction object to each query or
     * command that should be part of the transaction using the .transaction()
     * method. Then call transaction.commit() or .rollback() to complete the
     * process. No need to close the transaction.
     * 
     * @return a transaction object
     */
    public Transaction startTransaction() {
	Transaction trans = new Transaction();
	trans.setConnection(getConnection());
	return trans;
    }

    /**
     * Create a query that uses this transaction object.
     */
    public Query transaction(Transaction trans) {
	return new Query(this).transaction(trans);
    }

    public void close() {
	if (ds instanceof HikariDataSource) {
	    ((HikariDataSource) ds).close();
	}
    }

    public void setDataSourceClassName(String dataSourceClassName) {
	this.dataSourceClassName = dataSourceClassName;
    }

    public void setDriverClassName(String driverClassName) {
	this.driverClassName = driverClassName;
    }

    public void setJdbcUrl(String jdbcUrl) {
	this.jdbcUrl = jdbcUrl;
    }

    public void setServerName(String serverName) {
	this.serverName = serverName;
    }

    public void setDatabaseName(String databaseName) {
	this.databaseName = databaseName;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public int getMaxPoolSize() {
	return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
	this.maxPoolSize = maxPoolSize;
    }

    public ISqlLogger getSqlLogger() {
	return sqlLogger;
    }
}

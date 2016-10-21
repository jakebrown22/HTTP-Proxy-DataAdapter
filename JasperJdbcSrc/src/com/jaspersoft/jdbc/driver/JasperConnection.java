/*******************************************************************************
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package com.jaspersoft.jdbc.driver;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * This class implements java.sql.Connection. JasperConnection is returned by JasperDriver.connect() call.
 * Creates no connection, does nothing, only creates JasperPreparedStatement and JasperStatement objects.
 *   
 * @author bpillai@xtivia.com
 */

import com.jaspersoft.web.rest.JasperRestConnector;

public class JasperConnection implements Connection {

	private JasperRestConnector restConnector =  null;
	
	public JasperConnection(JasperRestConnector rInvoke){
		this.restConnector = rInvoke;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Statement createStatement() throws SQLException {
		return new JasperStatement(this, restConnector);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new JasperPreparedStatement(this, restConnector, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return null;
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return sql;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return false;
	}

	@Override
	public void commit() throws SQLException {
	}

	@Override
	public void rollback() throws SQLException {
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return new JasperDatabaseMetaData(this, this.restConnector);
	}   

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return false;
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
	}

	@Override
	public String getCatalog() throws SQLException {
		return null;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {

	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
	}

	/**
	 * Ignore type and concurrency as we support read only Resultset
	 */
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return new JasperStatement(this, restConnector);
	}

	/**
	 * Ignore type and concurrency as we support read only Resultset
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new JasperPreparedStatement(this, restConnector, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return null;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return null;
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
	}

	@Override
	public int getHoldability() throws SQLException {
		return 0;
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return null;
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return null;
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
	}

	/**
	 * Ignore type and concurrency as we support read only Resultset
	 */
	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new JasperStatement(this, restConnector);
	}
	
	/**
	 * Ignore type and concurrency as we support read only Resultset
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new JasperPreparedStatement(this, restConnector, sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Ignore autoGeneratedKeys
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return new JasperPreparedStatement(this, restConnector, sql);
	}
	
	/**
	 * Ignore columnIndexes
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return new JasperPreparedStatement(this, restConnector, sql);
	}
	/**
	 * Ignore columnNames 
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return new JasperPreparedStatement(this, restConnector, sql);
	}

	@Override
	public Clob createClob() throws SQLException {
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return false;
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return null;
	}

	@Override
	public void setSchema(String schema) throws SQLException {
	}

	@Override
	public String getSchema() throws SQLException {
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return 0;
	}

}

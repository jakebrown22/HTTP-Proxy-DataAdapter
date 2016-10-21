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


import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.jaspersoft.web.rest.JasperRestConnector;

public class JasperPreparedStatement extends JasperStatement implements PreparedStatement {

	public JasperPreparedStatement(Connection conn, JasperRestConnector restConnector, String sql) throws SQLException {
		super(conn, restConnector);
		super.getCachedrowset().setCommand(sql);
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		super.initCachedRowSetWrapper();
		super.setCachedRowSetWrapper(super.getRestConnector().executeRequest(super.getCachedRowSetWrapper()));
		return new JasperResultSet(this, super.getRestConnector(), super.getCachedRowSetWrapper());
	}

	@Override
	public int executeUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean execute() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addBatch() throws SQLException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void clearParameters() throws SQLException {
		super.getCachedrowset().clearParameters();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return super.getCachedrowset().getMetaData();
	}
	
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return null;
	}
	
	/************************************************************************************************************************/
	/**
	 * Below methods are parameter setters for PreparedStatement. All parameters are set on the 
	 * CachedRowSet object.
	 */
	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		//super.getCachedrowset().setNull(parameterIndex, sqlType);
		// to fix a bug in CachedRowSet where sqltype is lost
		super.getCachedrowset().setObject(parameterIndex, new Object[]{null, sqlType});
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		super.getCachedrowset().setBoolean(parameterIndex, x);
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		super.getCachedrowset().setByte(parameterIndex, x);
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		super.getCachedrowset().setShort(parameterIndex, x);
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		super.getCachedrowset().setInt(parameterIndex, x);
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		super.getCachedrowset().setLong(parameterIndex, x);
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		super.getCachedrowset().setFloat(parameterIndex, x);
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		super.getCachedrowset().setDouble(parameterIndex, x);
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		super.getCachedrowset().setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		super.getCachedrowset().setString(parameterIndex, x);
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		super.getCachedrowset().setBytes(parameterIndex, x);
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		super.getCachedrowset().setDate(parameterIndex, x);
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		super.getCachedrowset().setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		super.getCachedrowset().setTimestamp(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		super.getCachedrowset().setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		super.getCachedrowset().setBinaryStream(parameterIndex, x, length);
	}
	
	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		super.getCachedrowset().setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		super.getCachedrowset().setObject(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		super.getCachedrowset().setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		super.getCachedrowset().setRef(parameterIndex, x);
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		super.getCachedrowset().setBlob(parameterIndex, x);
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		super.getCachedrowset().setClob(parameterIndex, x);
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		super.getCachedrowset().setArray(parameterIndex, x);
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		super.getCachedrowset().setDate(parameterIndex, x, cal);
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		super.getCachedrowset().setTime(parameterIndex, x, cal);
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		super.getCachedrowset().setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		super.getCachedrowset().setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		super.getCachedrowset().setURL(parameterIndex, x);

	}
	
	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		super.getCachedrowset().setRowId(parameterIndex, x);
	}

	@Override
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		super.getCachedrowset().setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		super.getCachedrowset().setNCharacterStream(parameterIndex, value, length);

	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		super.getCachedrowset().setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		super.getCachedrowset().setClob(parameterIndex, reader, length);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		super.getCachedrowset().setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		super.getCachedrowset().setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		super.getCachedrowset().setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		super.getCachedrowset().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		super.getCachedrowset().setAsciiStream(parameterIndex, x, (int) length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		super.getCachedrowset().setBinaryStream(parameterIndex, x, (int) length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		super.getCachedrowset().setCharacterStream(parameterIndex, reader, (int) length);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		super.getCachedrowset().setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		super.getCachedrowset().setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		super.getCachedrowset().setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		super.getCachedrowset().setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		super.getCachedrowset().setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		super.getCachedrowset().setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		super.getCachedrowset().setNClob(parameterIndex, reader);
	}
	
	// end of parameter setters for PreparedStatement
    /*******************************************************************************************/
}

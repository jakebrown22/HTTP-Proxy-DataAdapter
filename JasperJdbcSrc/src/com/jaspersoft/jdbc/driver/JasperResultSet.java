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

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.TimeZone;

import javax.sql.rowset.CachedRowSet;

import com.jaspersoft.web.rest.JasperRestConnector;
import com.jaspersoft.web.shared.CachedRowSetWrapper;

/**
 * This class extends the ResultSet interface and except for next and previous hands all methods over
 * to the CachedRowSet
 * @author bpillai@xtivia.com
 *
 */
public class JasperResultSet implements ResultSet {

	protected JasperRestConnector restConnector = null;
	protected CachedRowSet cachedrowset = null;
	private CachedRowSetWrapper cachedrowsetWrapper = null;
	private Statement statement = null;
    private long timeDifference = -1; // time diff with server 
    private TimeZone serverTZ = null;
    private TimeZone localTZ = null;
    
	
    public JasperResultSet(Statement stmt, JasperRestConnector rInvoke, CachedRowSetWrapper crw){
    	this.restConnector = rInvoke;
    	this.cachedrowsetWrapper = crw;
    	this.cachedrowset = crw.getCachedRowSet();
    	this.statement = stmt;

    	//Get the time difference between the JasperServer and driver. This time difference is added 
    	//in gets of Timestamp, Time and Date below.
    	try {
    		serverTZ = TimeZone.getTimeZone(rInvoke.getServerTimeZone());
    		localTZ = TimeZone.getDefault();
    		timeDifference = serverTZ.getRawOffset() - localTZ.getRawOffset();
    	} catch (SQLException e) {

    	}
    }
	
	@Override
	public boolean next() throws SQLException {
		
		int page = cachedrowset.getPageSize() > 0 ? cachedrowset.getRow() % cachedrowset.getPageSize() : 1;
		
		// we have reached the last row on the page and its not the first row
		if( ( 0 == page &&  ( cachedrowset.getRow() != 0  && (cachedrowset.getMaxRows() != cachedrowset.getPageSize() )))){ 
			getPage();
		}
		//this.currentRow++;
		return  this.cachedrowset.next();
	}
	
	private void getPage() throws SQLException{
		this.cachedrowset.release(); // get rid of the existing data
		//this.cachedrowsetWrapper.setCurrentRow(this.currentRow);
		this.cachedrowsetWrapper = this.restConnector.executeRequest(cachedrowsetWrapper);
		this.cachedrowset = cachedrowsetWrapper.getCachedRowSet();
		System.out.println("\nGot next page******************" +  "\n");
		
	}
	@Override
	public boolean previous() throws SQLException {
		int page = cachedrowset.getPageSize() > 0 ? cachedrowset.getRow() % cachedrowset.getPageSize() : 1;
		if(0 == page){
			getPage();
		}
		//this.currentRow--;
		return cachedrowset.previous();
	}
	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		cachedrowset.setFetchDirection(arg0);
	}
	@Override
	public void setFetchSize(int arg0) throws SQLException {
		cachedrowset.setFetchSize(arg0);
	}
	@Override
	public java.sql.Statement getStatement() throws SQLException {
		return this.statement;
	}
	@Override
	public void close() throws SQLException {
		cachedrowset.setFetchSize(0); // to overcome exception thrown at close of maxsize less than fetchsize
		cachedrowset.close();
		this.restConnector.closeResultSet(cachedrowsetWrapper); // close the resultset on the server too
	}
	@Override
	public int getType() throws SQLException {
		return cachedrowset.getType();
	}
	@Override
	public java.sql.ResultSetMetaData getMetaData() throws SQLException {
		return cachedrowset.getMetaData();
	}
	@Override
	public int getConcurrency() throws SQLException {
		return cachedrowset.getConcurrency();
	}
	@Override
	public int getFetchDirection() throws SQLException {
		return cachedrowset.getFetchDirection();
	}
	@Override
	public int getFetchSize() throws SQLException {
		return cachedrowset.getFetchSize();
	}
	@Override
	public int getHoldability() throws SQLException {
		return cachedrowset.getHoldability();
	}
	@Override
	public java.lang.String getCursorName() throws SQLException {
		return cachedrowset.getCursorName();
	}
	@Override
	public boolean wasNull() throws SQLException {
		return cachedrowset.wasNull();
	}
	/***************************************************************************************************/
	/**
	 * Below methods might have to be reworked in light of pagination 
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		return cachedrowset.isBeforeFirst();
	}
	@Override
	public boolean isClosed() throws SQLException {
		return cachedrowset.isClosed();
	}
	@Override
	public boolean isFirst() throws SQLException {
		return cachedrowset.isFirst();
	}
	@Override
	public boolean isLast() throws SQLException {
		return cachedrowset.isLast();
	}
	@Override
	public boolean last() throws SQLException {
		return cachedrowset.last();
	}
	@Override
	public boolean isAfterLast() throws SQLException {
		return cachedrowset.isAfterLast();
	}
	@Override
	public void moveToCurrentRow() throws SQLException {
		cachedrowset.moveToCurrentRow();
	}
	@Override
	public void afterLast() throws SQLException {
		cachedrowset.afterLast();
	}
	@Override
	public void beforeFirst() throws SQLException {
		cachedrowset.beforeFirst();
	}
	@Override
	public boolean first() throws SQLException {
		return cachedrowset.first();
	}
	/***************************************************************************************************/
	// All getter methods below are simply handed over to the CachedRowSet 
	@Override
	public java.sql.Date getDate(java.lang.String arg0, java.util.Calendar arg1) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0, arg1);
		if( t != null ){
			return new java.sql.Date(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Date getDate(int arg0) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0);
		if( t != null ){
			return new java.sql.Date(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Date getDate(int arg0, java.util.Calendar arg1) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0, arg1);
		if( t != null ){
			return new java.sql.Date(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Date getDate(java.lang.String arg0) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0);
		if( t != null ){
			return new java.sql.Date(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Time getTime(java.lang.String arg0) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0);
		if( t != null ){
			return new Time(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Time getTime(int arg0) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0);
		if( t != null ){
			return new Time(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Time getTime(java.lang.String arg0, java.util.Calendar arg1) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0, arg1);
		if( t != null ){
			return new Time(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Time getTime(int arg0, java.util.Calendar arg1) throws SQLException {
		java.sql.Timestamp t = getTimestamp(arg0, arg1);
		if( t != null ){
			return new Time(t.getTime());
		}else {
			return null;
		}	
	}
	@Override
	public java.sql.Timestamp getTimestamp(java.lang.String arg0, java.util.Calendar arg1) throws SQLException {
		java.sql.Timestamp t = cachedrowset.getTimestamp(arg0, arg1);
		if( null != t ){
			if( this.serverTZ.inDaylightTime( new Date(t.getTime())) ^ 
					this.localTZ.inDaylightTime(new Date(t.getTime()) ) )  {		
				t = new Timestamp(t.getTime() + timeDifference + 3600000); // add DST to time diff
			}	
			else{
				t = new Timestamp(t.getTime() + timeDifference);
			}	
		}
		return t;
	}
	@Override
	public java.sql.Timestamp getTimestamp(int arg0, java.util.Calendar arg1) throws SQLException {
		java.sql.Timestamp t = cachedrowset.getTimestamp(arg0, arg1);
		if( null != t ){
			if( this.serverTZ.inDaylightTime( new Date(t.getTime())) ^ 
					this.localTZ.inDaylightTime(new Date(t.getTime()) ) )  {		
				t = new Timestamp(t.getTime() + timeDifference + 3600000); // add DST to time diff
			}	
			else{
				t = new Timestamp(t.getTime() + timeDifference);
			}	
		}
		return t;
	}
	@Override
	public java.sql.Timestamp getTimestamp(java.lang.String arg0) throws SQLException {
		java.sql.Timestamp t = cachedrowset.getTimestamp(arg0);
		if( null != t ){
			if( this.serverTZ.inDaylightTime( new Date(t.getTime())) ^ 
					this.localTZ.inDaylightTime(new Date(t.getTime()) ) )  {		
				t = new Timestamp(t.getTime() + timeDifference + 3600000); // add DST to time diff
			}	
			else{
				t = new Timestamp(t.getTime() + timeDifference);
			}	
		}
		return t;
	}
	@Override
	public java.sql.Timestamp getTimestamp(int arg0) throws SQLException {
		java.sql.Timestamp t = cachedrowset.getTimestamp(arg0);
		if( null != t ){
			if( this.serverTZ.inDaylightTime( new Date(t.getTime())) ^ 
					this.localTZ.inDaylightTime(new Date(t.getTime()) ) )  {		
				t = new Timestamp(t.getTime() + timeDifference + 3600000); // add DST to time diff
			}	
			else{
				t = new Timestamp(t.getTime() + timeDifference);
			}	
		}
		return t;
	}
	@Override
	public boolean absolute(int arg0) throws SQLException {
		return cachedrowset.absolute(arg0);
	}
	@Override
	public void clearWarnings() throws SQLException {
		cachedrowset.clearWarnings();
	}
	@Override
	public int findColumn(java.lang.String arg0) throws SQLException {
		return cachedrowset.findColumn(arg0);
	}
	@Override
	public java.io.InputStream getAsciiStream(int arg0) throws SQLException {
		return cachedrowset.getAsciiStream(arg0);
	}
	@Override
	public java.io.InputStream getAsciiStream(java.lang.String arg0) throws SQLException {
		return cachedrowset.getAsciiStream(arg0);
	}
	@Override
	public java.math.BigDecimal getBigDecimal(java.lang.String arg0) throws SQLException {
		return cachedrowset.getBigDecimal(arg0);
	}
	@Override
	public java.math.BigDecimal getBigDecimal(java.lang.String arg0, int arg1) throws SQLException {
		return cachedrowset.getBigDecimal(arg0, arg1);
	}
	@Override
	public java.math.BigDecimal getBigDecimal(int arg0) throws SQLException {
		return cachedrowset.getBigDecimal(arg0);
	}
	@Override
	public java.math.BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
		return cachedrowset.getBigDecimal(arg0, arg1);
	}
	@Override
	public java.io.InputStream getBinaryStream(java.lang.String arg0) throws SQLException {
		return cachedrowset.getBinaryStream(arg0);
	}
	@Override
	public java.io.InputStream getBinaryStream(int arg0) throws SQLException {
		return cachedrowset.getBinaryStream(arg0);
	}
	@Override
	public java.sql.Blob getBlob(int arg0) throws SQLException {
		return cachedrowset.getBlob(arg0);
	}
	@Override
	public java.sql.Blob getBlob(java.lang.String arg0) throws SQLException {
		return cachedrowset.getBlob(arg0);
	}
	@Override
	public java.io.Reader getCharacterStream(java.lang.String arg0) throws SQLException {
		return cachedrowset.getCharacterStream(arg0);
	}
	@Override
	public java.io.Reader getCharacterStream(int arg0) throws SQLException {
		return cachedrowset.getCharacterStream(arg0);
	}
	@Override
	public java.sql.Clob getClob(java.lang.String arg0) throws SQLException {
		return cachedrowset.getClob(arg0);
	}
	@Override
	public java.sql.Clob getClob(int arg0) throws SQLException {
		return cachedrowset.getClob(arg0);
	}
	@Override
	public java.io.Reader getNCharacterStream(java.lang.String arg0) throws SQLException {
		return cachedrowset.getNCharacterStream(arg0);
	}
	@Override
	public java.io.Reader getNCharacterStream(int arg0) throws SQLException {
		return cachedrowset.getNCharacterStream(arg0);
	}
	@Override
	public java.sql.NClob getNClob(java.lang.String arg0) throws SQLException {
		return cachedrowset.getNClob(arg0);
	}
	@Override
	public java.sql.NClob getNClob(int arg0) throws SQLException {
		return cachedrowset.getNClob(arg0);
	}
	@Override
	public java.lang.String getNString(java.lang.String arg0) throws SQLException {
		return cachedrowset.getNString(arg0);
	}
	@Override
	public java.lang.String getNString(int arg0) throws SQLException {
		return cachedrowset.getNString(arg0);
	}
	@Override
	public int getRow() throws SQLException {
		return cachedrowset.getRow();
	}
	@Override
	public java.sql.RowId getRowId(int arg0) throws SQLException {
		return cachedrowset.getRowId(arg0);
	}
	@Override
	public java.sql.RowId getRowId(java.lang.String arg0) throws SQLException {
		return cachedrowset.getRowId(arg0);
	}
	@Override
	public java.sql.SQLXML getSQLXML(int arg0) throws SQLException {
		return cachedrowset.getSQLXML(arg0);
	}
	@Override
	public java.sql.SQLXML getSQLXML(java.lang.String arg0) throws SQLException {
		return cachedrowset.getSQLXML(arg0);
	}
	@Override
	public java.io.InputStream getUnicodeStream(java.lang.String arg0) throws SQLException {
		return cachedrowset.getUnicodeStream(arg0);
	}
	@Override
	public java.io.InputStream getUnicodeStream(int arg0) throws SQLException {
		return cachedrowset.getUnicodeStream(arg0);
	}
	@Override
	public java.sql.SQLWarning getWarnings() throws SQLException {
		return cachedrowset.getWarnings();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public java.lang.Object getObject(int arg0, java.lang.Class arg1) throws SQLException {
		Object o = cachedrowset.getObject(arg0, arg1);
		if(null != o){
			if(o instanceof java.sql.Timestamp){
				o = this.getTimestamp(arg0);
			}else if (o instanceof java.sql.Time){
				o = this.getTime(arg0);
			}else if (o instanceof java.sql.Date){
				o = this.getDate(arg0);
			}
		}
		return o;
	}
	@Override
	public java.lang.Object getObject(java.lang.String arg0, java.util.Map arg1) throws SQLException {
		Object o = cachedrowset.getObject(arg0, arg1);
		if(null != o){
			if(o instanceof java.sql.Timestamp){
				o = this.getTimestamp(arg0);
			}else if (o instanceof java.sql.Time){
				o = this.getTime(arg0);
			}else if (o instanceof java.sql.Date){
				o = this.getDate(arg0);
			}
		}
		return o;
	}
	@Override
	public java.lang.Object getObject(java.lang.String arg0) throws SQLException {
		Object o = cachedrowset.getObject(arg0);
		if(null != o){
			if(o instanceof java.sql.Timestamp){
				o = this.getTimestamp(arg0);
			}else if (o instanceof java.sql.Time){
				o = this.getTime(arg0);
			}else if (o instanceof java.sql.Date){
				o = this.getDate(arg0);
			}
		}
		return o;

	}
	@Override
	public java.lang.Object getObject(int arg0, java.util.Map arg1) throws SQLException {
		Object o = cachedrowset.getObject(arg0, arg1);
		if(null != o){
			if(o instanceof java.sql.Timestamp){
				o = this.getTimestamp(arg0);
			}else if (o instanceof java.sql.Time){
				o = this.getTime(arg0);
			}else if (o instanceof java.sql.Date){
				o = this.getDate(arg0);
			}
		}
		return o;

	}
	@Override
	public java.lang.Object getObject(int arg0) throws SQLException {
		Object o = cachedrowset.getObject(arg0);
		if(null != o){
			if(o instanceof java.sql.Timestamp){
				o = this.getTimestamp(arg0);
			}else if (o instanceof java.sql.Time){
				o = this.getTime(arg0);
			}else if (o instanceof java.sql.Date){
				o = this.getDate(arg0);
			}
		}
		return o;
	}
	@SuppressWarnings("unchecked")
	@Override
	public java.lang.Object getObject(java.lang.String arg0, java.lang.Class arg1) throws SQLException {
		Object o = cachedrowset.getObject(arg0, arg1);
		if(null != o){
			if(o instanceof java.sql.Timestamp){
				o = this.getTimestamp(arg0);
			}else if (o instanceof java.sql.Time){
				o = this.getTime(arg0);
			}else if (o instanceof java.sql.Date){
				o = this.getDate(arg0);
			}
		}
		return o;

	}
	@Override
	public boolean getBoolean(int arg0) throws SQLException {
		return cachedrowset.getBoolean(arg0);
	}
	@Override
	public boolean getBoolean(java.lang.String arg0) throws SQLException {
		return cachedrowset.getBoolean(arg0);
	}
	@Override
	public byte getByte(java.lang.String arg0) throws SQLException {
		return cachedrowset.getByte(arg0);
	}
	@Override
	public byte getByte(int arg0) throws SQLException {
		return cachedrowset.getByte(arg0);
	}
	@Override
	public short getShort(java.lang.String arg0) throws SQLException {
		return cachedrowset.getShort(arg0);
	}
	@Override
	public short getShort(int arg0) throws SQLException {
		return cachedrowset.getShort(arg0);
	}
	@Override
	public int getInt(int arg0) throws SQLException {
		return cachedrowset.getInt(arg0);
	}
	@Override
	public int getInt(java.lang.String arg0) throws SQLException {
		return cachedrowset.getInt(arg0);
	}
	@Override
	public long getLong(int arg0) throws SQLException {
		return cachedrowset.getLong(arg0);
	}
	@Override
	public long getLong(java.lang.String arg0) throws SQLException {
		return cachedrowset.getLong(arg0);
	}
	@Override
	public float getFloat(java.lang.String arg0) throws SQLException {
		return cachedrowset.getFloat(arg0);
	}
	@Override
	public float getFloat(int arg0) throws SQLException {
		return cachedrowset.getFloat(arg0);
	}
	@Override
	public double getDouble(java.lang.String arg0) throws SQLException {
		return cachedrowset.getDouble(arg0);
	}
	@Override
	public double getDouble(int arg0) throws SQLException {
		return cachedrowset.getDouble(arg0);
	}
	@Override
	public byte[] getBytes(int arg0) throws SQLException {
		return cachedrowset.getBytes(arg0);
	}
	@Override
	public byte[] getBytes(java.lang.String arg0) throws SQLException {
		return cachedrowset.getBytes(arg0);
	}
	@Override
	public java.sql.Array getArray(java.lang.String arg0) throws SQLException {
		return cachedrowset.getArray(arg0);
	}
	@Override
	public java.sql.Array getArray(int arg0) throws SQLException {
		return cachedrowset.getArray(arg0);
	}
	@Override
	public java.net.URL getURL(int arg0) throws SQLException {
		return cachedrowset.getURL(arg0);
	}
	@Override
	public java.net.URL getURL(java.lang.String arg0) throws SQLException {
		return cachedrowset.getURL(arg0);
	}
	@Override
	public java.lang.String getString(int arg0) throws SQLException {
		Object o = getObject(arg0);
		String s = null;
		if(null != o){
			if(o instanceof java.sql.Timestamp || o instanceof java.sql.Time || o instanceof java.sql.Date){
				s = o.toString();
			}else{
				s = cachedrowset.getString(arg0);
			}
		}
		return s; 
	}
	@Override
	public java.lang.String getString(java.lang.String arg0) throws SQLException {
		Object o = getObject(arg0);
		String s = null;
		if(null != o){
			if(o instanceof java.sql.Timestamp || o instanceof java.sql.Time || o instanceof java.sql.Date){
				s = o.toString();
			}else{
				s = cachedrowset.getString(arg0);
			}
		}
		return s; 
	}
	@Override
	public java.sql.Ref getRef(java.lang.String arg0) throws SQLException {
		return cachedrowset.getRef(arg0);
	}
	@Override
	public java.sql.Ref getRef(int arg0) throws SQLException {
		return cachedrowset.getRef(arg0);
	}
	@Override
	public boolean isWrapperFor(java.lang.Class arg0) throws SQLException {
		return cachedrowset.isWrapperFor(arg0);
	}
	@Override
	public java.lang.Object unwrap(java.lang.Class arg0) throws SQLException {
		return cachedrowset.unwrap(arg0);
	}

    /**********************************************************************************/
	/**
	 * Below update methods are commented out due to this being a read only resultset 
	 */
	@Override
	public void deleteRow() throws SQLException {
		//cachedrowset.deleteRow();
	}
	@Override
	public void cancelRowUpdates() throws SQLException {
		//cachedrowset.cancelRowUpdates();
	}
	@Override
	public void moveToInsertRow() throws SQLException {
		//cachedrowset.moveToInsertRow();
	}
	@Override
	public void insertRow() throws SQLException {
		//cachedrowset.insertRow();
	}
	@Override
	public void refreshRow() throws SQLException {
		//cachedrowset.refreshRow();
	}
	@Override
	public boolean relative(int arg0) throws SQLException {
		return false; //cachedrowset.relative(arg0);
	}
	@Override
	public boolean rowDeleted() throws SQLException {
		return false; //cachedrowset.rowDeleted();
	}
	@Override
	public boolean rowInserted() throws SQLException {
		return false; //cachedrowset.rowInserted();
	}
	@Override
	public boolean rowUpdated() throws SQLException {
		return false; //cachedrowset.rowUpdated();
	}
	@Override
	public void updateArray(int arg0, java.sql.Array arg1) throws SQLException {
		//cachedrowset.updateArray(arg0, arg1);
	}
	@Override
	public void updateArray(java.lang.String arg0, java.sql.Array arg1) throws SQLException {
		//cachedrowset.updateArray(arg0, arg1);
	}
	@Override
	public void updateAsciiStream(java.lang.String arg0, java.io.InputStream arg1) throws SQLException {
		//cachedrowset.updateAsciiStream(arg0, arg1);
	}
	@Override
	public void updateAsciiStream(java.lang.String arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		//cachedrowset.updateAsciiStream(arg0, arg1, arg2);
	}
	@Override
	public void updateAsciiStream(int arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		//cachedrowset.updateAsciiStream(arg0, arg1, arg2);
	}
	@Override
	public void updateAsciiStream(int arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		//cachedrowset.updateAsciiStream(arg0, arg1, arg2);
	}
	@Override
	public void updateAsciiStream(int arg0, java.io.InputStream arg1) throws SQLException {
		//cachedrowset.updateAsciiStream(arg0, arg1);
	}
	@Override
	public void updateAsciiStream(java.lang.String arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		//cachedrowset.updateAsciiStream(arg0, arg1, arg2);
	}
	@Override
	public void updateBigDecimal(java.lang.String arg0, java.math.BigDecimal arg1) throws SQLException {
		//cachedrowset.updateBigDecimal(arg0, arg1);
	}
	@Override
	public void updateBigDecimal(int arg0, java.math.BigDecimal arg1) throws SQLException {
		//cachedrowset.updateBigDecimal(arg0, arg1);
	}
	@Override
	public void updateBinaryStream(int arg0, java.io.InputStream arg1) throws SQLException {
		//cachedrowset.updateBinaryStream(arg0, arg1);
	}
	@Override
	public void updateBinaryStream(int arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		//cachedrowset.updateBinaryStream(arg0, arg1, arg2);
	}
	@Override
	public void updateBinaryStream(java.lang.String arg0, java.io.InputStream arg1) throws SQLException {
		//cachedrowset.updateBinaryStream(arg0, arg1);
	}
	@Override
	public void updateBinaryStream(java.lang.String arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		//cachedrowset.updateBinaryStream(arg0, arg1, arg2);
	}
	@Override
	public void updateBinaryStream(java.lang.String arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		//cachedrowset.updateBinaryStream(arg0, arg1, arg2);
	}
	@Override
	public void updateBinaryStream(int arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		//cachedrowset.updateBinaryStream(arg0, arg1, arg2);
	}
	@Override
	public void updateBlob(int arg0, java.sql.Blob arg1) throws SQLException {
		//cachedrowset.updateBlob(arg0, arg1);
	}
	@Override
	public void updateBlob(java.lang.String arg0, java.sql.Blob arg1) throws SQLException {
		//cachedrowset.updateBlob(arg0, arg1);
	}
	@Override
	public void updateBlob(int arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		//cachedrowset.updateBlob(arg0, arg1, arg2);
	}
	@Override
	public void updateBlob(java.lang.String arg0, java.io.InputStream arg1) throws SQLException {
		//cachedrowset.updateBlob(arg0, arg1);
	}
	@Override
	public void updateBlob(int arg0, java.io.InputStream arg1) throws SQLException {
		//cachedrowset.updateBlob(arg0, arg1);
	}
	@Override
	public void updateBlob(java.lang.String arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		//cachedrowset.updateBlob(arg0, arg1, arg2);
	}
	@Override
	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		//cachedrowset.updateBoolean(arg0, arg1);
	}
	@Override
	public void updateBoolean(java.lang.String arg0, boolean arg1) throws SQLException {
		//cachedrowset.updateBoolean(arg0, arg1);
	}
	@Override
	public void updateByte(int arg0, byte arg1) throws SQLException {
		//cachedrowset.updateByte(arg0, arg1);
	}
	@Override
	public void updateByte(java.lang.String arg0, byte arg1) throws SQLException {
		//cachedrowset.updateByte(arg0, arg1);
	}
	@Override
	public void updateBytes(java.lang.String arg0, byte[] arg1) throws SQLException {
		//cachedrowset.updateBytes(arg0, arg1);
	}
	@Override
	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		//cachedrowset.updateBytes(arg0, arg1);
	}
	@Override
	public void updateCharacterStream(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateCharacterStream(arg0, arg1, arg2);
	}
	@Override
	public void updateCharacterStream(java.lang.String arg0, java.io.Reader arg1, int arg2) throws SQLException {
		//cachedrowset.updateCharacterStream(arg0, arg1, arg2);
	}
	@Override
	public void updateCharacterStream(int arg0, java.io.Reader arg1, int arg2) throws SQLException {
		//cachedrowset.updateCharacterStream(arg0, arg1, arg2);
	}
	@Override
	public void updateCharacterStream(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateCharacterStream(arg0, arg1, arg2);
	}
	@Override
	public void updateCharacterStream(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateCharacterStream(arg0, arg1);
	}
	@Override
	public void updateCharacterStream(int arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateCharacterStream(arg0, arg1);
	}
	@Override
	public void updateClob(int arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateClob(arg0, arg1);
	}
	@Override
	public void updateClob(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateClob(arg0, arg1);
	}
	@Override
	public void updateClob(int arg0, java.sql.Clob arg1) throws SQLException {
		//cachedrowset.updateClob(arg0, arg1);
	}
	@Override
	public void updateClob(java.lang.String arg0, java.sql.Clob arg1) throws SQLException {
		//cachedrowset.updateClob(arg0, arg1);
	}
	@Override
	public void updateClob(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateClob(arg0, arg1, arg2);
	}
	@Override
	public void updateClob(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateClob(arg0, arg1, arg2);
	}
	@Override
	public void updateDate(int arg0, java.sql.Date arg1) throws SQLException {
		//cachedrowset.updateDate(arg0, arg1);
	}
	@Override
	public void updateDate(java.lang.String arg0, java.sql.Date arg1) throws SQLException {
		//cachedrowset.updateDate(arg0, arg1);
	}
	@Override
	public void updateDouble(int arg0, double arg1) throws SQLException {
		//cachedrowset.updateDouble(arg0, arg1);
	}
	@Override
	public void updateDouble(java.lang.String arg0, double arg1) throws SQLException {
		//cachedrowset.updateDouble(arg0, arg1);
	}
	@Override
	public void updateFloat(int arg0, float arg1) throws SQLException {
		//cachedrowset.updateFloat(arg0, arg1);
	}
	@Override
	public void updateFloat(java.lang.String arg0, float arg1) throws SQLException {
		//cachedrowset.updateFloat(arg0, arg1);
	}
	@Override
	public void updateInt(int arg0, int arg1) throws SQLException {
		//cachedrowset.updateInt(arg0, arg1);
	}
	@Override
	public void updateInt(java.lang.String arg0, int arg1) throws SQLException {
		//cachedrowset.updateInt(arg0, arg1);
	}
	@Override
	public void updateLong(java.lang.String arg0, long arg1) throws SQLException {
		//cachedrowset.updateLong(arg0, arg1);
	}
	@Override
	public void updateLong(int arg0, long arg1) throws SQLException {
		//cachedrowset.updateLong(arg0, arg1);
	}
	@Override
	public void updateNCharacterStream(int arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateNCharacterStream(arg0, arg1);
	}
	@Override
	public void updateNCharacterStream(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateNCharacterStream(arg0, arg1);
	}
	@Override
	public void updateNCharacterStream(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateNCharacterStream(arg0, arg1, arg2);
	}
	@Override
	public void updateNCharacterStream(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateNCharacterStream(arg0, arg1, arg2);
	}
	@Override
	public void updateNClob(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateNClob(arg0, arg1);
	}
	@Override
	public void updateNClob(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateNClob(arg0, arg1, arg2);
	}
	@Override
	public void updateNClob(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		//cachedrowset.updateNClob(arg0, arg1, arg2);
	}
	@Override
	public void updateNClob(int arg0, java.io.Reader arg1) throws SQLException {
		//cachedrowset.updateNClob(arg0, arg1);
	}
	@Override
	public void updateNClob(int arg0, java.sql.NClob arg1) throws SQLException {
		//cachedrowset.updateNClob(arg0, arg1);
	}
	@Override
	public void updateNClob(java.lang.String arg0, java.sql.NClob arg1) throws SQLException {
		//cachedrowset.updateNClob(arg0, arg1);
	}
	@Override
	public void updateNString(int arg0, java.lang.String arg1) throws SQLException {
		//cachedrowset.updateNString(arg0, arg1);
	}
	@Override
	public void updateNString(java.lang.String arg0, java.lang.String arg1) throws SQLException {
		//cachedrowset.updateNString(arg0, arg1);
	}
	@Override
	public void updateNull(java.lang.String arg0) throws SQLException {
		//cachedrowset.updateNull(arg0);
	}
	@Override
	public void updateNull(int arg0) throws SQLException {
		//cachedrowset.updateNull(arg0);
	}
	@Override
	public void updateObject(int arg0, java.lang.Object arg1, int arg2) throws SQLException {
		//cachedrowset.updateObject(arg0, arg1, arg2);
	}
	@Override
	public void updateObject(int arg0, java.lang.Object arg1) throws SQLException {
		//cachedrowset.updateObject(arg0, arg1);
	}
	@Override
	public void updateObject(java.lang.String arg0, java.lang.Object arg1, int arg2) throws SQLException {
		//cachedrowset.updateObject(arg0, arg1, arg2);
	}
	@Override
	public void updateObject(java.lang.String arg0, java.lang.Object arg1) throws SQLException {
		//cachedrowset.updateObject(arg0, arg1);
	}
	@Override
	public void updateRef(java.lang.String arg0, java.sql.Ref arg1) throws SQLException {
		//cachedrowset.updateRef(arg0, arg1);
	}
	@Override
	public void updateRef(int arg0, java.sql.Ref arg1) throws SQLException {
		//cachedrowset.updateRef(arg0, arg1);
	}
	@Override
	public void updateRow() throws SQLException {
		//cachedrowset.updateRow();
	}
	@Override
	public void updateRowId(java.lang.String arg0, java.sql.RowId arg1) throws SQLException {
		//cachedrowset.updateRowId(arg0, arg1);
	}
	@Override
	public void updateRowId(int arg0, java.sql.RowId arg1) throws SQLException {
		//cachedrowset.updateRowId(arg0, arg1);
	}
	@Override
	public void updateSQLXML(int arg0, java.sql.SQLXML arg1) throws SQLException {
		//cachedrowset.updateSQLXML(arg0, arg1);
	}
	@Override
	public void updateSQLXML(java.lang.String arg0, java.sql.SQLXML arg1) throws SQLException {
		//cachedrowset.updateSQLXML(arg0, arg1);
	}
	@Override
	public void updateShort(int arg0, short arg1) throws SQLException {
		//cachedrowset.updateShort(arg0, arg1);
	}
	@Override
	public void updateShort(java.lang.String arg0, short arg1) throws SQLException {
		//cachedrowset.updateShort(arg0, arg1);
	}
	@Override
	public void updateString(int arg0, java.lang.String arg1) throws SQLException {
		//cachedrowset.updateString(arg0, arg1);
	}
	@Override
	public void updateString(java.lang.String arg0, java.lang.String arg1) throws SQLException {
		//cachedrowset.updateString(arg0, arg1);
	}
	@Override
	public void updateTime(java.lang.String arg0, java.sql.Time arg1) throws SQLException {
		//cachedrowset.updateTime(arg0, arg1);
	}
	@Override
	public void updateTime(int arg0, java.sql.Time arg1) throws SQLException {
		//cachedrowset.updateTime(arg0, arg1);
	}
	@Override
	public void updateTimestamp(int arg0, java.sql.Timestamp arg1) throws SQLException {
		//cachedrowset.updateTimestamp(arg0, arg1);
	}
	@Override
	public void updateTimestamp(java.lang.String arg0, java.sql.Timestamp arg1) throws SQLException {
		//cachedrowset.updateTimestamp(arg0, arg1);
	}
	/**********************************************************************************/
	
}

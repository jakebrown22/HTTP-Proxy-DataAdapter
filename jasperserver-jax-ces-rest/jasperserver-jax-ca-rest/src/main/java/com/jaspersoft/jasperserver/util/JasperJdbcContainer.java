/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.util;

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperserver.api.security.validators.Validator;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.web.shared.CachedRowSetWrapper;

/**
 * This class given a connection object as input creates a preparedStatement 
 * statement against a database and holds the Resultset till the close request comes in.
 *
 */

public class JasperJdbcContainer {

	private Connection connection = null;
	private PreparedStatement preparedstatement = null;
	private ResultSet resultset = null;
	protected static final Log logger = LogFactory.getLog(JasperJdbcContainer.class);
	

	//**************************************************************
	//public methods
    
    public JasperJdbcContainer(){
    	//JasperJDBCServer.setLoggerLevel(logger);
    }
	
	/**
	 *  Returns the resultset object created from executing a preparedStatement
	 * @return
	 */
	public ResultSet getResultset() {
		return resultset;
	}
	
	public void close() {
		try {
			if (this.resultset != null)
				this.resultset.close();
			
			if (this.preparedstatement != null)
				this.preparedstatement.close();
			
			if (this.connection != null)
				this.connection.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);		
		}
	}// close()

	/**
	 * The main execute method which takes a connection and CachedRowSetWrapper object 
	 * extracts the command from the CachedRowSet, populates it with parameters and executes the
	 * PreparedStatement. 
	 * @param conn jdbc Connection
	 * @param crw  CachedRowSetWrapper
	 * @throws SQLException
	 */
	public void execute(Connection conn, CachedRowSetWrapper crw)
			throws SQLException {
    	long startTime = System.currentTimeMillis();		
		this.connection = conn;
    	try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter execute .. Start Time" + System.currentTimeMillis() );	
        	}    			
        	if (logger.isDebugEnabled()){
        		logger.debug("Sql query: " + crw.getCachedRowSet().getCommand());	
        		logger.debug("Sql properties: Max Rows: " + crw.getCachedRowSet().getMaxRows());
        		logger.debug("Sql properties: Max Fetch Size: " + crw.getCachedRowSet().getFetchSize());
        		logger.debug("Sql properties: Max Fields Size: " + crw.getCachedRowSet().getMaxFieldSize());
        		logger.debug("Sql properties: Page Size: " + crw.getCachedRowSet().getPageSize());
        	}    			
        	
    		// Validate the SQL
			boolean isSQLValid = Validator.validateSQL(crw.getCachedRowSet().getCommand());
			if(!isSQLValid)
				throw new SQLException("Invalid SQL:" + crw.getCachedRowSet().getCommand());
			
        	if (logger.isDebugEnabled()){
        		logger.debug("Sql validated successfully.");	
        	}    			
        	
			this.preparedstatement = conn.prepareStatement(crw.getCachedRowSet().getCommand());

        	
			//set the properties on the prepstmt
			this.preparedstatement.setMaxRows(crw.getCachedRowSet().getMaxRows());
			this.preparedstatement.setFetchSize(crw.getCachedRowSet().getFetchSize());
			this.preparedstatement.setMaxFieldSize(crw.getCachedRowSet().getMaxFieldSize());
			
			//set the parameters if present
			Object[] parameters = getParameters(crw.getCachedRowSet());
			if(null != parameters){
				insertParameters(parameters, this.preparedstatement);
			}
        	if (logger.isDebugEnabled()){
        		logger.debug("Parameters:" + parameters.toString());
        	}    			
			
        	long newStartTime = System.currentTimeMillis();
			this.resultset = this.preparedstatement.executeQuery();
        	if (logger.isDebugEnabled()){
        		long newElapsedTime = System.currentTimeMillis() - newStartTime;
        		logger.debug("Total time to execute query: " + newElapsedTime);
        	}    						
    	}catch (Exception ex){
    		logger.error(ex.getMessage(), ex);
    		throw new SQLException(ex.getMessage());
    	}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit execute .. Total Time Spent: " + elapsedTime);	
        	}
    	}
	

	}
	/**
	 *  This method is a hack, using reflection invokes a private method on the CachedRowSet object to 
	 *  extract the parameter payload. 
	 * @param crs CachedRowSet 
	 * @return
	 */
	private Object[] getParameters(CachedRowSet crs){
		Object[] params = null;
		try {
			Method method = crs.getClass().getMethod("getParams", (Class[])null);
			method.setAccessible(true);
			params = (Object[]) method.invoke(crs, (Object[])null);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
			throw new RemoteException("Can't get method name." + e.getMessage());
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
			throw new RemoteException("Can't get method name." + e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new RemoteException("Can't get method name." + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new RemoteException("Can't get method name." + e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
			throw new RemoteException("Can't get method name." + e.getMessage());
		}
		
		return params;

	}
	/**
	 * Given a array of jdbc parameters and a PreparedStatment loops through the parameter array
	 * and populates the PreparedStatement
	 * @param params jdbc parameters extracted from a CachedRowSet 
	 * @param pstmt  the PreparedStatment to populate
	 * @throws SQLException
	 */
	private void insertParameters(Object[] params, PreparedStatement pstmt)
			throws SQLException {
		
		Object[] param = null;

		for (int i = 0; i < params.length; i++) {
			
			if (params[i] instanceof Object[]) {
				param = (Object[]) params[i];
				if (param.length == 2) {
					if (param[0] == null) {
						pstmt.setNull(i + 1, ((Integer) param[1]).intValue());
						continue;
					}

					if (param[0] instanceof java.sql.Date
							|| param[0] instanceof java.sql.Time
							|| param[0] instanceof java.sql.Timestamp) {
						if (param[1] instanceof java.util.Calendar) {
							pstmt.setDate(i + 1, (java.sql.Date) param[0],
									(java.util.Calendar) param[1]);
							continue;
						} else {
							 throw new
							 SQLException("Unknown Parameter, expected java.util.Calendar");
						}
					}

					if (param[0] instanceof Reader) {
						pstmt.setCharacterStream(i + 1, (Reader) param[0],
								((Integer) param[1]).intValue());
						continue;
					}

					/*
					 * What's left should be setObject(int, Object, scale)
					 */
					if (param[1] instanceof Integer) {
						pstmt.setObject(i + 1, param[0],
								((Integer) param[1]).intValue());
						continue;
					}

				} else if (param.length == 3) {

					if (param[0] == null) {
						pstmt.setNull(i + 1, ((Integer) param[1]).intValue(),
								(String) param[2]);
						continue;
					}

					// need to find and fix inputstreams
					if (param[0] instanceof java.io.InputStream) {
						//logger.fine("Found parameter of type input stream");
					 }//inputstream if

					/*
					 * no point at looking at the first element now; what's left
					 * must be the setObject() cases.
					 */
					if (param[1] instanceof Integer
							&& param[2] instanceof Integer) {
						pstmt.setObject(i + 1, param[0],
								((Integer) param[1]).intValue(),
								((Integer) param[2]).intValue());
						continue;
					}
					throw new SQLException("Unexpected Parameter");

				} else {
					// common case - this catches all SQL92 types
					pstmt.setObject(i + 1, params[i]);
					continue;
				}
			} else {
				// Try to get all the params to be set here
				pstmt.setObject(i + 1, params[i]);
				//logger.finest("Param" + i+ ": " + params[i]);
			}
		}//for
		//logger.exiting(getClass().getName(), "insertParameters");
	}//insertParameters

}// end of class

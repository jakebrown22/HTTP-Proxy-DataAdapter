/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.BeanReportDataSource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.JdbcReportDataSource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.JndiJdbcReportDataSource;
import com.jaspersoft.jasperserver.remote.dbservices.ConnectionService;
import com.jaspersoft.jasperserver.remote.dbservices.impl.BeanConnectionServiceImpl;
import com.jaspersoft.jasperserver.remote.dbservices.impl.JdbcConnectionServiceImpl;
import com.jaspersoft.jasperserver.remote.dbservices.impl.JndiConnectionServiceImpl;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;


public class QueryUtil {
	protected static final Log logger = LogFactory.getLog(QueryUtil.class);
	private static final String cookieName =  "REQUEST_ORIGINATOR";
	private static final String cookieValue =  "js_custom_adapter";
	
	
	public static Connection getConnection(Resource resource) {
    	long startTime = System.currentTimeMillis();		
		Connection conn = null;
		ConnectionService connectionService = null;
		try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter getConnection .. Start Time" + System.currentTimeMillis() );	
        	}			
			if (resource instanceof  JdbcReportDataSource){
				connectionService = new JdbcConnectionServiceImpl();
			}else if (resource instanceof  JndiJdbcReportDataSource){
				connectionService = new JndiConnectionServiceImpl();
			}else if (resource instanceof  BeanReportDataSource){
				connectionService = new BeanConnectionServiceImpl();
			}
			
			if (connectionService != null)
				conn = connectionService.getConnection(resource);
			else 
				throw new RemoteException("Invalid Resource: Please check datasource url");		
			
		} catch (Exception ex) {
    		logger.error(ex.getMessage(), ex);
			throw new RemoteException("Connection Error." + ex.getMessage());
		} finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit getConnection .. Total Time Spent: " + elapsedTime);	
        	}			
		}
	
		return conn;
	}
	

	/**
	 * Execute DataBaseMetaData methods using reflection
	 * @param dmd
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Method findMethod(DatabaseMetaData dmd, String methodName, Object[] parameters) throws ClassNotFoundException{
    	long startTime = System.currentTimeMillis();		
		try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter findMethod .. Start Time" + System.currentTimeMillis() );	
        	}						
			 Class cl = Class.forName("java.sql.DatabaseMetaData");
			 Method[] methods = cl.getDeclaredMethods();
			 // Trying to avoid collision of methods with varying parameters and avoid having to do parameter class types
			 
			 int paramCount = 0;
			 if(null != parameters ){
				 paramCount = parameters.length;
			 }
			 
			 for(Method m : methods){
				 if(m.getName().equals(methodName)){
					 if(Modifier.isPublic(m.getModifiers()) && m.getParameterTypes().length == paramCount){
						 return m;
					 }	 
				 }
			 }//for
			 return null;			
		}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit findMethod .. Total Time Spent: " + elapsedTime);	
        	}									
		}

	}	
	
	/**
	 * Validate request
	 * @param headers
	 * @return
	 * @throws RemoteException
	 */
	public static void validateRequest(final HttpHeaders headers, final String requestOrgCheck){
    	long startTime = System.currentTimeMillis();		
		try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter validateRequest .. Start Time" + System.currentTimeMillis() );	
        		logger.debug("requestOrgCheck" + requestOrgCheck);
        	}			
			if (requestOrgCheck != null && requestOrgCheck.equalsIgnoreCase("Y")){
	        	if (logger.isDebugEnabled()){
	        		logger.debug("cookieValue" + headers.getCookies().get(cookieName).getValue());
	        	}							
				if (!cookieValue.equals(headers.getCookies().get(cookieName).getValue()))
					throw new RemoteException("Invalid Request: Please check request url");
				}			
		}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit validateRequest .. Total Time Spent: " + elapsedTime);	
        	}						
		}

	}		
	
}

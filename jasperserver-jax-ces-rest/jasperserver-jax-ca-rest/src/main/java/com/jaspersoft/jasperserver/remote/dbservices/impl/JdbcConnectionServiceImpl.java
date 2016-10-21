/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.jasperserver.remote.dbservices.impl;

import java.sql.Connection;

import java.sql.DriverManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.JdbcReportDataSource;
import com.jaspersoft.jasperserver.remote.dbservices.ConnectionService;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.jasperserver.remote.exception.ResourceNotFoundException;
import com.jaspersoft.ji.license.LicenseManager;


public class JdbcConnectionServiceImpl implements ConnectionService {
    protected static final Log logger = LogFactory.getLog(JdbcConnectionServiceImpl.class);
   
    /**
     *
     * @param resource
     * @return Connection object, guaranteed to be non-null (not found or not supported resource indicated by exception)
     * @throws ResourceNotFoundException if no resource found
     * @throws RemoteException in case of unclassified error
     */
    public Connection getConnection(Resource resource)  {
		Connection result = null;
		long startTime = System.currentTimeMillis();		
		try {
			if (logger.isDebugEnabled()){
        		logger.debug("Enter getConnection .. Start Time" + System.currentTimeMillis() );	
        	} 						
            if (resource instanceof  JdbcReportDataSource){
            	JdbcReportDataSource jdbcDataSource = (JdbcReportDataSource)  resource;
            	Class.forName(jdbcDataSource.getDriverClass());
            	result = DriverManager.getConnection(jdbcDataSource.getConnectionUrl(), jdbcDataSource.getUsername(), jdbcDataSource.getPassword());
            }else{
				throw new RemoteException("Invalid Resource: Please check datasource url");            	
            }
		    }catch(Exception ex){
	    		logger.error(ex.getMessage(), ex);
				throw new RemoteException("Cannot get jdbc connection:" + ex.getMessage());
		    }finally{
	        	if (logger.isDebugEnabled()){
	        		long elapsedTime = System.currentTimeMillis() - startTime;
	        		logger.debug("Exit getConnection .. Total Time Spent: " + elapsedTime);	
	        	}
	    	}
		    return result;

    }
    

    
}


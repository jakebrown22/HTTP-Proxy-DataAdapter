/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.jasperserver.remote.dbservices.impl;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.JndiJdbcReportDataSource;
import com.jaspersoft.jasperserver.remote.dbservices.ConnectionService;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.jasperserver.remote.exception.ResourceNotFoundException;


public class JndiConnectionServiceImpl implements ConnectionService {
    protected static final Log logger = LogFactory.getLog(MetaDataServiceImpl.class);

    /**
     *
     * @param resource
     * @return Connection object, guaranteed to be non-null (not found or not supported resource indicated by exception)
     * @throws ResourceNotFoundException if no resource found 
     * @throws RemoteException in case of unclassified error
     */
    @SuppressWarnings("unused")
	public Connection getConnection(Resource resource)  {
		Connection result = null;
		String DATASOURCE_CONTEXT = null;
		long startTime = System.currentTimeMillis();
		try {
			if (logger.isDebugEnabled()){
        		logger.debug("Enter getConnection .. Start Time" + System.currentTimeMillis() );	
        	} 									
            if (resource instanceof  JndiJdbcReportDataSource){
            	JndiJdbcReportDataSource jndiDataSource = (JndiJdbcReportDataSource)  resource;
            	DATASOURCE_CONTEXT = "java:comp/env/" + jndiDataSource.getJndiName();
            	Context initialContext = new InitialContext();
            	if (initialContext != null){
            		DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
                    if (datasource != null) {
                      result = datasource.getConnection();
                    }else{
                    	throw new RemoteException("Cannot get database connection: Please check datasource url");
                    }
            	} else {
					throw new RemoteException("Cannot get jndi context: Please check datasource url");
				}

            }else{
				throw new RemoteException("Invalid Resource: Please check datasource url");            	
            }
		    }catch(Exception ex){
	    		logger.error(ex.getMessage(), ex);
				throw new RemoteException("Cannot get jndi connection:" + ex.getMessage());
		    }finally{
	        	if (logger.isDebugEnabled()){
	        		long elapsedTime = System.currentTimeMillis() - startTime;
	        		logger.debug("Exit getConnection .. Total Time Spent: " + elapsedTime);	
	        	}
	    	}
		    return result;

    }
    

    
}


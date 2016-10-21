/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.

 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.jasperserver.remote.dbservices.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.jaspersoft.jasperserver.api.common.util.spring.StaticApplicationContext;
import com.jaspersoft.jasperserver.api.engine.jasperreports.service.impl.JdbcDataSourceService;
import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.BeanReportDataSource;
import com.jaspersoft.jasperserver.remote.dbservices.ConnectionService;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.jasperserver.remote.exception.ResourceNotFoundException;


public class BeanConnectionServiceImpl implements ConnectionService {
    protected static final Log logger = LogFactory.getLog(BeanConnectionServiceImpl.class);
    protected ApplicationContext applicationContext;
    
   /**
     *
     * @param resource
     * @return Connection object, guaranteed to be non-null (not found or not supported resource indicated by exception)
     * @throws ResourceNotFoundException if no resource found 
     * @throws RemoteException in case of unclassified error
     */
    public Connection getConnection(Resource resource) {
		Connection result = null;
		long startTime = System.currentTimeMillis();
		try {
			if (logger.isDebugEnabled()){
        		logger.debug("Enter getConnection .. Start Time" + System.currentTimeMillis() );	
        	} 			
            if (resource instanceof  BeanReportDataSource){
            	BeanReportDataSource beanDataSource = (BeanReportDataSource)  resource;
            	applicationContext= StaticApplicationContext.getApplicationContext();
            	Object bean = this.applicationContext.getBean(beanDataSource.getBeanName());
            	Method serviceMethod = bean.getClass().getMethod(beanDataSource.getBeanMethod(), null);
    			JdbcDataSourceService service = (JdbcDataSourceService) serviceMethod.invoke(bean, null);
            	result = service.getDataSource().getConnection();
            }else{
				throw new RemoteException("Invalid Resource: Please check datasource url");            	
            }
		    }catch (InvocationTargetException ex){
	    		logger.error(ex.getMessage(), ex);
				throw new RemoteException("Cannot get bean datasource connection:" + ex.toString());		    	
		    }
			catch(Exception ex){
	    		logger.error(ex.getMessage(), ex);
				throw new RemoteException("Cannot get bean datasource connection:" + ex.getMessage());
		    }finally{
	        	if (logger.isDebugEnabled()){
	        		long elapsedTime = System.currentTimeMillis() - startTime;
	        		logger.debug("Exit getConnection .. Total Time Spent: " + elapsedTime);	
	        	}
	    	}
		    return result;

    }
    
}


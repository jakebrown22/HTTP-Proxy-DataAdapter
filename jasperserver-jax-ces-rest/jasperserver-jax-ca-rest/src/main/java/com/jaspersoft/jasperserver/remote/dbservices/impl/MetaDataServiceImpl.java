/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.jasperserver.remote.dbservices.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.teiid.core.util.StringUtil;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.remote.dbservices.MetaDataService;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.jasperserver.util.JasperSerializationUtil;
import com.jaspersoft.jasperserver.util.QueryUtil;
import com.jaspersoft.ji.license.LicenseCheckStatus;
import com.jaspersoft.ji.license.LicenseException;
import com.jaspersoft.ji.license.LicenseManager;
import com.jaspersoft.web.shared.CachedRowSetWrapper;

@Component("jdbcMetaDataService")
public class MetaDataServiceImpl implements MetaDataService {
    protected static final Log logger = LogFactory.getLog(MetaDataServiceImpl.class);

    @javax.annotation.Resource(name="licenseManager")
    private LicenseManager licenseManager;
    
    
	/**
	 * This method invokes a method ( a total of around 170 odd ) on the DatabaseMetaData object based on 
	 * method name and parameters. If the result is a Resultset a CachedRowSet object is populated with 
	 * its results and returned. Else all other types are returned as is.
	 * @param request
	 * @return
	 */
    
    public byte[] getDBMetaData(Resource resource, CachedRowSetWrapper crw) {
		long startTime = System.currentTimeMillis();    	
    	byte[] ret = new byte[0];
    	Connection conn = null;
    	Method method = null;   
    	CachedRowSet crs = null;
    	Object result = null;
    	try{
			if (logger.isDebugEnabled()){
        		logger.debug("Enter getDBMetaData .. Start Time" + System.currentTimeMillis() );	
        	} 							
    		if(crw.getParameters() != null){
    			for(int i = 0; i< crw.getParameters().length; i++){
    				Object param = crw.getParameters()[i];
    				
    				//if(param instanceof String && ((String) param).length() == 0){
    				if(param instanceof String && StringUtil.isEmpty((String) param)){
    					crw.getParameters()[i] = null;  // make it null
    				}
    			}
    		}
    		conn = QueryUtil.getConnection(resource);
			DatabaseMetaData dm = conn.getMetaData();    		
    		method = QueryUtil.findMethod(dm, crw.getRequestId(), crw.getParameters());
    		if(null != method){
				result = method.invoke(dm, crw.getParameters());
				if(null != result){
					if(result instanceof java.sql.ResultSet){ // got a resultset
						crs = RowSetProvider.newFactory().createCachedRowSet();
						crs.populate((ResultSet) result);
						((java.sql.ResultSet) result).close(); // close the resultset
						result = crs;
					}
					if (result instanceof Serializable){
						ret = JasperSerializationUtil.serialize((Serializable) result);
					}else{
						logger.warn("Cannot serialize object" + result.getClass().getName());
					}
				}// if
			}else{
				throw new RemoteException(crw.getRequestId() +  " method name is not supported.");
			}
    	} catch (Exception ex) {
    		logger.error(ex.getMessage(), ex);
			throw new RemoteException("Meta Data fail." + ex.getMessage());
		} 
    	finally{
    		try{
   	   			if (conn != null)
   	   				conn.close();   
   	   			if(crs != null)
   	   				crs.close();	
    		}catch (Exception ex){
    			logger.error(ex.getMessage(), ex);
    			throw new RemoteException("Meta Data fail." + ex.getMessage());
    		}
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit getDBMetaData .. Total Time Spent: " + elapsedTime);	
        	}    		
    	}
    	return ret;
   	}        
	
}



/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

package com.jaspersoft.jasperserver.remote.dbservices.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.remote.dbservices.ExecuteQueryService;
import com.jaspersoft.jasperserver.remote.dbservices.MetaDataService;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.jasperserver.remote.exception.ResourceNotFoundException;
import com.jaspersoft.jasperserver.remote.services.SingleRepositoryService;
import com.jaspersoft.jasperserver.util.JasperJdbcContainer;
import com.jaspersoft.jasperserver.util.QueryUtil;
import com.jaspersoft.jasperserver.util.ResourceCache;
import com.jaspersoft.ji.license.LicenseCheckStatus;
import com.jaspersoft.ji.license.LicenseException;
import com.jaspersoft.ji.license.LicenseManager;
import com.jaspersoft.web.shared.CachedRowSetWrapper;

@Component("executeQueryService")
public class ExecuteQueryServiceImpl implements ExecuteQueryService {
    protected static final Log logger = LogFactory.getLog(ExecuteQueryServiceImpl.class);

    @javax.annotation.Resource(name = "messageSource")
    private MessageSource messages;
    
    @javax.annotation.Resource
    private SingleRepositoryService singleRepositoryService;    
    
    @javax.annotation.Resource
    private MetaDataService jdbcMetaDataService;
    
    @javax.annotation.Resource
    private ResourceCache jdbcCache;
    
    private Integer fetchSize = null;
    
    private Integer maxRows = null;
    
    private Integer pageSize = null;
    
    /**
     *
     * @param resource
     * @return Connection object, guaranteed to be non-null (not found or not supported resource indicated by exception)
     * @throws ResourceNotFoundException if no resource found
     * @throws RemoteException in case of unclassified error
     */
    public CachedRowSetWrapper executeQuery(Resource resource, CachedRowSetWrapper crw, String tenantId)  {
		long startTime = System.currentTimeMillis();

		JasperJdbcContainer jdbcHolder = this.jdbcCache.get(tenantId + ":" + crw.getRequestId());
		try{
			if (logger.isDebugEnabled()){
        		logger.debug("Enter executeQuery .. Start Time " + System.currentTimeMillis());	
        	}			
			// Check Fetch Size property and if client value exceed server side property then server side property will take precedence
			if (crw.getCachedRowSet() != null && (crw.getCachedRowSet().getFetchSize() == 0 ||  crw.getCachedRowSet().getFetchSize() >= this.getFetchSize().intValue())){
				crw.getCachedRowSet().setFetchSize(this.getFetchSize().intValue());
			}
			
			// Check Max Row property and if client value exceed server side property then server side property will take precedence 
			if (crw.getCachedRowSet() != null && (crw.getCachedRowSet().getMaxRows() == 0 || crw.getCachedRowSet().getMaxRows() >= this.getMaxRows().intValue())){
				crw.getCachedRowSet().setMaxRows(this.getMaxRows().intValue());
			}
			
			// Check Page Size property and if client value exceed server side property then server side property will take precedence 
			if (crw.getCachedRowSet() != null && (crw.getCachedRowSet().getPageSize() >= this.getPageSize().intValue())){
				crw.getCachedRowSet().setPageSize(this.getPageSize().intValue());
			}			
			
			if(null == jdbcHolder){ // First time
				jdbcHolder = createJdbcContainer(crw, resource, tenantId);
			}
			
			crw.getCachedRowSet().populate(jdbcHolder.getResultset());
		} catch (Exception ex) {
    		logger.error(ex.getMessage(), ex);
			throw new RemoteException("Execute Query fail." + ex.getMessage());
		}finally{
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit executeQuery .. Total Time Spent: " + elapsedTime);	
        	}
    	}
		return crw;
    	}
    


	public void releaseConnection(String tenantId, String requestID){
    	this.jdbcCache.remove(tenantId + ":" + requestID);
    }
    
	/**
	 * This method needs to get the connection from JasperServer
	 * @param crw
	 * @return
	 * @throws SQLException
	 */
	private JasperJdbcContainer createJdbcContainer(CachedRowSetWrapper crw, Resource resource, String tenantId) {
		long startTime = System.currentTimeMillis();
		JasperJdbcContainer jdbcContainer = new JasperJdbcContainer();
		try{
			if (logger.isDebugEnabled()){
        		logger.debug("Enter createJdbcContainer .. Start Time" + System.currentTimeMillis() );	
        	}						
			Connection conn = QueryUtil.getConnection(resource);	
			jdbcContainer.execute(conn, crw);
			this.jdbcCache.put(tenantId + ":" + crw.getRequestId(), jdbcContainer);// all went fine we put it into the cache			
		} catch (Exception ex) {
    		logger.error(ex.getMessage(), ex);
			throw new RemoteException("Execute Query fail." + ex.getMessage());
		}finally{
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit createJdbcContainer .. Total Time Spent: " + elapsedTime);	
        	}
    	}
		return jdbcContainer;
	}   
    
	public ResourceCache getJdbcCache() {
		return jdbcCache;
	}


	public void setJdbcCache(ResourceCache jdbcCache) {
		this.jdbcCache = jdbcCache;
	}


    
    public Integer getFetchSize() {
		return fetchSize;
	}


	public void setFetchSize(Integer fetchSize) {
		this.fetchSize = fetchSize;
	}


	public Integer getMaxRows() {
		return maxRows;
	}


	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}


	public Integer getPageSize() {
		return this.pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}	
	
	
	
}



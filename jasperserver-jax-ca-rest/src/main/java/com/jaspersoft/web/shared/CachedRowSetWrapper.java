/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.web.shared;

import java.io.Serializable;
import java.util.UUID;
import javax.sql.rowset.CachedRowSet;

public class CachedRowSetWrapper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CachedRowSet cachedRowSet = null;
	private String sqlException = null;
	private int currentRow = 0;
	private String requestId = null;
	private String dataSourceName = null;

	//used only for metadataRequest
	private Object[] parameters = null;
	
	
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	public CachedRowSet getCachedRowSet() {
		return cachedRowSet;
	}
	public void setCachedRowSet(CachedRowSet cachedRowSet) {
		this.cachedRowSet = cachedRowSet;
	}
	public String getSqlException() {
		return sqlException;
	}
	public void setSqlException(String sqlException) {
		this.sqlException = sqlException;
	}
	public int getCurrentRow() {
		return currentRow;
	}
	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public void generateRequestID(){
		requestId = UUID.randomUUID().toString();		
	}

}

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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.jaspersoft.web.rest.JasperRestConnector;

/**
 * JasperDriver implements java.sql.Driver. This class is registered with the DriveManager.
 * It only creates a JasperRestConnector and JasperConnection objects
 * 
 *@author bpillai@xtivia.com
 *
 */
public class JasperDriver implements Driver {

	private final static String username = "user";
	private final static String password = "password";
	private final static String pageSize = "PageSize";
	
	
	public JasperDriver(){
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {

		JasperRestConnector restConnector = new JasperRestConnector(url);

		if ( null != info ){
			restConnector.setJasperUser(this.getPropertyIgnoreCase(info, JasperDriver.username, ""));
			restConnector.setJasperPassword(this.getPropertyIgnoreCase(info, JasperDriver.password, ""));
			try{
				restConnector.setPageSize(Integer.valueOf(this.getPropertyIgnoreCase(info, pageSize, "0")));
			}catch(NumberFormatException e){
				throw new SQLException("PageSize format not correct");
			}
		}	
		//test the login to the backend
		try {
			restConnector.login();
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return new JasperConnection(restConnector);
	}
	
	/**
	  * get value from {Properties}, if no key exist then return default value.
	  * 
	  * @param props
	  * @param key
	  * @param defaultV
	  * @return
	  */
	public String getPropertyIgnoreCase(Properties prop, String key, String defaultV) {
		String value = prop.getProperty(key);
		if (null != value)
			return value;

		// Not matching with the actual key then
		Set<Entry<Object, Object>> s = prop.entrySet();
		Iterator<Entry<Object, Object>> it = s.iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			if (key.equalsIgnoreCase((String) entry.getKey())) {
				return (String) entry.getValue();
			}
		}//while
		return defaultV;
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return true;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return null;
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}

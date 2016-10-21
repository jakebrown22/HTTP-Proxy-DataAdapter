
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

package com.jaspersoft.web.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.sql.rowset.CachedRowSet;
import javax.xml.bind.DatatypeConverter;

import com.jaspersoft.web.shared.CachedRowSetWrapper;

import oracle.jdbc.rowset.OracleCachedRowSet;


/**
 * This class is the interface between the driver and the rest apis in the backend.
 * There are 4 rest methods which are invoked by this interface.
 * 
 */

public class JasperRestConnector {

	private static String initialQueryPath = "rest_v2/dbQueryExecutor/";
	private String executeQueryPath = null; // path to execute a query
	private String closeQueryPath = null; // path to execute a close	
	private String databaseMetaDataPath = null;// path to get db metadata
	private String timeZonePath = null;// path to get server timezone
	private String loginQueryPath = null;
	private int    pageSize = 5000;
	
	private static final String JasperJDBCURL = "jdbc:jaspersoft";
	private static final String cookieName =  "REQUEST_ORIGINATOR";
	private static final String cookieValue =  "js_custom_adapter";

	private String JasperDataSourceName = null;
	private String JasperUser = null;
	private String JasperPassword = null;
	private String sessionCookie = null;
	private String serverTimeZone = null;

	private static final Logger logger =
			Logger.getLogger(JasperRestConnector.class.getName());


	public JasperRestConnector(String url) throws SQLException{
		logger.setLevel(Level.FINE);
		String startUrl = parseURL(url);
		this.loginQueryPath = startUrl + "rest/login";
		setQueryPaths(startUrl + JasperRestConnector.initialQueryPath);
	}

	/**
	 * Method extracts the datasource name from end of url
	 * @param url  
	 * @return url 
	 * @throws SQLException 
	 */
	private String parseURL(String url) throws SQLException{

		if(!url.contains(JasperJDBCURL)){
			throw new SQLException("Invalid url: " + url + " must have " + JasperJDBCURL);
		}
		
		String[] tokens = url.split(";");
		if(tokens.length >= 2){
			url = tokens[0].substring(tokens[0].indexOf('@') + 1);
			if(null == url ){
				throw new SQLException("URL is invalid: " + url);
			}
			this.JasperDataSourceName = tokens[1].substring(tokens[1].indexOf('=') + 1);
			if(null == this.JasperDataSourceName){
				throw new SQLException("Datasource is invalid: " + tokens[1]);
			}
			if(!this.JasperDataSourceName.startsWith("/")){
				this.JasperDataSourceName = "/" + this.JasperDataSourceName; // add a "/" if its not present
			}
			String pagesize = null;
			if(tokens.length > 2){
				pagesize = tokens[2].substring(tokens[2].indexOf('=') + 1);
				if(null != pagesize){
					try{
						this.setPageSize(Integer.valueOf(pagesize));
					}catch(NumberFormatException e){
						throw new SQLException("PageSize format not correct");
					}
				}//if
			}//if
		}else{
			throw new SQLException("No datasource specified or missing ; between url and datasource " + url);
		}
		if(!url.endsWith("/")){
			url = url + "/";
		}
		return url;

	}

	public void setQueryPaths(String queryPath){
		executeQueryPath = queryPath + "resultdata"; // in the application the path to execute a query
		closeQueryPath = queryPath + "closeresult"; // in the application the path to close a query	
		databaseMetaDataPath = queryPath + "dbmetadata";
		timeZonePath = queryPath + "timezone";
	}

	/**
	 * Creates a CachedRowSet 
	 * @return  CachedRowSet
	 * @throws SQLException
	 */
	public CachedRowSet createCachedRowSet() throws SQLException{
		return new OracleCachedRowSet();
	}

	/**
	 * @throws SQLException ******************************************************************************************/
	// Methods to extract various properties from DatabaseMetaData object in the server 

	public Object getDBMetaData(String methodName, Object[] parameters) throws SQLException{
		
		CachedRowSetWrapper crw = new CachedRowSetWrapper();
		setWrapperProperties(crw);    	
		crw.setParameters(parameters);
		crw.setRequestId(methodName);
		
		Object result = null;
		byte[] ret = null;
		try {
			 ret =	this.sendCRWPost(this.databaseMetaDataPath, crw);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		if (null != crw.getSqlException()) {
			throw new SQLException(crw.getSqlException()); // got an SQLException throw it
		}
		if(null != ret && ret.length > 0 ){  //got some Object
			result = deserialize(ret); 
		}
		return result;
	}

	/********************************************************************************************/

	/**
	 * Method execute a request against the server
	 * @param crw
	 * @return
	 * @throws SQLException
	 */

	public CachedRowSetWrapper executeRequest(CachedRowSetWrapper crw) throws SQLException {

		setWrapperProperties(crw);
		putPageSize(crw);
		try {
			crw = (CachedRowSetWrapper) deserialize(
					this.sendCRWPost(this.executeQueryPath, crw));
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		if (null != crw.getSqlException()) {
			throw new SQLException(crw.getSqlException()); // got an SQLException throw it
		}
		return crw;
	}

	/**
	 * Execute close on the server to clean up. Only sends the requestID to the backend.
	 * @param crw
	 * @throws SQLException
	 */
	public void closeResultSet(CachedRowSetWrapper crw) throws SQLException {

		try {
			this.sentClosePost(this.closeQueryPath, crw);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}

	}
	/**
	 * This method gets the timezone the server is on.
	 * @return
	 * @throws SQLException
	 */
	
	public String getServerTimeZone() throws SQLException{
		// Need to get it only once during the instance
		if(this.serverTimeZone == null){
			try {
				HttpURLConnection con = getHttpConnection(this.timeZonePath, "0");
				con.connect();
				if( con.getResponseCode() != HttpURLConnection.HTTP_OK){
					// if we created a session and it expired call the method again
					if(checkSessionExpiredError(con.getResponseCode())){
						getServerTimeZone();
					}else{
						throw new SQLException(con.getResponseMessage());
					}	
				}
				StringBuffer sb = new StringBuffer();
				String inputLine = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}
				br.close();
				con.disconnect();
				this.serverTimeZone = sb.toString();
				logger.fine("Server TimeZone is: " + this.serverTimeZone);
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
		}
		return this.serverTimeZone;

	}
	
	/**
	 * Posts the close method to the server
	 * @param url
	 * @param crw
	 * @throws Exception
	 */
	private void sentClosePost(String url, CachedRowSetWrapper crw) throws Exception{
		
		HttpURLConnection con = getHttpConnection(url, String.valueOf(crw.getRequestId().length()));
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(crw.getRequestId());
		wr.flush();
		wr.close();

		if( con.getResponseCode() != HttpURLConnection.HTTP_OK){
			// if we created a session and it expired call the method again
			if(checkSessionExpiredError(con.getResponseCode())){
				sentClosePost(url, crw);
			}else{
				throw new SQLException(con.getResponseMessage());
			}	
		}
		con.disconnect();
	}
	/**
	 * Post a CachedRowSetWrapper to the backend
	 * @param url
	 * @param crw
	 * @return
	 * @throws Exception
	 */
	
	private byte[] sendCRWPost(String url, CachedRowSetWrapper crw) throws Exception{
		
		byte[] payload = serialize((Serializable) crw);
		HttpURLConnection con = getHttpConnection(url, String.valueOf(payload.length));
		con.getOutputStream().write(payload);
		
		if( con.getResponseCode() != HttpURLConnection.HTTP_OK){
			// if we created a session and it expired call the method again
			if(checkSessionExpiredError(con.getResponseCode())){
				sendCRWPost(url, crw);
			}else{
				throw new SQLException(con.getResponseMessage());
			}	
		}
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = con.getInputStream().read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	    }
	    buffer.flush();
	    con.disconnect();
	    return buffer.toByteArray();
	}
	
	private boolean checkSessionExpiredError(int responseCode){
		boolean expired = false;
		if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED && 
				this.sessionCookie != null &&
				(!sessionCookie.equals("NA")) ){
			expired = true;
			this.sessionCookie = null; // remove the session cookie
		}
		return expired;
	}
	/**
	 * Creates an http connection with default header values set
	 * @param url
	 * @param payloadLength
	 * @return
	 * @throws Exception
	 */
	
	private HttpURLConnection getHttpConnection(String url, String payloadLength) throws Exception{
		URL obj = new URL(url);
		HttpURLConnection con = null;
		if(url.startsWith("https")){
			con = (HttpsURLConnection) obj.openConnection();
		} else{
			con = (HttpURLConnection) obj.openConnection();
		}
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Content-length", payloadLength);
		//con.setConnectTimeout(60000); // set timeout for a min
		
		// we havent tried creating a session
		if(this.sessionCookie == null){
			this.login(); // do a login to create a session
		}
		if(this.sessionCookie.equals("NA")){ // we failed to create a session
			logger.fine("Session not created doing basic authentication");
			con.setRequestProperty("Authorization", getAuthorizationHeaderValue());
			con.setRequestProperty("Cookie", JasperRestConnector.cookieName + "=" + JasperRestConnector.cookieValue);
		}else{
			String cookie = this.sessionCookie + "; " +
                    JasperRestConnector.cookieName + "=" + JasperRestConnector.cookieValue;
			con.setRequestProperty("Cookie", cookie);
		}
		con.setDoOutput(true);
		con.setDoInput(true);
		return con;
	}
	
	/**
	 * This method uses post to attempt to login to JasperServer and create a session 
	 * @throws Exception
	 */
	public void login() throws Exception{

		String cred = "j_username=" + this.JasperUser + "&j_password=" + this.JasperPassword;
		URL obj = new URL(this.loginQueryPath);
		HttpURLConnection con = null;
		if(this.loginQueryPath.startsWith("https")){
			con = (HttpsURLConnection) obj.openConnection();
		} else{
			con = (HttpURLConnection) obj.openConnection();
		}
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-length", Integer.toString(cred.length()));
		con.setDoOutput(true);
		con.setDoInput(true);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(cred);
		wr.flush();
		wr.close();

		if( con.getResponseCode() != HttpURLConnection.HTTP_OK){
			
			logger.log(Level.SEVERE, con.getResponseMessage());
			
			if( con.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN ){
				throw new SQLException("Invalid login/password or Org id " + con.getResponseMessage());
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND ) {
				throw new SQLException("Server unavailable " + con.getResponseMessage());
			}else{
				throw new SQLException(con.getResponseMessage());
			}	
		}//if
		
		Map<String, List<String>> headers = con.getHeaderFields();
		StringBuilder sb = new StringBuilder();
		List<String> cookies = headers.get("Set-Cookie");
		
		if (cookies != null && cookies.size() > 0) {
			for (String str : cookies) {
				sb.append(str).append(",");
			}
			String cookie = sb.toString();
			if (cookie.length() > 1 && cookie.endsWith(",")) {
				cookie = cookie.substring(0, cookie.length() - 1);
			}
			
			this.sessionCookie =  cookie;
		}//if
		
		// Failed to create session fallback to basic authentication
		if( null == this.sessionCookie){
			logger.log(Level.SEVERE, "Failed to create session");
			sessionCookie = "NA";
		}
		con.disconnect();
	}

/*************************************************************************************************************************/
	/**
	 * If page size not set, set the default size if maxrows = 0 or greater than pagesize
	 * @param crw
	 * @throws SQLException
	 */
	private void putPageSize(CachedRowSetWrapper crw) throws SQLException{
		
		if( (crw.getCachedRowSet().getMaxRows() > this.pageSize) || (0 >= crw.getCachedRowSet().getMaxRows()) ){
			crw.getCachedRowSet().setPageSize(this.pageSize);
		}
	}
	
	/**
	 * Set default wrapper properties
	 * @param crw
	 */
	private void setWrapperProperties(CachedRowSetWrapper crw){
		//set datasource in the CachedRowSetWrapper
		crw.setSqlException(null); // remove any exception if there
		crw.setDataSourceName(this.JasperDataSourceName);
	}
	
	private String getAuthorizationHeaderValue(){
        String usernameAndPassword = this.JasperUser + ":" + this.JasperPassword;
        return "Basic " + DatatypeConverter.printBase64Binary( usernameAndPassword.getBytes() );
 	}
	/****************************************************************************************/
	//Getters and setters
	public String getJasperDataSourceName() {
		return JasperDataSourceName;
	}

	public void setJasperDataSourceName(String jasperDataSourceName) {
		JasperDataSourceName = jasperDataSourceName;
	}

	public String getJasperUser() {
		return JasperUser;
	}

	public void setJasperUser(String jasperUser) {
		JasperUser = jasperUser.trim();
	}

	public String getJasperPassword() {
		return JasperPassword;
	}

	public void setJasperPassword(String jasperPassword) {
		JasperPassword = jasperPassword.trim();
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize){
		// if its greater than 10 set it else ignore 
		if(pageSize > 10){
			this.pageSize = pageSize;
		}	
	}
	
	/**
	 * DeSerialize a byte array
	 * @param input
	 * @return
	 */
	public static Object deserialize(byte[] input) {
		
		Object obj = null;
		Exception exp = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(input);
		ObjectInputStream jois = null;
		try {
			jois = new ObjectInputStream(bais);
			obj =  jois.readObject();
		} catch (IOException e) {
			exp = e;
		} catch (ClassNotFoundException e) {
			exp = e;
		}finally{
			try {
				if(null != jois){
					jois.close();
				}	
				bais.close();				
			} catch (IOException e1) {
			}
			
			if(null != exp ){
				throw new RuntimeException(exp);
			}	
		}
		return obj;
	}
	
	/**
	 * Serialize to a byte array
	 * @param input
	 * @return
	 */
	public static byte[] serialize(Object input) {
		
		byte[] output = null;
		Exception exp = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(input);
			output = bos.toByteArray();
		} catch (IOException e) {
			exp = e;
		}finally{
			try {
				if(null != oos){
					oos.close();
				}	
				bos.close();
			} catch (IOException e) {
			}
			if(null != exp){
				throw new RuntimeException(exp);
			}
		}
		
		return output;
	}
	

}

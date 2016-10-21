/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  A resource cache to hold multiple JasperJdbcContainers and close it after 
 *  a preset time interval. 
 *   
 */
public class ResourceCache {
	protected static final Log logger = LogFactory.getLog(ResourceCache.class);
	private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	private Checker check = null;
	private long timeToLive = 600000; // 10 mins
	private long napTime = 600000; // 10 mins
	

	/**
	 * Two constructors both initialize the Checker thread 
	 */
	public ResourceCache(){
		startCheckerThread();
	};

	public ResourceCache(long timeToLive){
		this.timeToLive = timeToLive;
		startCheckerThread();
	}
	
	private void startCheckerThread(){
		check = new Checker(this);
		check.setDaemon(true);
		check.start();
	}	
	/**************************************************************/
	/**
	 * Add a new entry to the cache
	 * @param key  requestid
	 * @param value JasperJdbcContainer
	 */
	public void put(String key, Object value){
    	long startTime = System.currentTimeMillis();		
		try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter put method .. Start Time" + System.currentTimeMillis() );	
        		logger.debug("Cache key: " + key);
        	}    			
        	
			cleanCache(); // clean up before putting
			cache.put(key, new Object[]{System.currentTimeMillis(), value});			
		}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit put .. Total Time Spent: " + elapsedTime);	
        	}			
		}
	}

	/**
	 * Returns the JasperJdbcContainer if available and also extends it life
	 * @param key RequestId
	 * @return JasperJdbcContainer
	 */
	public JasperJdbcContainer get(String key){
    	long startTime = System.currentTimeMillis();		
		JasperJdbcContainer holder = null;
		try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter get method .. Start Time" + System.currentTimeMillis() );	
        		logger.debug("Cache key: " + key);
        	}    			
        	
			Object[] value = (Object[]) cache.get(key);
			if( null != value){
				holder = (JasperJdbcContainer) value[1];
				value[0] = System.currentTimeMillis();//extend its life since it has been touched
			}
		}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit get .. Total Time Spent: " + elapsedTime);	
        	}						
		}
		return holder;
	}

	/**
	 * Removes a JasperJdbcContainer from memory and invokes it close method 
	 * @param key
	 */
	public void remove(String key){
    	long startTime = System.currentTimeMillis();		
		try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter remove method .. Start Time" + System.currentTimeMillis() );	
        		logger.debug("Cache key: " + key);
        	}    			
        	
			JasperJdbcContainer jjh = (JasperJdbcContainer) get(key);
			if(null != jjh){
				jjh.close();
			}	
			this.cache.remove(key);			
		}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit remove .. Total Time Spent: " + elapsedTime);	
        	}									
		}

	}

	/**
	 * Iterates through the cache and cleans out JasperJdbcContainer objects that have 
	 * expired.
	 */
	public synchronized void cleanCache(){
    	long methodStartTime = System.currentTimeMillis();
    	try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter cleanCache  .. Start Time" + System.currentTimeMillis() );	
        	}    			
        	
    		Iterator<?> it = this.cache.entrySet().iterator();
    		while (it.hasNext()) {
    			@SuppressWarnings("rawtypes")
    			Map.Entry pair = (Map.Entry)it.next();
    			Object[] value = (Object[]) pair.getValue();
    			Long startTime = (Long) value[0];
            	if (logger.isDebugEnabled()){
            		logger.debug("Cache Start Time: " + startTime );	
            		logger.debug("Cache Time to live: " + timeToLive );
            	}
            	
    			if( (startTime + timeToLive) < System.currentTimeMillis()){
    				remove((String) pair.getKey());
    			}	
    		}//while    		
    	}finally{
    		if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - methodStartTime;
        		logger.debug("Exit cleanCache .. Total Time Spent: " + elapsedTime);	
        	}							    		
    	}
	
	}

	/**
	 * Private thread class which wakes up to clean the cache at fixed 
	 * intervals
	 *
	 */
	private class Checker extends Thread{

		private ResourceCache resCache = null;
		//private long napTime = 600000; // 10 mins

		public Checker(ResourceCache rs){
			this.resCache = rs;
		}
		
		public void run(){
			while(true){
				try {
					Thread.sleep(napTime);
					this.resCache.cleanCache();
				} catch (InterruptedException e) {
				}
			}
		}//run	
	}//Checker

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public long getNapTime() {
		return napTime;
	}

	public void setNapTime(long napTime) {
		this.napTime = napTime;
	}
	
	
}//ResourceCache

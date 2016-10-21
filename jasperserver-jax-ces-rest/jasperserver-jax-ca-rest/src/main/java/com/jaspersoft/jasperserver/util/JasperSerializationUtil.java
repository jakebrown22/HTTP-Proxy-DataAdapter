/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.web.shared.CachedRowSetWrapper;

/**
 * This class is a custom (de)serialization utility 
 * @author Xtivia
 */
public class JasperSerializationUtil{
	private static final Log logger = LogFactory.getLog(JasperSerializationUtil.class);
	
	/**
	 * DeSerialize a byte array
	 * @param input
	 * @return
	 */
	public static Object deserialize(byte[] input) {
		
		Object obj = null;
		Exception exp = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(input);
		JasperObjectInputStream jois = null;
    	long startTime = System.currentTimeMillis();
		try {
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter deserialize .. Start Time" + System.currentTimeMillis() );	
        	}    		
        	
			jois = new JasperSerializationUtil().new JasperObjectInputStream(bais);
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
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit deserialize .. Total Time Spent: " + elapsedTime);	
        	}
        	
			if(null != exp ){
				logger.error(exp.getMessage(), exp);
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
    	long startTime = System.currentTimeMillis();		
		try {
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter serialize .. Start Time" + System.currentTimeMillis() );	
        	}    		
        	
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
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit serialize .. Total Time Spent: " + elapsedTime);	
        	}
        	
			if(null != exp){
				throw new RuntimeException(exp);
			}
		}
		
		return output;
	}
    
	
	/**************************************************************************************/
	/**
	 * Private inner class which extends ObjectInputStream and implements the forward looking fix for 
 * serialized java object. 
	 * @author Xtivia
	 *
	 */
	
    private class JasperObjectInputStream extends  ObjectInputStream {
    	
    	private boolean foundWrapper = false;
    	
		public JasperObjectInputStream(InputStream in) throws IOException {
			super(in);
		}
		
	    /**
	     * Only check our expected CachedRowSetWrapper class ignore its sub classes as they deserialize
	     */
	    @Override
	    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
	            ClassNotFoundException {
	    	
	        if (desc.getName().equals(CachedRowSetWrapper.class.getName())) {
	        	// check if the class has the cachedRowSet field if so the object is ours
	        	if(desc.getField("cachedRowSet") != null){
	           	 	foundWrapper = true;
	        	} 
	        }
	        
	        if(!foundWrapper){
	            throw new InvalidClassException(
	                    "Unauthorized deserialization attempt",
	                    desc.getName());
	        }
	        
	    	//System.out.println(desc.getName());
	        return super.resolveClass(desc);
	    }
	    
    	
    }//JasperObjectInputStream
	/**************************************************************************************/  
}

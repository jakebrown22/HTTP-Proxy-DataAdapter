/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.
* http://www.jaspersoft.com.
*
* Unless you have purchased  a commercial license agreement from Jaspersoft,
* the following license terms  apply:
*
* This program is free software: you can redistribute it and/or  modify
* it under the terms of the GNU Affero General Public License  as
* published by the Free Software Foundation, either version 3 of  the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero  General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package com.jaspersoft.ji.jaxrs.dbquery;

import java.io.Serializable;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.api.metadata.user.domain.impl.client.MetadataUserDetails;
import com.jaspersoft.jasperserver.api.metadata.user.service.UserAuthorityService;
import com.jaspersoft.jasperserver.remote.dbservices.ExecuteQueryService;
import com.jaspersoft.jasperserver.remote.dbservices.MetaDataService;
import com.jaspersoft.jasperserver.remote.resources.converters.ResourceConverterProvider;
import com.jaspersoft.jasperserver.remote.services.SingleRepositoryService;
import com.jaspersoft.jasperserver.util.JasperSerializationUtil;
import com.jaspersoft.jasperserver.util.QueryUtil;
import com.jaspersoft.ji.license.LicenseCheckStatus;
import com.jaspersoft.ji.license.LicenseException;
import com.jaspersoft.ji.license.LicenseManager;
import com.jaspersoft.web.shared.CachedRowSetWrapper;


@Component("dbQueryExecutorJaxrsService")
@Path("/dbQueryExecutor")
public class DBQueryExecutorJaxrsService {
	private static final Log logger = LogFactory.getLog(DBQueryExecutorJaxrsService.class);
	
    @javax.annotation.Resource
    private SingleRepositoryService singleRepositoryService;
    
    @javax.annotation.Resource
    private ResourceConverterProvider resourceConverterProvider;

    @javax.annotation.Resource
    private MetaDataService jdbcMetaDataService;    
    
    @javax.annotation.Resource
    private ExecuteQueryService executeQueryService;
    
    @javax.annotation.Resource
    private UserAuthorityService userAuthorityService;
    
    @javax.annotation.Resource(name="licenseManager")
    private LicenseManager licenseManager;    
    
    @javax.annotation.Resource(name = "messageSource")
    private MessageSource messages;    
    
    
    private String requestOrgCheck; 
    
    @POST
    @Path("/dbmetadata")
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public byte[] getDBMetaData(final @Context HttpHeaders headers, byte[] request){
    	Resource resource = null;
    	byte[] result = null;
    	CachedRowSetWrapper crw = null;
    	long startTime = System.currentTimeMillis();
    	try{
        	if (logger.isDebugEnabled()){
        		logger.debug("Enter getDBMetaData .. Start Time" + System.currentTimeMillis() );	
        	}    		
        	QueryUtil.validateRequest(headers, requestOrgCheck);
    		crw = (CachedRowSetWrapper) JasperSerializationUtil.deserialize(request);
    		resource = singleRepositoryService.getResource(crw.getDataSourceName());
    		result = jdbcMetaDataService.getDBMetaData(resource, crw);
    	}catch (Exception ex){
    		logger.error(ex.getMessage(), ex);
    		crw.setSqlException(ex.getMessage());
    	}finally{
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit getDBMetaData .. Total Time Spent: " + elapsedTime);	
        	}
    	}
    	return result;
    }   
    
    
	@POST
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	@Path("/resultdata")
	public byte[] execGetResultSet(final @Context HttpHeaders headers, byte[] request){
    	long startTime = System.currentTimeMillis();
		Resource resource = null;
		CachedRowSetWrapper crw = null;
    	//String tenantId = ((com.jaspersoft.jasperserver.api.metadata.user.domain.TenantQualified) principal).getTenantId();
		try{
	    	if (logger.isDebugEnabled()){
	    		logger.debug("Enter execGetResultSet .. Start Time: " + System.currentTimeMillis() );	
	    	}
	    	  /* Checking if license is valid and custom jdbc driver feature supported */
	    	checkLicense();
	    	
			Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MetadataUserDetails mud = (MetadataUserDetails) principal;
			String tenantId = mud.getTenantId() != null ? mud.getTenantId(): "";	    	
	    	
	    	if (logger.isTraceEnabled()){
	    		logger.trace("Tenant Id: " + tenantId);	
	    		logger.trace("User Name: " + mud.getUsername());
	    	}
	    	
			QueryUtil.validateRequest(headers, requestOrgCheck);
			crw = (CachedRowSetWrapper) JasperSerializationUtil.deserialize(request);
			resource = singleRepositoryService.getResource(crw.getDataSourceName());
			crw = executeQueryService.executeQuery(resource, crw, tenantId);
		}catch (Exception ex){
    		logger.error(ex.getMessage(), ex);
    		crw.setSqlException(ex.getMessage());
    	}finally{
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit execGetResultSet .. Total Time Spent: " + elapsedTime);	
        	}
    	}
		return JasperSerializationUtil.serialize((Serializable) crw);
	}      
	
	/**
	 * This method removes the JasperJdbcContainer object from the cache
	 * @param requestID
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/closeresult")
	public Response releaseConnection(final @Context HttpHeaders headers, String requestID){
    	long startTime = System.currentTimeMillis();
		try{
			if (logger.isDebugEnabled()){
	    		logger.debug("Enter releaseConnection .. Start Time" + System.currentTimeMillis() );	
	    	}
	    	Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MetadataUserDetails mud = (MetadataUserDetails) principal;
			String tenantId = mud.getTenantId() != null ? mud.getTenantId(): "";
			
			QueryUtil.validateRequest(headers, requestOrgCheck);
			executeQueryService.releaseConnection(tenantId, requestID);			
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			return Response.serverError().build();
		}finally{
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit releaseConnection .. Total Time Spent: " + elapsedTime);	
        	}
    	}
		return Response.ok().build();
	}	

	
	/**
	 * This method removes the JasperJdbcContainer object from the cache
	 * @param requestID
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/timezone")	
	public Response getTimeZone(final @Context HttpHeaders headers){
    	long startTime = System.currentTimeMillis();		
    	String result = "";
		try{
			if (logger.isDebugEnabled()){
	    		logger.debug("Enter getTimeZone .. Start Time" + System.currentTimeMillis() );	
	    	}			
			QueryUtil.validateRequest(headers, requestOrgCheck);
			result = TimeZone.getDefault().getID();			
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			return Response.serverError().build();
		}finally{
        	if (logger.isDebugEnabled()){
        		long elapsedTime = System.currentTimeMillis() - startTime;
        		logger.debug("Exit getTimeZone .. Total Time Spent: " + elapsedTime);	
        	}
    	}
		return Response.status(200).entity(result).build();
	}
	
	
   @GET
   @Path("/verify")
   @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
   public Response verify(){
		String result = "Jasper REST Services Successfully started..";
		return Response.status(200).entity(result).build();
	}


   public String getRequestOrgCheck() {
	   return requestOrgCheck;
   }


   public void setRequestOrgCheck(String requestOrgCheck) {
	   this.requestOrgCheck = requestOrgCheck;
   }
  
   private void checkLicense() throws LicenseException {
		long startTime = System.currentTimeMillis();    	
   	try{
			if (logger.isDebugEnabled()){
       		logger.debug("Enter checkLicense .. Start Time" + System.currentTimeMillis() );	
       	}							    		
           LicenseCheckStatus licenseCheckStatus = licenseManager.checkLicense();
           if (licenseCheckStatus.isLicenseAccepted()) {
               if (!licenseManager.isFeatureSupported("AHD")) {
                   String message = getMessages().getMessage("LIC_014_feature.not.licensed.domains", null, LocaleContextHolder.getLocale());
                   throw new LicenseException(message);
               }
           } else {
               throw new LicenseException("License fail." + licenseCheckStatus.getMessage());
           }    		
   	}finally{
       	if (logger.isDebugEnabled()){
       		long elapsedTime = System.currentTimeMillis() - startTime;
       		logger.debug("Exit checkLicense .. Total Time Spent: " + elapsedTime);	
       	}    		
   	}

   }
   
	private MessageSource getMessages() {
		return messages;
	}

   
}

/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.

 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.remote.dbservices;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.ji.license.LicenseException;
import com.jaspersoft.web.shared.CachedRowSetWrapper;


public interface ExecuteQueryService {

	public CachedRowSetWrapper executeQuery(Resource resource, CachedRowSetWrapper crw, String tenantId) throws RemoteException, LicenseException;
	public void releaseConnection(String tenanrtId, String requestID);

}

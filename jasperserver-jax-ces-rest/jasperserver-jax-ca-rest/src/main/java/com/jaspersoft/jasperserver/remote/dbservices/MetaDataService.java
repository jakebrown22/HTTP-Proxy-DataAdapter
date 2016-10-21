/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.

 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.remote.dbservices;

import javax.sql.rowset.CachedRowSet;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.ji.license.LicenseException;
import com.jaspersoft.web.shared.CachedRowSetWrapper;


public interface MetaDataService {
	public byte[] getDBMetaData(Resource resource, CachedRowSetWrapper crw)  throws RemoteException, LicenseException;	
	
}

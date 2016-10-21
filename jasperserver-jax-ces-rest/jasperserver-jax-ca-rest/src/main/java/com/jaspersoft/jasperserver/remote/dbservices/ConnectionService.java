/*
 * Copyright (C) 2005 - 2015 TIBCO Software Inc. All rights reserved.

 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
package com.jaspersoft.jasperserver.remote.dbservices;

import java.sql.Connection;

import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.remote.exception.RemoteException;
import com.jaspersoft.ji.license.LicenseException;


public interface ConnectionService {

    public Connection getConnection(Resource resource) throws RemoteException, LicenseException;


}

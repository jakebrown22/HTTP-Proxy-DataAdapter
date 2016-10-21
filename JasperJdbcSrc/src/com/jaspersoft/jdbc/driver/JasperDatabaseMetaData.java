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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.jaspersoft.web.rest.JasperRestConnector;

/**
 * This class implements the DatabaseMetaData interface and simple invokes the JasperRestConnector.java's
 * one single method getDBMeta data. Reflection is used to get all db meta data information. 
 * @author bpillai@xtivia.com
 *
 */
public class JasperDatabaseMetaData implements DatabaseMetaData {

	private JasperRestConnector restConnector =  null;
	private JasperConnection connection = null;

	public JasperDatabaseMetaData(JasperConnection connection, JasperRestConnector rInvoke ){
		this.restConnector = rInvoke;
		this.connection = connection;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public java.lang.String getURL() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getURL", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getAttributes(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getAttributes", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		Object obj = restConnector.getDBMetaData( "isReadOnly", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean allProceduresAreCallable() throws SQLException {
		Object obj = restConnector.getDBMetaData( "allProceduresAreCallable", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		Object obj = restConnector.getDBMetaData( "allTablesAreSelectable", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		Object obj = restConnector.getDBMetaData( "autoCommitFailureClosesAllResultSets", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		Object obj = restConnector.getDBMetaData( "dataDefinitionCausesTransactionCommit", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "dataDefinitionIgnoredInTransactions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean deletesAreDetected(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "deletesAreDetected", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		Object obj = restConnector.getDBMetaData( "doesMaxRowSizeIncludeBlobs", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public java.sql.ResultSet getBestRowIdentifier(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int arg3, boolean arg4) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getBestRowIdentifier", new Object[]{arg0, arg1, arg2, arg3, arg4});
	}

	@Override
	public java.lang.String getCatalogSeparator() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getCatalogSeparator", new Object[]{});
	}

	@Override
	public java.lang.String getCatalogTerm() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getCatalogTerm", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getCatalogs() throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getCatalogs", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getClientInfoProperties() throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getClientInfoProperties", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getColumnPrivileges(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getColumnPrivileges", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public java.sql.ResultSet getColumns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getColumns", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public java.sql.ResultSet getCrossReference(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getCrossReference", new Object[]{arg0, arg1, arg2, arg3, arg4, arg5});
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getDatabaseMinorVersion", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public java.lang.String getDatabaseProductName() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getDatabaseProductName", new Object[]{});
	}

	@Override
	public java.lang.String getDatabaseProductVersion() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getDatabaseProductVersion", new Object[]{});
	}

	@Override
	public int getDefaultTransactionIsolation() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getDefaultTransactionIsolation", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getDriverMajorVersion() {
		Object obj = null;
		try {
			obj = restConnector.getDBMetaData( "getDriverMajorVersion", new Object[]{});
		} catch (SQLException e) {
		}
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getDriverMinorVersion(){
		Object obj = null;
		try {
			obj = restConnector.getDBMetaData( "getDriverMinorVersion", new Object[]{});
		} catch (SQLException e) {
		}
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public java.lang.String getDriverName() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getDriverName", new Object[]{});
	}

	@Override
	public java.lang.String getDriverVersion() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getDriverVersion", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getExportedKeys(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getExportedKeys", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.lang.String getExtraNameCharacters() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getExtraNameCharacters", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getFunctionColumns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getFunctionColumns", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public java.sql.ResultSet getFunctions(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getFunctions", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.lang.String getIdentifierQuoteString() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getIdentifierQuoteString", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getImportedKeys(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getImportedKeys", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.sql.ResultSet getIndexInfo(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, boolean arg3, boolean arg4) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getIndexInfo", new Object[]{arg0, arg1, arg2, arg3, arg4});
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getJDBCMajorVersion", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getJDBCMinorVersion", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxBinaryLiteralLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxCatalogNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxCharLiteralLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxColumnNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxColumnsInGroupBy", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxColumnsInIndex", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxColumnsInOrderBy", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxColumnsInSelect() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxColumnsInSelect", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxColumnsInTable() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxColumnsInTable", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxConnections() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxConnections", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxCursorNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxCursorNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxIndexLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxIndexLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

/*	@Override
	public long getMaxLogicalLobSize() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxLogicalLobSize", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (long) obj;
	}
*/
	@Override
	public int getMaxProcedureNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxProcedureNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxRowSize() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxRowSize", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxSchemaNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxSchemaNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxStatementLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxStatementLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxStatements() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxStatements", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxTableNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxTableNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxTablesInSelect() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxTablesInSelect", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public int getMaxUserNameLength() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getMaxUserNameLength", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public java.lang.String getNumericFunctions() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getNumericFunctions", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getPrimaryKeys(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getPrimaryKeys", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.sql.ResultSet getProcedureColumns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getProcedureColumns", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public java.lang.String getProcedureTerm() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getProcedureTerm", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getProcedures(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getProcedures", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.sql.ResultSet getPseudoColumns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getPseudoColumns", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getResultSetHoldability", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public java.sql.RowIdLifetime getRowIdLifetime() throws SQLException {
		return (java.sql.RowIdLifetime) restConnector.getDBMetaData( "getRowIdLifetime", new Object[]{});
	}

	@Override
	public java.lang.String getSQLKeywords() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getSQLKeywords", new Object[]{});
	}

	@Override
	public int getSQLStateType() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getSQLStateType", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public java.lang.String getSchemaTerm() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getSchemaTerm", new Object[]{});
	}

	@Override
	public java.lang.String getSearchStringEscape() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getSearchStringEscape", new Object[]{});
	}

	@Override
	public java.lang.String getStringFunctions() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getStringFunctions", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getSuperTables(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getSuperTables", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.sql.ResultSet getSuperTypes(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getSuperTypes", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.lang.String getSystemFunctions() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getSystemFunctions", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getTablePrivileges(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getTablePrivileges", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public java.sql.ResultSet getTableTypes() throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getTableTypes", new Object[]{});
	}

	@Override
	public java.lang.String getTimeDateFunctions() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getTimeDateFunctions", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getTypeInfo() throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getTypeInfo", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getUDTs(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, int[] arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getUDTs", new Object[]{arg0, arg1, arg2, arg3});
	}

	@Override
	public java.lang.String getUserName() throws SQLException {
		return (java.lang.String) restConnector.getDBMetaData( "getUserName", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getVersionColumns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getVersionColumns", new Object[]{arg0, arg1, arg2});
	}

	@Override
	public boolean insertsAreDetected(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "insertsAreDetected", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException {
		Object obj = restConnector.getDBMetaData( "isCatalogAtStart", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean locatorsUpdateCopy() throws SQLException {
		Object obj = restConnector.getDBMetaData( "locatorsUpdateCopy", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		Object obj = restConnector.getDBMetaData( "nullPlusNonNullIsNull", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException {
		Object obj = restConnector.getDBMetaData( "nullsAreSortedAtEnd", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean nullsAreSortedAtStart() throws SQLException {
		Object obj = restConnector.getDBMetaData( "nullsAreSortedAtStart", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean nullsAreSortedHigh() throws SQLException {
		Object obj = restConnector.getDBMetaData( "nullsAreSortedHigh", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean nullsAreSortedLow() throws SQLException {
		Object obj = restConnector.getDBMetaData( "nullsAreSortedLow", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean othersDeletesAreVisible(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "othersDeletesAreVisible", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean othersInsertsAreVisible(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "othersInsertsAreVisible", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean othersUpdatesAreVisible(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "othersUpdatesAreVisible", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean ownDeletesAreVisible(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "ownDeletesAreVisible", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean ownInsertsAreVisible(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "ownInsertsAreVisible", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean ownUpdatesAreVisible(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "ownUpdatesAreVisible", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "storesLowerCaseIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "storesLowerCaseQuotedIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "storesMixedCaseIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "storesMixedCaseQuotedIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "storesUpperCaseIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "storesUpperCaseQuotedIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsANSI92EntryLevelSQL", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsANSI92FullSQL() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsANSI92FullSQL", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsANSI92IntermediateSQL", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsAlterTableWithAddColumn", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsAlterTableWithDropColumn", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsBatchUpdates() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsBatchUpdates", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCatalogsInDataManipulation", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCatalogsInIndexDefinitions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCatalogsInPrivilegeDefinitions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCatalogsInProcedureCalls", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCatalogsInTableDefinitions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsColumnAliasing() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsColumnAliasing", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsConvert(int arg0, int arg1) throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsConvert", new Object[]{arg0, arg1});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsConvert() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsConvert", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCoreSQLGrammar", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsCorrelatedSubqueries", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsDataDefinitionAndDataManipulationTransactions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsDataManipulationTransactionsOnly", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsDifferentTableCorrelationNames", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsExpressionsInOrderBy", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsExtendedSQLGrammar", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsFullOuterJoins() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsFullOuterJoins", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsGetGeneratedKeys", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsGroupBy() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsGroupBy", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsGroupByBeyondSelect", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsGroupByUnrelated() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsGroupByUnrelated", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsIntegrityEnhancementFacility", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsLikeEscapeClause() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsLikeEscapeClause", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsLimitedOuterJoins", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsMinimumSQLGrammar", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsMixedCaseIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsMixedCaseQuotedIdentifiers", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsMultipleOpenResults() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsMultipleOpenResults", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsMultipleResultSets() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsMultipleResultSets", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsMultipleTransactions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsMultipleTransactions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsNamedParameters() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsNamedParameters", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsNonNullableColumns() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsNonNullableColumns", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsOpenCursorsAcrossCommit", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsOpenCursorsAcrossRollback", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsOpenStatementsAcrossCommit", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsOpenStatementsAcrossRollback", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsOrderByUnrelated() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsOrderByUnrelated", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsOuterJoins() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsOuterJoins", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsPositionedDelete() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsPositionedDelete", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsPositionedUpdate() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsPositionedUpdate", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

/*	@Override
	public boolean supportsRefCursors() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsRefCursors", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}
*/
	@Override
	public boolean supportsResultSetConcurrency(int arg0, int arg1) throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsResultSetConcurrency", new Object[]{arg0, arg1});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsResultSetHoldability(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsResultSetHoldability", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsResultSetType(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsResultSetType", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSavepoints() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSavepoints", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSchemasInDataManipulation", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSchemasInIndexDefinitions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSchemasInPrivilegeDefinitions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSchemasInProcedureCalls", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSchemasInTableDefinitions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSelectForUpdate() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSelectForUpdate", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsStatementPooling() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsStatementPooling", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsStoredFunctionsUsingCallSyntax", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsStoredProcedures() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsStoredProcedures", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSubqueriesInComparisons", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSubqueriesInExists() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSubqueriesInExists", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSubqueriesInIns() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSubqueriesInIns", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsSubqueriesInQuantifieds", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsTableCorrelationNames() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsTableCorrelationNames", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsTransactionIsolationLevel(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsTransactionIsolationLevel", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsTransactions() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsTransactions", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsUnion() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsUnion", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean supportsUnionAll() throws SQLException {
		Object obj = restConnector.getDBMetaData( "supportsUnionAll", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean updatesAreDetected(int arg0) throws SQLException {
		Object obj = restConnector.getDBMetaData( "updatesAreDetected", new Object[]{arg0});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean usesLocalFilePerTable() throws SQLException {
		Object obj = restConnector.getDBMetaData( "usesLocalFilePerTable", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public boolean usesLocalFiles() throws SQLException {
		Object obj = restConnector.getDBMetaData( "usesLocalFiles", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public java.sql.Connection getConnection() throws SQLException {
		return (java.sql.Connection) this.connection;//restConnector.getDBMetaData( "getConnection", new Object[]{});
	}

	@Override
	public java.sql.ResultSet getSchemas(java.lang.String arg0, java.lang.String arg1) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getSchemas", new Object[]{arg0, arg1});
	}

	@Override
	public java.sql.ResultSet getSchemas() throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getSchemas", new Object[]{});
	}

	@Override
	public int getDatabaseMajorVersion() throws SQLException {
		Object obj = restConnector.getDBMetaData( "getDatabaseMajorVersion", new Object[]{});
		if( obj == null ){
			obj = 0;
		}
		return (int) obj;
	}

	@Override
	public boolean generatedKeyAlwaysReturned() throws SQLException {
		Object obj = restConnector.getDBMetaData( "generatedKeyAlwaysReturned", new Object[]{});
		if( obj == null ){
			obj = false;
		}
		return (boolean) obj;
	}

	@Override
	public java.sql.ResultSet getTables(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, String[] arg3) throws SQLException {
		return (java.sql.ResultSet) restConnector.getDBMetaData( "getTables", new Object[]{arg0, arg1, arg2, arg3});
	}

}

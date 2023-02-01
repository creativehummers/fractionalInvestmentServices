/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions;

import lombok.extern.java.Log;

import java.util.List;

/*
 *  Postgre specific features if required to be included here, overriding any of super class functions
 */
@Log
public class PostgreSQLJDBC extends AbstractSQLJDBC {

	public static PostgreSQLJDBC getInstance(String poolName){
		PostgreSQLJDBC instance =  new PostgreSQLJDBC();
		instance.createPooledConnection(poolName);
		return instance;
	}

	@Override
	public List<List< Object>> executeSelect(String queryIdentifier, List params) {
		return super.executeSelect(queryIdentifier, params);
	}

	@Override
	public List<List<Object>> executeUpdate(String queryIdentifier, List params) {
		return super.executeUpdate(queryIdentifier, params);
	}
}



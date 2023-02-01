/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions;

import com.dbtransactions.enums.DBName;
import com.dbtransactions.enums.QueryType;
import com.dbtransactions.enums.ResultFormat;
import com.dbtransactions.transform.CSVData;
import com.dbtransactions.transform.JSONColumnDataRows;
import com.dbtransactions.transform.JSONObjectsList;
import com.dbtransactions.transform.ResultTransformerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/*
 *   Provide the JAVA Method APIs to instantiate and run CRUD queries
 *   Both:
 *       serverless capabilities of single execution call sequence
 *       connection pooling based interface through JNDI
 *   handleRequest() method with parameters qualifying it switch to: CRUD, SP, functions..
 */
public class DatabaseTransactions {

    private static final HashMap<ResultFormat, ResultTransformerInterface> GetResultTransformersMap = resultTransformersMap();
    private static final Hashtable<String, DatabaseTransactions> DBPoolInstanceMap = new Hashtable<>();
    public static final String Singleton = "Singleton";
    protected AbstractSQLJDBC sqljdbcInstance;    //Instantiated in the constructor

    /*
     *   Create the instance like a factory
     */
    public DatabaseTransactions(DBName dbmsName, String poolName) {
        SQLQueries.readQueriesFile();
        switch (dbmsName) {
            case PostGreSQLJDBC:
                sqljdbcInstance = PostgreSQLJDBC.getInstance(poolName); //have instantiated pool/connections
                break;
            case OracleSQLJDBC:
                sqljdbcInstance = OracleSQLJDBC.getInstance(poolName); //have instantiated pool/connections
                break;
            default:
                throw new RuntimeException("Must Provide valid dbmsName");
        }
    }

    /*
    *   Map ResultFormat to the concrete class instances to #transform the dbData to format required
     */
    private static HashMap<ResultFormat, ResultTransformerInterface> resultTransformersMap() {
        HashMap<ResultFormat, ResultTransformerInterface> transformers = new HashMap<ResultFormat, ResultTransformerInterface>();
        transformers.put(ResultFormat.CSVData, new CSVData());
        transformers.put(ResultFormat.JSONColumnDataRows, new JSONColumnDataRows());
        transformers.put(ResultFormat.JSONObjectsList, new JSONObjectsList());

        return transformers;
    }

    /*
     *   Only getter for the class instance with getInstance(DBTransactionsExecutor.Singleton) for unique single instance
     */
    public synchronized DatabaseTransactions getInstance(DBName dbmsName, String poolName, ResultFormat formatType) {
        DatabaseTransactions inst = DBPoolInstanceMap.get(poolName);
        if (inst == null)
            DBPoolInstanceMap.put(poolName, (inst = new DatabaseTransactions(dbmsName, poolName)));

        return inst;
    }

    /*
     *     The api method to invoke and execute the query
     */
    public String handleRequest(String queryIdentifier, List params, ResultFormat formatType) {
        String query = SQLQueries.getQuery(queryIdentifier);
        QueryType queryType = SQLQueries.getQueryType(query);
        List<List<Object>> result = new ArrayList<List<Object>>();
        //TODO: can use switch case OR pattern to delegate to executeXXXX method
        if (queryType == QueryType.SELECT)
            result = sqljdbcInstance.executeSelect(query, params);
        else if (queryType == QueryType.UPDATE)
            result = sqljdbcInstance.executeUpdate(query, params);

        return convertToResultFormat(result, formatType);
    }

    /*
    *   Fixed the return format as String form of the ResultFormat: csv/json
    *   Create polymorphic method if required for different return format
     */
    private String convertToResultFormat(List dbData, ResultFormat formatType) {
        return (String) GetResultTransformersMap.get(formatType).transform(dbData);
    }

}





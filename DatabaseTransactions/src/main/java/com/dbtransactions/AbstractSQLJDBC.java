/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions;

import com.dbtransactions.enums.ColumnMetaData;
import com.dbtransactions.transform.ResultSetAsListOfHashMaps;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.java.Log;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
*   Implement concrete classes for the databases target required
*   Implementation for common functionality can be provided
 */
@Log
public abstract class AbstractSQLJDBC {

    /*   connection jdbc connection and pool details from dbcp pooling */
    public DataSource jdbcPool;
    protected String url = "jdbc:dbms://host:port/dbname";
    protected String user = "user";
    protected  String password = "pwd_encrypted?";

    /*
    *   Returns result as enum resultFormat declares
    *   close stmt, return the connection to pool after execution
    *   { "column_meta_data" : { "name" : "..." , "type" : ".." , ...} ,
    *     "data_rows" : [ [ ".." , nnn , nn.mm , bool, .. ], [ ...] ]
    *    }
     */
    public List<List<Object>> executeSelect(String query, List params){
        List<List<Object>> result = new ArrayList<List<Object>>();
        Connection conn = connect();
        try{
            executeSelectWithConnection(conn, query, params, result);
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
            List row = new ArrayList();
            row.add("Error: executeSelect: " + ex.getMessage());
            result.add(row);
        }
        return result;
    }

    /*
    *   all insert, update and delete
    *   should implement a check from whitelist / blacklist set of endpoint DB or tables
     */
    public List<List<Object>> executeUpdate(String query, List params){
        List<List<Object>> resultList = new ArrayList<>();
        Connection conn = connect();
        try{
            int resultCount = executeUpdateWithConnection(conn, query, params);
            fillInResultList(resultList, resultCount);
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
            fillInError( resultList, ex.getMessage());
        }
        return resultList;
    }

    /*
     *   Fill the given result ArrayList with the header row : "ResultCount" and data_row with (int) resultCount
     */
    private void fillInResultList(List<List<Object>> result, int resultCount) {
        List<Object> updateRow = new ArrayList<>();
        //header row
        HashMap row = new HashMap<>();
        row.put(com.dbtransactions.enums.ColumnMetaData.name.name() , "ResultCount");
        updateRow.add(row);
        //data_rows
        List<Object> dataRows = new ArrayList<>();
        List<Object> data_row = new ArrayList<>(); data_row.add(resultCount);
        dataRows.add(data_row);
        //add to resultList
        result.add(updateRow);
        result.add(dataRows);
    }

    private void fillInError(List<List<Object>> result, String errorMsg) {
        List<Object> updateRow = new ArrayList<>();
        //header row
        HashMap row = new HashMap<>();
        row.put(com.dbtransactions.enums.ColumnMetaData.name.name() , "ERROR");
        updateRow.add(row);
        //data_rows
        List<Object> dataRows = new ArrayList<>();
        List<Object> data_row = new ArrayList<>(); data_row.add(errorMsg);
        dataRows.add(data_row);
        //add to resultList
        result.add(updateRow);
        result.add(dataRows);
    }

    private void executeSelectWithConnection(Connection conn, String query, List params, List<List<Object>>  resultList ) throws SQLException{
        PreparedStatement stmt = getPreparedStatement(  conn,  query,  params);
        ResultSet rs = stmt.executeQuery();
        try{
            List result = ResultSetAsListOfHashMaps.getInstance().convert(rs);
            resultList.addAll(result);
        } catch(Exception ex){
            List row = new ArrayList();
            row.add("Error: Conversion: " + ex.getMessage());
            resultList.add(row);
        }
        stmt.close();
        rs.close();
        conn.close(); //HikariCP does not "close" but cleans up and returns to the pool
    }

    private int executeUpdateWithConnection(Connection conn, String query, List params ) throws SQLException{
        PreparedStatement stmt = getPreparedStatement(  conn,  query,  params);
        int resultCount = stmt.executeUpdate();
        stmt.close();
        conn.close(); //HikariCP does not "close" but cleans up and returns to the pool
        return resultCount;
    }

    private PreparedStatement getPreparedStatement( Connection conn, String query, List params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        for( int idx=0; idx < params.size(); idx++){
            stmt.setObject(idx+1, params.get(idx));
        }
        return stmt;
    }

    protected void readDBCredentials(String dbIdentifier){
       // ReadDBPropertiesFile.getSingletonInstance();
    }

    public Connection connect() {
        try {
            return jdbcPool.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void createPooledConnection(String dbIdentifier){
        log.info("Opening database on createPooledConnection");
        HikariConfig config = new HikariConfig(dbIdentifier+".properties");
        jdbcPool = new HikariDataSource(config);
    }

    /*
     *   Connect using JNDI typically inside a container viz: Tomcat
     */
    public void connectJNDI(){
        log.info("Opening database on connectJNDI");
        Connection conn = null;
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            jdbcPool = (DataSource) ctx.lookup("java:/comp/env/jdbc/db-transactions-server");
        } catch (Exception e) {
            log.severe(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        log.info("Opened database successfully");
    }

}
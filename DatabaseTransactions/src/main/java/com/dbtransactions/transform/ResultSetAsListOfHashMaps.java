/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions.transform;

import com.dbtransactions.enums.ColumnMetaData;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log
public class ResultSetAsListOfHashMaps {

    static volatile ResultSetAsListOfHashMaps INSTANCE = new ResultSetAsListOfHashMaps();

    public static ResultSetAsListOfHashMaps getInstance(){
        return INSTANCE;
    }

    public List<List<Object>> convert(ResultSet rs){
        List<List<Object>> result= new ArrayList<>();
        int numColumns;
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
            numColumns = rsmd.getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Object> headerRow = new ArrayList<>();
        result.add(headerRow);
        getColumnHeaderRow( numColumns, rsmd, headerRow);
        getDataRows(numColumns, rsmd, rs, result);
        return result;
    }

    private String str( int num){
        return ""+num;
    }

    private void getColumnHeaderRow( int numColumns, ResultSetMetaData rsmd, ArrayList<Object> headerRow ){
        try {
            for (int col_idx = 1; col_idx < numColumns + 1; col_idx++) {
                HashMap<String, Object> colMetaData = new HashMap<>();
                colMetaData.put(ColumnMetaData.name.name(), rsmd.getColumnName(col_idx));
                colMetaData.put(ColumnMetaData.type.name(), rsmd.getColumnType(col_idx));
                colMetaData.put(ColumnMetaData.type_name.name(), rsmd.getColumnTypeName(col_idx));
                colMetaData.put(ColumnMetaData.display_size.name(), rsmd.getColumnDisplaySize(col_idx));
                colMetaData.put(ColumnMetaData.precision.name(), rsmd.getPrecision(col_idx));
                colMetaData.put(ColumnMetaData.scale.name(), rsmd.getScale(col_idx));
                headerRow.add(colMetaData);
            }
        } catch(SQLException ex){
            log.severe("Error: "+ ex.getMessage());
        }
    }

    private void getDataRows( int numColumns, ResultSetMetaData rsmd, ResultSet rs, List<List<Object>> resultList ){
        ArrayList<Object> dataRows = new ArrayList<>();
        try {
            while(rs.next()) {
                ArrayList<Object> dataRow = new ArrayList<>();
                for (int row_idx=1; row_idx<numColumns+1; row_idx++) {
                    convertRowsToMap( rsmd, rs, row_idx,  dataRow );
                }
                dataRows.add(dataRow);
            }
        } catch(SQLException ex){
            log.severe("Error: "+ ex.getMessage());
        }
        resultList.add(dataRows);
    }

    private void convertRowsToMap( ResultSetMetaData rsmd, ResultSet rs, int col_idx, ArrayList<Object> dataRow) throws SQLException {
        int columnType = rsmd.getColumnType(col_idx);

        switch( columnType ) {
            case Types.ARRAY:
                dataRow.add(rs.getArray(col_idx)); break;
            case Types.BIGINT:						//Should it be getLong ?
                BigDecimal decimal = rs.getBigDecimal(col_idx);
                BigInteger val = (decimal == null ? null : decimal.toBigInteger());
                dataRow.add(val);
                break;
            case Types.BOOLEAN:
            case Types.BIT:
                dataRow.add(rs.getBoolean(col_idx)); break;
            case Types.BLOB:
                dataRow.add(rs.getBlob(col_idx)); break;
            case Types.BINARY:
            case Types.NCHAR:
            case Types.VARBINARY:
                dataRow.add(rs.getBytes(col_idx)); break; 	 	//check.. if this holds BINARY -> getBytes..?
            case Types.CHAR:
                dataRow.add(rs.getByte(col_idx)); break;
            case Types.CLOB:
                dataRow.add(rs.getClob(col_idx)); break;
            case Types.DECIMAL:
                dataRow.add(rs.getBigDecimal(col_idx)); break; 	//convert this to the precision of the column
            case Types.DOUBLE:
                dataRow.add(rs.getDouble(col_idx)); break;
            case Types.FLOAT:
            case Types.REAL:
                dataRow.add(rs.getFloat(col_idx)); break;
            case Types.INTEGER:
            case Types.SMALLINT:
                dataRow.add(rs.getInt(col_idx)); break;
            case Types.LONGNVARCHAR:
            case Types.NVARCHAR:
                dataRow.add(rs.getNString(col_idx)); break;
            case Types.LONGVARCHAR:
            case Types.ROWID:
            case Types.VARCHAR:
                dataRow.add(rs.getString(col_idx)); break;
            case Types.NCLOB:
                dataRow.add(rs.getNClob(col_idx)); break;
            case Types.SQLXML:
                dataRow.add(rs.getSQLXML(col_idx).getString()); break; //presume this is not huge 100MB+ XMLS..!..
            case Types.DATE:
                dataRow.add(rs.getDate(col_idx)); break;
            case Types.TIMESTAMP:
                dataRow.add(rs.getTimestamp(col_idx)); break;
            default:
                dataRow.add(rs.getObject(col_idx)); break;
        }
        //Default caters for now to DATALINK, DISTINCT, NULL, REF, REF_CURSOR, TIME_WITH_TIMEZONE, TIMESTAMP_WITH_TIMEZONE,

    }
}

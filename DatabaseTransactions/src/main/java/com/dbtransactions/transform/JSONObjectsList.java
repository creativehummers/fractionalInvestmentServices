/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions.transform;

import com.dbtransactions.enums.ColumnMetaData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class JSONObjectsList implements ResultTransformerInterface<String> {


    public String transform(List<List<Object>> dbData){
        JSONObject resultSetAsJSONObj = new JSONObject();
        JSONArray columnsJSON = new JSONArray();
        List columns_metadata = (List) dbData.get(0);
        hydrateColumnsMetaData( columns_metadata, columnsJSON);
        resultSetAsJSONObj.put("columns_metadata" , columnsJSON);
        JSONArray rowsJSON = new JSONArray();
        hydrateDataRows(dbData, rowsJSON);
        resultSetAsJSONObj.put("data_rows" , rowsJSON);
        return resultSetAsJSONObj.toString();
    }

        private void hydrateColumnsMetaData(List<Object> columns_metadata, JSONArray columnsJSON){
            for( int idx=0; idx < columns_metadata.size(); idx++ ) {
                HashMap col = (HashMap) columns_metadata.get(idx);
                JSONObject column = new JSONObject();
                column.put(ColumnMetaData.name.name(),col.get(ColumnMetaData.name.name()));
                column.put(ColumnMetaData.type.name(),col.get(ColumnMetaData.type.name()));
                column.put(ColumnMetaData.type_name.name(), col.get(ColumnMetaData.type_name.name()));
                column.put(ColumnMetaData.display_size.name(), col.get(ColumnMetaData.display_size.name()));
                column.put(ColumnMetaData.precision.name(), col.get(ColumnMetaData.precision.name()));
                column.put(ColumnMetaData.scale.name(), col.get(ColumnMetaData.scale.name()));
                columnsJSON.put(column);
            }

        }

        private void hydrateDataRows(List<List<Object>> dbData, JSONArray rowsJSON){
            for( int idx=1; idx < dbData.size(); idx++ ) {
                List<Object> data_rows = dbData.get(idx);
                for( int row_idx=0; row_idx < data_rows.size(); row_idx++ ) {
                    JSONArray data_row = new JSONArray();
                    List<Object> each_row = (List)data_rows.get(row_idx);
                    for( int col_idx=0; col_idx < each_row.size(); col_idx++ ) {
                        data_row.put( each_row.get(col_idx));
                    }
                    rowsJSON.put(data_row);
                }

            }
        }

}

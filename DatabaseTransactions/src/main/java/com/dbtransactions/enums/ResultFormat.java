package com.dbtransactions.enums;

public enum ResultFormat {
        ListOfHashMaps,             //Default format
        CSVData,                    //CSV format with header and rows
        JSONColumnDataRows,         //JSON with first row of col_meta_data, data_rows
        JSONObjectsList,            //JSON of colname:value pairs as data_rows
        DBNativeJSON,                //PostGre, DBMS native json response
        RAWFormat                    //Raw format as provided by SQL call ..
    }



/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package com.dbtransactions.transform;

import java.util.List;

public class CSVData implements ResultTransformerInterface<String> {


    public String transform(List<List<Object>> dbData){

        return "FAILED CSV STRING";
    }

}

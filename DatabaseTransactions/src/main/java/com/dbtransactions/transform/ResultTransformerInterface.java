package com.dbtransactions.transform;

import java.util.List;

public interface ResultTransformerInterface<T> {

    public  T transform(List<List< Object>> dbData);
}

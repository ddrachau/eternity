package com.prodyna.pac.eternity.server.common;

import java.util.HashMap;
import java.util.Map;

public abstract class QueryUtils {

    public static Map<Integer, Object> map(Object... objects) {

        Map result = new HashMap();

        int i = 0;

        while (i < objects.length) {
            result.put(objects[i++], objects[i++]);
        }

        return result;
    }

}

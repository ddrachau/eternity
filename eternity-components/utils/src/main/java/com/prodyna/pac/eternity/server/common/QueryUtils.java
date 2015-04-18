package com.prodyna.pac.eternity.server.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class with some basic query utility methods to reduce redundancy and unnecessary inheritances.
 */
public abstract class QueryUtils {

    /**
     * Creates a map of the given objects.
     *
     * @param objects the content of the new map, length has to be % 2 = 0, the 1st, 3rd... element needs to be an int
     * @return the new created map
     */
    public static Map<Integer, Object> map(Object... objects) {

        Map<Integer, Object> result = new HashMap();

        int i = 0;

        while (i < objects.length) {
            result.put((Integer) objects[i++], objects[i++]);
        }

        return result;

    }

}

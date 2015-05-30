package com.prodyna.pac.eternity.common.helper;

import java.util.Map;

/**
 * Helper class with some basic query utility methods to reduce redundancy and unnecessary inheritances.
 */
public interface QueryMapBuilder {

    /**
     * Creates a map of the given objects.
     *
     * @param objects the content of the new map, length has to be % 2 = 0, the 1st, 3rd... element needs to be an int
     * @return the new created map
     */
    Map<Integer, Object> map(Object... objects);

}

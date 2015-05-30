package com.prodyna.pac.eternity.common.helper.impl;

import com.prodyna.pac.eternity.common.helper.QueryMapBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation for a QueryMapBuilder
 */
public class QueryMapBuilderImpl implements QueryMapBuilder {

    @Override
    public Map<Integer, Object> map(final Object... objects) {

        Map<Integer, Object> result = new HashMap();

        int i = 0;

        while (i < objects.length) {
            result.put((Integer) objects[i++], objects[i++]);
        }

        return result;

    }

}

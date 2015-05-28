package com.prodyna.pac.eternity.test.server.model;

import com.prodyna.pac.eternity.server.model.FilterRequest;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests for the filter request.
 */
public class FilterRequestTest {

    @Test
    public void testSetTotal() {

        FilterRequest request = new FilterRequest(null, null, 25, 15);

        Assert.assertEquals(25, request.getStart());
        Assert.assertEquals(15, request.getPageSize());

        request.setTotalSize(26);

        Assert.assertEquals(25, request.getStart());
        Assert.assertEquals(15, request.getPageSize());

        request.setTotalSize(25);

        Assert.assertEquals(15, request.getStart());
        Assert.assertEquals(15, request.getPageSize());

        request.setTotalSize(14);

        Assert.assertEquals(0, request.getStart());
        Assert.assertEquals(15, request.getPageSize());

        request = new FilterRequest(null, null, 25, 0);

        Assert.assertEquals(25, request.getStart());
        Assert.assertEquals(0, request.getPageSize());

        request.setTotalSize(30);

        Assert.assertEquals(25, request.getStart());
        Assert.assertEquals(0, request.getPageSize());

        request.setTotalSize(20);

        Assert.assertEquals(0, request.getStart());
        Assert.assertEquals(0, request.getPageSize());

    }


}

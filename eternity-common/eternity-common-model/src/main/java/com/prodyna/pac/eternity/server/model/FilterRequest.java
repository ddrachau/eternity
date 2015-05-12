package com.prodyna.pac.eternity.server.model;

import java.util.Arrays;

public class FilterRequest {

    private String sort;
    private String[] filter;
    private int start;
    private int pageSize;

    public FilterRequest(final String sort, final String[] filter, final int start, final int pageSize) {
        this.sort = sort;
        this.filter = filter;
        this.start = start;
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public String[] getFilter() {
        return filter;
    }

    public int getStart() {
        return start;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "FilterRequest{" +
                "sort='" + sort + '\'' +
                ", filter=" + Arrays.toString(filter) +
                ", start=" + start +
                ", pageSize=" + pageSize +
                '}';
    }

}

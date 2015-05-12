package com.prodyna.pac.eternity.server.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FilterRequest {

    private String sortString;

    private String filterString;

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

    public String getSortString() {

        return sortString;
    }

    public void setSortString(String sortString) {

        this.sortString = sortString;
    }

    public String getFilterString() {

        return filterString;
    }

    public void setFilterString(String filterString) {

        this.filterString = filterString;
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

    public boolean hasValidSortFilter() {

        return this.getSort() != null && this.getSort().length() > 1 &&
                (this.getSort().charAt(0) == '+' || this.getSort().charAt(0) == '-');

    }

    public boolean isSortDescending() {

        return this.hasValidSortFilter() && this.getSort().charAt(0) == '-';

    }

    public boolean hasValidFilter() {

        boolean validFilter = this.getFilter() != null && this.getFilter().length > 0;

        if (validFilter) {

            for (String f : this.getFilter()) {

                validFilter = validFilter && f != null && f.length() > 2 && f.split(":").length == 2;

            }

        }

        return validFilter;
    }

    public Map<String, String> getFilterMap() {

        HashMap<String, String> result = new HashMap<>();

        for (String f : this.getFilter()) {

            String[] parts = f.split(":");

            result.put(parts[0], parts[1]);

        }

        return result;

    }

    @Override
    public String toString() {

        return "FilterRequest{" +
                "sortString='" + sortString + '\'' +
                ", filterString='" + filterString + '\'' +
                ", sort='" + sort + '\'' +
                ", filter=" + Arrays.toString(filter) +
                ", start=" + start +
                ", pageSize=" + pageSize +
                '}';
    }

}

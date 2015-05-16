package com.prodyna.pac.eternity.server.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FilterRequest {

    private String sort;

    private String[] filter;

    private int start;

    private int pageSize;

    private Map<String, String> mappings = new HashMap<>();

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

    public Map<String, String> getMappings() {

        return mappings;
    }

    public void setMappings(final Map<String, String> mappings) {

        this.mappings = mappings;
    }

    public String getSortString() {

        String sortString = "";

        if (this.hasValidSortFilter()) {

            if (this.mappings.containsKey(this.getSort().substring(1))) {
                sortString = "ORDER BY " + this.mappings.get(this.getSort().substring(1)) +
                        (this.isSortDescending() ? " DESC" : " ");
            } else {
                throw new RuntimeException("unknown sort filter: " + this.getSort().substring(1));
            }

        }

        return sortString;

    }

    public String getFilterString() {

        String filterString = "";

        if (this.hasValidFilter()) {

            filterString = "WHERE ";

            int filterCounter = 0;

            Map<String, String> filterMap = this.getFilterMap();

            for (Map.Entry<String, String> pair : filterMap.entrySet()) {

                filterCounter++;

                if (this.mappings.containsKey(pair.getKey())) {
                    filterString += this.mappings.get(pair.getKey()) + "=~'" + pair.getValue() + "' ";
                } else {
                    throw new RuntimeException("unknown filter: " + pair.getKey());
                }

                if (filterCounter < filterMap.size()) {
                    filterString += "AND ";
                }

            }

        }

        return filterString;

    }

    private boolean hasValidSortFilter() {

        return this.getSort() != null && this.getSort().length() > 1 &&
                (this.getSort().charAt(0) == '+' || this.getSort().charAt(0) == '-');

    }

    private boolean isSortDescending() {

        return this.hasValidSortFilter() && this.getSort().charAt(0) == '-';

    }

    private boolean hasValidFilter() {

        boolean validFilter = this.getFilter() != null && this.getFilter().length > 0;

        if (validFilter) {

            for (String f : this.getFilter()) {

                validFilter = validFilter && f != null && f.length() > 2 && f.split(":").length == 2;

            }

        }

        return validFilter;
    }

    private Map<String, String> getFilterMap() {

        HashMap<String, String> result = new HashMap<>();

        for (String f : this.getFilter()) {
            String[] parts = f.split(":");
            String value = parts[1].replace("\\", "\\\\");
            value = "(?i).*\\\\Q" + value + "\\\\E.*";
            result.put(parts[0], value);
        }

        return result;

    }

    @Override
    public String toString() {

        return "FilterRequest{" +
                ", sort='" + sort + '\'' +
                ", filter=" + Arrays.toString(filter) +
                ", start=" + start +
                ", pageSize=" + pageSize +
                '}';
    }

}

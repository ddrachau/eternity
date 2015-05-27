package com.prodyna.pac.eternity.server.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A filter request is a set of filter criteria for a search.
 */
public class FilterRequest {

    /**
     * optional search string for a property, starting with + ascending or - descending
     */
    private String sort;

    /**
     * an array of filters for the search 'property:value'.
     */
    private String[] filter;

    /**
     * the start offset for the search if pagination is used
     */
    private int start;

    /**
     * the page size for the result, 0 for unlimited
     */
    private int pageSize;

    /**
     * a mapping list for the internal to external properties
     */
    private Map<String, String> mappings = new HashMap<>();

    /**
     * Default constructor for a filter request
     *
     * @param sort     the optional sort string
     * @param filter   optional filters
     * @param start    pagination offset
     * @param pageSize the result set max size
     */
    public FilterRequest(final String sort, final String[] filter, final int start, final int pageSize) {

        this.sort = sort;
        this.filter = filter.clone();
        this.setStart(start);
        this.setPageSize(pageSize);

    }

    /**
     * Basic Getter
     *
     * @return the sort
     */
    public String getSort() {

        return sort;

    }

    /**
     * Basic Getter
     *
     * @return the filter
     */
    public String[] getFilter() {

        if (filter != null) {
            return filter.clone();
        } else {
            return new String[0];
        }

    }

    /**
     * Basic Getter
     *
     * @return the start
     */
    public int getStart() {

        return start;

    }

    /**
     * Setter for the start
     *
     * @param start &gt;= 0
     */
    private void setStart(final int start) {

        if (start < 0) {
            this.start = 0;
        } else {
            this.start = start;
        }

    }

    /**
     * Basic Getter
     *
     * @return the pageSize
     */
    public int getPageSize() {

        return pageSize;

    }

    /**
     * Setter for the page size
     *
     * @param pageSize &gt;=0
     */
    private void setPageSize(final int pageSize) {

        if (pageSize < 0) {
            this.pageSize = 0;
        } else {
            this.pageSize = pageSize;
        }

    }

    /**
     * Basic setter
     *
     * @param mappings the mappings between in and external properties
     */
    public void setMappings(final Map<String, String> mappings) {

        this.mappings = mappings;
    }

    /**
     * Creates a sort string
     *
     * @return the sort string derived form the sort property
     */
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

    /**
     * Creates a filter string
     *
     * @return the filter string derived from the filters property
     */
    public String getFilterString() {

        StringBuilder buffer = new StringBuilder();

        if (this.hasValidFilter()) {

            buffer.append("WHERE ");

            int filterCounter = 0;

            Map<String, String> filterMap = this.getFilterMap();

            for (Map.Entry<String, String> pair : filterMap.entrySet()) {

                filterCounter++;

                if (this.mappings.containsKey(pair.getKey())) {
                    buffer.append(this.mappings.get(pair.getKey()));
                    buffer.append("=~'");
                    buffer.append(pair.getValue());
                    buffer.append("' ");
                } else {
                    throw new RuntimeException("unknown filter: " + pair.getKey());
                }

                if (filterCounter < filterMap.size()) {
                    buffer.append("AND ");
                }
            }
        }

        return buffer.toString();

    }

    /**
     * Check if the sort property is in a valid format
     *
     * @return true if valid, false otherwise
     */
    private boolean hasValidSortFilter() {

        return this.getSort() != null && this.getSort().length() > 1 &&
                (this.getSort().charAt(0) == '+' || this.getSort().charAt(0) == '-');

    }


    /**
     * Check if the sort property is descending
     *
     * @return true if descending, false otherwise
     */
    private boolean isSortDescending() {

        return this.hasValidSortFilter() && this.getSort().charAt(0) == '-';

    }


    /**
     * Check if the filters property is in a valid format
     *
     * @return true if valid, false otherwise
     */
    private boolean hasValidFilter() {

        boolean validFilter = this.getFilter().length > 0;

        if (validFilter) {

            for (String f : this.getFilter()) {

                validFilter = validFilter && f != null && f.length() > 2 && f.split(":").length == 2;

            }

        }

        return validFilter;
    }

    /**
     * Creates a filter map form the filters property
     *
     * @return the created map
     */
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

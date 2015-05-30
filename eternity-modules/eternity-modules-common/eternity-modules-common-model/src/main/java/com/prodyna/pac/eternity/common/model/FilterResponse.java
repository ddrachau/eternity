package com.prodyna.pac.eternity.common.model;

import java.util.List;

/**
 * A filter response is a return wrapper for a search with a filter request.
 *
 * @param <T> the type this response wraps
 */
public class FilterResponse<T> {

    /**
     * the result data
     */
    private List<T> data;

    /**
     * the page size used for this response
     */
    private int pageSize;

    /**
     * the offset used for this response
     */
    private int offset;

    /**
     * the totalSize of available data entries
     */
    private int totalSize;

    /**
     * Empty default constructor
     */
    public FilterResponse() {

    }

    /**
     * Creates a filter response and initialize the following properties:
     *
     * @param data      the result data
     * @param pageSize  the used page size
     * @param offset    the used search offset
     * @param totalSize the total size of data entries available
     */
    public FilterResponse(final List<T> data, final int pageSize, final int offset, final int totalSize) {

        this.data = data;
        this.pageSize = pageSize;
        this.offset = offset;
        this.totalSize = totalSize;
    }

    /**
     * Basic Getter
     *
     * @return the data
     */
    public List<T> getData() {

        return this.data;
    }

    /**
     * Basic Setter
     *
     * @param data the result data
     */
    public void setData(final List<T> data) {

        this.data = data;
    }

    /**
     * Basic Getter
     *
     * @return the pageSize
     */
    public int getPageSize() {

        return this.pageSize;
    }

    /**
     * Basic Setter
     *
     * @param pageSize the pageSize used for the search
     */
    public void setPageSize(final int pageSize) {

        this.pageSize = pageSize;
    }

    /**
     * Basic Getter
     *
     * @return the offset
     */
    public int getOffset() {

        return this.offset;
    }

    /**
     * Basic Setter
     *
     * @param offset the offset used for the search
     */
    public void setOffset(final int offset) {

        this.offset = offset;
    }

    /**
     * Basic Getter
     *
     * @return the totalSize
     */
    public int getTotalSize() {

        return this.totalSize;
    }

    /**
     * Basic Setter
     *
     * @param totalSize the totalSize of available data entries
     */
    public void setTotalSize(final int totalSize) {

        this.totalSize = totalSize;
    }

    /**
     * Getter for the number of pages
     *
     * @return the number of pages, but at least 1
     */
    public int getNumberOfPages() {

        if (this.getPageSize() <= 0) {
            return 1;
        } else {
            return (this.getTotalSize() + this.getPageSize() - 1) / this.getPageSize();
        }

    }

}

package com.prodyna.pac.eternity.server.model;

import java.util.List;

public class FilterResponse<T> {

    private List<T> data;

    private int pageSize;

    private int offset;

    private int totalSize;

    public FilterResponse() {

    }

    public FilterResponse(final List<T> data, final int pageSize, final int offset,
                          final int totalSize) {

        this.data = data;
        this.pageSize = pageSize;
        this.offset = offset;
        this.totalSize = totalSize;
    }

    public List<T> getData() {

        return this.data;
    }

    public void setData(final List<T> data) {

        this.data = data;
    }

    public int getPageSize() {

        return this.pageSize;
    }

    public void setPageSize(final int pageSize) {

        this.pageSize = pageSize;
    }

    public int getOffset() {

        return this.offset;
    }

    public void setOffset(final int offset) {

        this.offset = offset;
    }

    public int getTotalSize() {

        return totalSize;
    }

    public void setTotalSize(final int totalSize) {

        this.totalSize = totalSize;
    }

    public int getNumberOfPages() {

        if (this.getPageSize() == 0) {
            return 1;
        } else {
            return (this.getTotalSize() + this.getPageSize() - 1) / this.getPageSize();
        }

    }

}

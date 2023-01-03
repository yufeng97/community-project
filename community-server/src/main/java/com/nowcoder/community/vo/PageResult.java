package com.nowcoder.community.vo;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> {
    private static final long serialVersionUID = 8163854998178877689L;
    private long totalCount;
    private int pageSize;
    private int curPage;
    private List<T> list;

//    private boolean hasMore;

    public PageResult() {
    }

    public PageResult(List<T> list, long totalCount, int pageSize, int curPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.curPage = curPage;
    }

    public PageResult(List<T> list, long totalCount, int pageSize) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
    }

    public PageResult(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<T> getList() {
        if (this.list == null || this.list.isEmpty()) {
            this.list = new ArrayList(0);
        }

        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isHasMore() {
        System.out.println("this.totalCount " + this.totalCount + " curPage: " + this.curPage);
        return this.totalCount - (this.curPage + 1) * this.pageSize > 0;
    }

//    public void setHasMore(boolean hasMore) {
//        this.hasMore = hasMore;
//    }

}

package com.nowcoder.community.vo;

import java.util.ArrayList;
import java.util.List;

public class ScrollResult<T> {
    private static final long serialVersionUID = 816385499817874589L;
    private long totalCount;
    private List<T> list;

    private boolean hasMore;

    public ScrollResult() {
    }

    public ScrollResult(List<T> list, long totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
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
        return this.list.size() < this.totalCount;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}

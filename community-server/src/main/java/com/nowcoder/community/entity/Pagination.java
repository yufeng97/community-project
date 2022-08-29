package com.nowcoder.community.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pagination {
    private Integer pageNum = 1;
    private Integer pageSize = 7;
    private boolean countSql = true;
    private String orderBy;
}

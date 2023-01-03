package com.nowcoder.community.util;

import lombok.Data;

import java.util.List;

@Data
public class DataPage<T> {
    private long total;
    private List<T> rows;
}

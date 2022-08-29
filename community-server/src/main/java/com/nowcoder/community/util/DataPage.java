package com.nowcoder.community.util;

import lombok.Data;

import java.util.List;

@Data
public class DataPage {
    private long total;
    private List<?> rows;
}

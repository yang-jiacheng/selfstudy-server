package com.lxy.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollResult<T> {

    /**
     * 数据集合
     */
    private List<T> records;

}

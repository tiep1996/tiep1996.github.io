package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

//AnhTT_IIST
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataPage<T> implements Serializable {
    private int pageIndex;
    private int pageSize;
    private long pageCount;
    private long dataCount;
    private List<T> data;
}

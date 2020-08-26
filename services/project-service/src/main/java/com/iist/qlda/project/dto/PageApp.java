package com.iist.qlda.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @author dangnp
 * @created 29/07/2020 - 12:46 PM
 * @project services
 **/
@Data
public class PageApp<T> {
    private int pageIndex;
    private long totalItems;
    private int itemsPerPage;
    @JsonIgnore
    private String sort;
    private List<T> data;

    public PageApp(int pageIndex, int itemsPerPage, String sort) {
        this.pageIndex = pageIndex;
        this.itemsPerPage = itemsPerPage;
        this.sort = sort;
    }

    public String getSortBy() {
        return sort.split(",")[0];
    }

    public boolean isASC() {
        String data = sort.split(",")[1];
        if (data.equals("asc")) {
            return true;
        }
        return false;
    }
}

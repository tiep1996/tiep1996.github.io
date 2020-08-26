package com.iist.qlda.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author dangnp
 * @created 01/08/2020 - 9:48 AM
 * @project services
 **/
@Data

public class TreeDTO {
    private String text;
    private Long value;
    private Boolean checked;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TreeDTO> children;
}

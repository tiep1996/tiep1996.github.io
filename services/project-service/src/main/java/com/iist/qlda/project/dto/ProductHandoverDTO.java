package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHandoverDTO {
    private Long productHandoverId;

    private Long projectId;

    private String productName;

    private Integer statusHandover;

    private String description;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private int stt;

    private String statusHandoverStr;

}

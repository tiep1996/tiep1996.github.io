package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachDocumentDTO {
    private Long attachDocumentId;

    private String code;

    private String name;

    private String path;

    private Integer type;

    private double size;

    private Long parentId;

    private String createDate;

    private Long createBy;

    private String updateDate;

    private Long updateBy;

    private Integer isActive;
}

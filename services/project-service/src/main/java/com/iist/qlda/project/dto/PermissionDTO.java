package com.iist.qlda.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private Long permissionId;

    private String code;

    private String name;

    private Integer level;

    private Long parentId;

    private String description;

    private String pathId;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private Integer isActive;

    private Boolean hasChild;

    private List<PermissionDTO> permissionChilds;

}

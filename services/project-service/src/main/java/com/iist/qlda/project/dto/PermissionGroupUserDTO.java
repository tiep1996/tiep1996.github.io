package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupUserDTO {
    private Long id;

    private Long groupUserId;

    private Long permissionId;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private Integer isActive;


}

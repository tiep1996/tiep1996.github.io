package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumanResourcesDTO {

    private Long humanResourceId;

    private String code;

    private String lastName;

    private String firstName;

    private String email;

    private String phone;

    private Date startDate;

    private Date endDate;

    private Long departmentId;

    private Long positionId;

    private String description;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private Integer isActive;

    private String username;

    private String password;

    private Long centerId;

    private Integer isNew;

    private Double resourcesUsed;

    private List<String> lstPermission;

    private String newPassword;

    private String newPasswordConfirm;

    private String role;
}

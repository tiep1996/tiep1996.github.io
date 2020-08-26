package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDTO {
    private Long projectMemberId;

    private Long humanResourceId;

    private Long projectId;

    private String role;

    private String department;

    private Date dateJoin;

    private Date dateOut;

    private Integer resources;

    private Double resourcesUsed;

    private Integer isActive;

    private Integer noJoin;

    private Double KPI;

    private String username;

    private String firstName;

    private Integer page;

    private Integer pageSize;

    private Long totalRecord;

    private String type;
}

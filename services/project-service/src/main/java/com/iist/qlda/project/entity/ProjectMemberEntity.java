package com.iist.qlda.project.entity;

import javax.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name ="PROJECT_MEMBER")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_MEMBER_ID")
    private Long projectMemberId;

    @Column(name = "HUMAN_RESOURCE_ID")
    private Long humanResourceId;

    @Column(name = "PROJECT_ID")
    private Long projectId;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "DATE_JOIN")
    private Date dateJoin;

    @Column(name = "DATE_OUT")
    private Date dateOut;

    @Column(name = "RESOURCES")
    private Integer resources;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;

    @Column(name = "NO_JOIN")
    private Integer noJoin;

    @Column(name = "KPI")
    private Double KPI;

}

package com.iist.qlda.project.entity;

import javax.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name ="PERMISSION_GROUP_USER")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "GROUP_USER_ID")
    private Long groupUserId;

    @Column(name = "PERMISSION_ID")
    private Long permissionId;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;


}

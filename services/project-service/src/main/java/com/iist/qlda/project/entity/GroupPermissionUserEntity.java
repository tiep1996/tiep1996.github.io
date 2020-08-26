package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author dangnp
 * @created 25/07/2020 - 10:18 AM
 * @project services
 **/
@Data
@Entity
@Table(name = "GROUPPERMISSION_USER")
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermissionUserEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "GROUP_PERMISSION_ID")
    private Long groupPermissionId;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "CREATE_DATE")
    private Long createDate;
    @Column(name = "CREATE_BY")
    private Long createBy;
    @Column(name = "UPDATE_DATE")
    private Long updateDate;
    @Column(name = "UPDATE_BY")
    private Long updateBy;

    public GroupPermissionUserEntity(Long groupPermissionId, Long userId) {
        this.groupPermissionId = groupPermissionId;
        this.userId = userId;
    }
}

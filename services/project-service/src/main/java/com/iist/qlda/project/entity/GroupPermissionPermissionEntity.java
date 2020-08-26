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
@Table(name = "GROUPPERMISSION_PERMISSION")
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermissionPermissionEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "GROUP_PERMISSION_ID")
    private Long groupPermissionId;
    @Column(name = "PERMISSION_ID")
    private Long permissionId;

    public GroupPermissionPermissionEntity(Long groupPermissionId, Long permissionId) {
        this.groupPermissionId = groupPermissionId;
        this.permissionId = permissionId;
    }
}

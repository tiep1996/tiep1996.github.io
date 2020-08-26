package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.GroupPermissionDTO;
import com.iist.qlda.project.dto.PermissionDTO;
import com.iist.qlda.project.dto.TreeDTO;

import java.util.List;

public interface PermissionService {
    List<PermissionDTO> findAllPermissionIsActive();

    List<GroupPermissionDTO> findAllPermissionByGoupPermission();

    public List<TreeDTO> getTreePermission(Long groupPermissionId);

}

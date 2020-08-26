package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.*;
import com.iist.qlda.project.entity.GroupPermissionUserEntity;

import java.util.List;

/**
 * @author dangnp
 * @created 25/07/2020 - 9:40 AM
 * @project services
 **/
public interface GroupPermissionService {
    GroupPermissionDTO create(GroupPermissionDTO dto);

    GroupPermissionDTO update(GroupPermissionDTO dto, Long id);

    GroupPermissionDTO delete(Long id);

    void addUser(Long groupId, Long userId);

    void addUserPermission(List<Long> lstId);

    GroupPermissionDTO findById(Long id);

    PageApp<GroupPermissionDTO> findAll(PageApp page);

    PageApp<GroupPermissionDTO> searchAllByUnit(String code, String name, String status, PageApp page);

    List<GroupPermissionDTO> findAllByStatus(String status);

    List<IHumanResourcesShowDTO> getlistHumanResourcesDepatment(String name);

    GroupPermissionDTO checkedGroupPermission(Long id);

    List<String> getChilPermission(String code);
    
    void deleteUserPermission(List<Long> lstId);
    
	List<HumanResourcesShowDTO> viewUserPermission(Long grId);

    GroupPermissionUserEntity isUserAdded(Long uId);
}

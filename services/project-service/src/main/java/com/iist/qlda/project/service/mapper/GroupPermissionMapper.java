package com.iist.qlda.project.service.mapper;

import com.iist.qlda.project.dto.GroupPermissionDTO;
import com.iist.qlda.project.entity.GroupPermissionEntity;
import org.mapstruct.Mapper;

/**
 * @author dangnp
 * @created 24/07/2020 - 3:29 PM
 * @project services
 **/

@Mapper(componentModel = "spring")
public interface GroupPermissionMapper extends EntityMapper<GroupPermissionDTO, GroupPermissionEntity>{
}

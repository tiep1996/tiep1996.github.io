package com.iist.qlda.project.service.mapper;

import com.iist.qlda.project.dto.PermissionDTO;
import com.iist.qlda.project.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, PermissionEntity> {
}

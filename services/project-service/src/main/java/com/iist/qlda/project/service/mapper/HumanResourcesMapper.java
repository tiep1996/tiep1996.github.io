package com.iist.qlda.project.service.mapper;

import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HumanResourcesMapper extends EntityMapper<HumanResourcesDTO, HumanResourcesEntity> {

}

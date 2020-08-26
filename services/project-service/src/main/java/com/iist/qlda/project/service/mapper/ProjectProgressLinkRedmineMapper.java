package com.iist.qlda.project.service.mapper;

import com.iist.qlda.project.dto.ProjectProgressLinkRedmineDto;
import com.iist.qlda.project.entity.ProjectProgressLinkRedmine;
import org.mapstruct.Mapper;

/**
 * @author nuctv
 * @since 10 ,7/25/2020 , 2020
 */
@Mapper(componentModel = "spring")
public interface ProjectProgressLinkRedmineMapper extends EntityMapper<ProjectProgressLinkRedmineDto, ProjectProgressLinkRedmine>{
}

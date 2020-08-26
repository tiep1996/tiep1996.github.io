package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.ProjectProgressLinkRedmineDto;

import java.util.List;
import java.util.Map;

/**
 * @author nuctv
 * @since 11 ,7/24/2020 , 2020
 */
public interface ProjectProgressService {
    ProjectProgressLinkRedmineDto saveLinkRedmine(ProjectProgressLinkRedmineDto projectProgressLinkRedmineDto);
    List<String> synchronizedWithRedmine();
    Map<String,Object> getLInkRedmineByProjectID(Long id);
    List<String> checkLinkExist(ProjectProgressLinkRedmineDto dto);
}

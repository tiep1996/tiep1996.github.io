package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.DataPage;
import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import com.iist.qlda.project.entity.ProjectMemberEntity;

import java.util.List;

public interface ProjectMemberService {
    ProjectDTO saveProjectMember(ProjectDTO projectDTO);

    DataPage<ProjectMemberDTO> getListMemberProject(ProjectDTO dto);

//    nuctv 10/08
    List<ProjectMemberEntity> findByProjectIDAndHumanID(Long projectID,Long humanID);
}

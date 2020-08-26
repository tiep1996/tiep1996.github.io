package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.DataPage;
import com.iist.qlda.project.dto.ProjectDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ProjectService {
    ProjectDTO saveProject(ProjectDTO dto, MultipartFile[] files);

    ProjectDTO editProject(ProjectDTO dto, MultipartFile[] files);

    ProjectDTO saveEstimate(ProjectDTO dto);

    Long deleteProject(Long projectId);

    DataPage<ProjectDTO> listProject(ProjectDTO dto);

    boolean checkCodeExist(String code);


    void save(MultipartFile file) throws IOException;

    public Resource load(String filename);

    public Stream<Path> loadAll();

    public void init();

    public ProjectDTO countProjectMember(String type, Long projectId,String role);

}

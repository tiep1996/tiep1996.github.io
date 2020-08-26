package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.AttachDocumentEntity;
import com.iist.qlda.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    ProjectEntity findByProjectId(Long projectId);

    ProjectEntity findNameByProjectId(Long projectId);

    List<ProjectEntity> findByCodeAndIsActive(String code, Integer isActive);

}

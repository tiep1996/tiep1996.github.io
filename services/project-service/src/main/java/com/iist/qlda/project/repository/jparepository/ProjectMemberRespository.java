package com.iist.qlda.project.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iist.qlda.project.entity.ProjectMemberEntity;

import java.util.List;

@Repository
public interface ProjectMemberRespository extends JpaRepository<ProjectMemberEntity, Long>{

    List<ProjectMemberEntity> findAllByProjectId(Long id);
}

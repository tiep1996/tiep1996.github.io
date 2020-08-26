package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberJpaRepository extends JpaRepository<ProjectMemberEntity, Long> {
}

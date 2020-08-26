package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.ProjectProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author nuctv
 * @since 12 ,7/24/2020 , 2020
 */
public interface ProjectProgressRepository extends JpaRepository<ProjectProgressEntity,Long> {
    ProjectProgressEntity findByProjectId(Long id);

    @Query(value="select pp from ProjectProgressEntity pp join ProjectEntity p on pp.projectId=p.projectId and p.isActive= 1")
    List<ProjectProgressEntity> getAllWithProjectActive();
}

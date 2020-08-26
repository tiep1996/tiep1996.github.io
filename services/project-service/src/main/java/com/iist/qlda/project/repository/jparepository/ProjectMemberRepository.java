package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, Long> {
    ProjectMemberEntity findAllByHumanResourceId(long i);

//    int findByProjectId();
//
//    String findByRole();


    ProjectMemberEntity findByProjectMemberId(long i);

    //nuctv 10/08
    List<ProjectMemberEntity> findByProjectIdAndHumanResourceId(Long projectID, Long humanID);
}

package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.ProjectProgressLinkRedmine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nuctv
 * @since 10 ,7/25/2020 , 2020
 */
@Repository
public interface ProjectProgressLinkRedmineRepository extends JpaRepository<ProjectProgressLinkRedmine,Long> {
    ProjectProgressLinkRedmine findByProjectID(Long id);
}

package com.iist.qlda.project.repository.jparepository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iist.qlda.project.entity.MasterPlanEntity;
import com.iist.qlda.project.entity.ProjectPlanEntity;

import java.util.List;

@Repository
public interface MasterPlanRepository extends JpaRepository<MasterPlanEntity, Long> {
	MasterPlanEntity findAllByProjectId(long id);

	List<MasterPlanEntity> findByProjectId(Long projectId);

	//dangnp
	@Query(value="Select mp from MasterPlanEntity mp where mp.projectId=?1")
	MasterPlanEntity getByProjectId(Long proID);
}

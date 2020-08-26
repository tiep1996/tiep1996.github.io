package com.iist.qlda.project.dao;

import com.iist.qlda.project.entity.HumanResourcesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumanResouceDao extends JpaRepository<HumanResourcesEntity,Long> {

}

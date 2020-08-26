package com.iist.qlda.project.repository;

import com.iist.qlda.project.entity.AppParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppParamRepository extends JpaRepository<AppParamEntity, Long> {
    AppParamEntity findByName(String name);
}

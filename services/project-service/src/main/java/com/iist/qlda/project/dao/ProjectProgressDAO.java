package com.iist.qlda.project.dao;

import com.iist.qlda.project.entity.ProjectProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ProjectProgressDAO extends JpaRepository<ProjectProgressEntity,Long> {
}

package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    @Query(value = "SELECT * FROM PERMISSION p WHERE p.IS_ACTIVE = 1", nativeQuery = true)
    List<PermissionEntity> findAllIsActive();

    @Query(value = "SELECT * FROM PERMISSION p WHERE p.PARENT_ID = ?1 AND p.IS_ACTIVE = 1", nativeQuery = true)
    List<PermissionEntity> findByParentIdIsActive(Long id);

    List<PermissionEntity> findAllByLevel(int level);
}

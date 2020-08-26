package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.GroupPermissionPermissionEntity;
import com.iist.qlda.project.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dangnp
 * @created 25/07/2020 - 10:29 AM
 * @project services
 **/
@Repository
public interface GroupPermissionPermissionRepository extends JpaRepository<GroupPermissionPermissionEntity, Long> {

    @Query("SELECT p FROM PermissionEntity p INNER JOIN GroupPermissionPermissionEntity gpp ON p.permissionId = gpp.permissionId WHERE gpp.groupPermissionId = ?1")
    List<PermissionEntity> findByGroupPermissionId(Long id);

    void deleteByGroupPermissionId(Long id);

    GroupPermissionPermissionEntity findByGroupPermissionIdAndPermissionId(Long grouPermissionId, Long permissionId);
}

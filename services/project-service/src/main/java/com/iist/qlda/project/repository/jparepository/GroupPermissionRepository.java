package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.GroupPermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dangnp
 * @created 24/07/2020 - 3:32 PM
 * @project services
 **/
@Repository
public interface GroupPermissionRepository extends JpaRepository<GroupPermissionEntity, Long> {
    @Query(value = "SELECT * FROM GROUPPERMISSION GP WHERE GP.CODE = ?1 AND GP.STATE = 1", nativeQuery = true)
    GroupPermissionEntity findIsActiveByCode(String code);

    @Query(value = "SELECT p.CODE FROM PERMISSION p WHERE p.PARENT_ID = (SELECT P.PERMISSION_ID FROM PERMISSION P WHERE P.CODE = ?1)", nativeQuery = true)
    List<String> getCodePeermission(String code);

    @Query("SELECT p FROM GroupPermissionEntity p WHERE (?1 is null or p.code = ?1) AND (?2 is null or p.name LIKE CONCAT('%',?2,'%')) AND (?3 is null or p.status = ?3 ) AND p.state = 1")
    Page<GroupPermissionEntity> searchAllByUnit(String code, String name, String status, Pageable pageable);


}

package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.DepartmentEntity;
import common.Constants;
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
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    @Query(value = "SELECT * FROM DEPARTMENT d WHERE d.STATUS = ?1", nativeQuery = true)
    List<DepartmentEntity> findAllByStatus(String status);

}

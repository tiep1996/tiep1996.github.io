package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.dto.ICentersDTO;
import com.iist.qlda.project.entity.CentersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentersRepository extends JpaRepository<CentersEntity,Long> {

    String sql="SELECT ct.ID as id ,ct.CODE as code,ct.NAME as name from CENTERS as ct ORDER BY ct.CREATE_DATE DESC";
    @Query(value = sql, nativeQuery = true, countQuery = sql)
    List<ICentersDTO> getCenter();
}

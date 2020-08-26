package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.AppParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApparamJpaRepository extends JpaRepository<AppParamEntity, Long> {
    AppParamEntity findByType(String parType);

    @Query(value="SELECT *  FROM APP_PARAMS U WHERE U.PAR_TYPE = 'POSITION'", nativeQuery = true)
    List<AppParamEntity> findAllCodeByPosition();
}

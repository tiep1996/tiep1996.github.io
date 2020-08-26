package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.HumanResourcesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanResourcesRepository extends JpaRepository<HumanResourcesEntity, Long> {
    HumanResourcesEntity findByUsername(String username);

    List<HumanResourcesEntity> findByIsActive(Integer isActive);

    //ducvm
    HumanResourcesEntity findByCode(String code);

    List<HumanResourcesEntity> findByEmail(String email);

    @Query("SELECT HR FROM HumanResourcesEntity HR WHERE HR.email=?1")
    HumanResourcesEntity findByEmail2(String email);

    /*end duc */
    //TanNV
    HumanResourcesEntity findByHumanResourceId(Long id);

}

package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.dto.*;
import com.iist.qlda.project.entity.GroupPermissionUserEntity;
import com.iist.qlda.project.repository.customreporsitory.HumanResourcesCustomRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dangnp
 * @created 25/07/2020 - 10:29 AM
 * @project services
 **/
@Repository
public interface GroupPermissionUserRepository extends JpaRepository<GroupPermissionUserEntity, Long> {

    GroupPermissionUserEntity findByGroupPermissionIdAndUserId(Long gPermissionId, Long userId);
    //TanNv
    String sql=" SELECT  hr.HUMAN_RESOURCES_ID as humanResourceId, hr.USERNAME as username, " +
            " ap.PAR_CODE  as parcode " +
            " FROM HUMAN_RESOURCES as hr   " +
            " LEFT JOIN APP_PARAMS as ap on hr.DEPARTMENT_ID = ap.APP_PARAMS_ID  " +
            " WHERE ( LOWER(hr.USERNAME) like :name) ";
    @Query(value = sql, nativeQuery = true)
    List<IHumanResourcesShowDTO> getlistHumanResourcesDepatment(@Param("name") String name) ;

    void deleteByGroupPermissionIdAndUserId(Long grPId, Long userId);

    GroupPermissionUserEntity findByUserId(Long userId);
}

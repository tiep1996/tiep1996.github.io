package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.dto.IGroupUserDTO;
import com.iist.qlda.project.entity.GroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUserEntity,Long> {
    String sql="SELECT gu.GROUP_USER_ID as id ,gu.CODE as code,gu.NAME as name from GROUP_USER as gu ORDER BY gu.CREATE_DATE DESC";
    @Query(value = sql, nativeQuery = true, countQuery = sql)
    List<IGroupUserDTO> getAllGroupuser();
}

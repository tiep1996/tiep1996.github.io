package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.entity.AttachDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachDocumentRepository extends JpaRepository<AttachDocumentEntity, Long> {
    List<AttachDocumentEntity> findByParentId(Long parentId);

    //Dangnp
    String sql="SELECT * FROM ATTACH_DOCUMENT as AD WHERE AD.PARENT_ID=?1 and AD.IS_ACTIVE=1";
    @Query(value = sql, nativeQuery = true)
    List<AttachDocumentEntity> getList(Long id);
}

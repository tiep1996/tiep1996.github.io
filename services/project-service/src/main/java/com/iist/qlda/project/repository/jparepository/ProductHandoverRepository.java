package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.dto.IProjectMailDTO;
import com.iist.qlda.project.entity.ProductHandoverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductHandoverRepository extends JpaRepository<ProductHandoverEntity, Long> {
    List<ProductHandoverEntity> findAllByProjectId(Long id);
    ProductHandoverEntity findProductHandoverEntityByProductHandoverId(Long id);
}


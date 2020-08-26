package com.iist.qlda.project.service.productHandoverService;

import com.iist.qlda.project.dto.ProductHandoverDTO;
import com.iist.qlda.project.entity.ProductHandoverEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ProductHandoverService {
    void saveProductHandover(List<ProductHandoverDTO> productHandoverDTO);

    List<ProductHandoverEntity> viewProductHandover(Long id);

    void deleteProductHandover(List<Long> listDeleteId);

    ByteArrayInputStream exportFile(long id) throws IOException;
}

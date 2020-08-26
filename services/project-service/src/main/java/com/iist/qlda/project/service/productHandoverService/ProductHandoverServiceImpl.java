package com.iist.qlda.project.service.productHandoverService;

import com.iist.qlda.project.dto.ProductHandoverDTO;
import com.iist.qlda.project.dto.ProjectPlanDTO;
import com.iist.qlda.project.entity.ProductHandoverEntity;
import com.iist.qlda.project.repository.jparepository.ProductHandoverRepository;
import com.iist.qlda.project.repository.jparepository.ProjectRepository;
import common.CommonUtils;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductHandoverServiceImpl implements ProductHandoverService {
    Date date = new Date(System.currentTimeMillis());

    @Autowired
    private ProductHandoverRepository productHandoverRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public void saveProductHandover(List<ProductHandoverDTO> productHandoverDTO) {
        List<ProductHandoverEntity> productHandoverEntityList = new ArrayList<>();
        for (ProductHandoverDTO dto : productHandoverDTO) {
            ProductHandoverEntity productHandoverEntity;
            if(productHandoverRepository.findProductHandoverEntityByProductHandoverId(dto.getProductHandoverId()) == null){
                productHandoverEntity = new ProductHandoverEntity();
            }else{
                productHandoverEntity = productHandoverRepository.findProductHandoverEntityByProductHandoverId(dto.getProductHandoverId());
            }
            productHandoverEntity.setProjectId(dto.getProjectId());
            productHandoverEntity.setProductName(dto.getProductName().trim());
            productHandoverEntity.setStatusHandover(dto.getStatusHandover());
            productHandoverEntity.setDescription(dto.getDescription().trim());
            if(dto.getCreateDate() == null) {
                productHandoverEntity.setCreateDate(date);
            }else{
                productHandoverEntity.setCreateDate(dto.getCreateDate());
            }
            productHandoverEntity.setCreateBy(dto.getCreateBy());
            productHandoverEntity.setUpdateDate(date);
            productHandoverEntity.setUpdateBy(dto.getUpdateBy());
            productHandoverEntityList.add(productHandoverEntity);
        }
        productHandoverRepository.saveAll(productHandoverEntityList);
    }

    @Override
    public List<ProductHandoverEntity> viewProductHandover(Long pjId) {
        return productHandoverRepository.findAllByProjectId(pjId);
    }

    @Override
    public void deleteProductHandover(List<Long> listDeleteId) {
        for(long pjId: listDeleteId) {
            productHandoverRepository.deleteById(pjId);
        }
    }

    @Override
    public ByteArrayInputStream exportFile(long pjId) throws IOException {
        Workbook workbook = null;
        InputStream in = null;
        byte[] exportInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            List<ProductHandoverDTO> lstProductHandoverDto = getListHandoverDTO(pjId);
            String nameProject = projectRepository.findNameByProjectId(pjId).getName();

            in = CommonUtils.getInputStreamByFileName("product-handover-template.xlsx");

            Map<String, Object> beans = new HashedMap();
            beans.put("title", nameProject);
            beans.put("listProducts", Collections.singletonList(lstProductHandoverDto));

            XLSTransformer transformer = new XLSTransformer();
            workbook = transformer.transformXLS(in, beans);
            workbook.write(byteArrayOutputStream);
            exportInputStream = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
        }
        return new ByteArrayInputStream(exportInputStream);
    }

    private List<ProductHandoverDTO> getListHandoverDTO(long pjId) {
        String[] listStatus = {"Chưa làm", "Đang làm","Đã hoàn thành","Đã bàn giao khách hàng","Đang sửa theo comment khách hàng"};
        List<ProductHandoverDTO> lstProductHandoverDto = new ArrayList<>();
        List<ProductHandoverEntity> lstProductHandover = productHandoverRepository.findAllByProjectId(pjId);
        for(int i=0; i< lstProductHandover.size(); i++){
            ProductHandoverDTO productHandoverDTO = new ProductHandoverDTO();
            productHandoverDTO.setProductName(lstProductHandover.get(i).getProductName());
            productHandoverDTO.setStatusHandoverStr(listStatus[lstProductHandover.get(i).getStatusHandover()]);
            productHandoverDTO.setDescription(lstProductHandover.get(i).getDescription());
            productHandoverDTO.setStt(i+1);
            lstProductHandoverDto.add(productHandoverDTO);
        }
        return  lstProductHandoverDto;
    }

}

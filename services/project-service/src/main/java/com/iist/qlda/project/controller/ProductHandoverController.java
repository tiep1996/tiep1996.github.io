package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.ProductHandoverDTO;
import com.iist.qlda.project.entity.ProductHandoverEntity;
import com.iist.qlda.project.repository.jparepository.ProductHandoverRepository;
import com.iist.qlda.project.service.MailService;
import com.iist.qlda.project.service.productHandoverService.ProductHandoverService;
import common.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.iist.qlda.project.common.StatusCode.SYSTEM_ERROR;

@RestController
@RequestMapping("/productHandover")
@CrossOrigin("*")
public class ProductHandoverController {

    private final Logger log = LogManager.getLogger(ProductHandoverController.class);

    @Autowired
    private ProductHandoverService productHandoverService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ProductHandoverRepository productHandoverRepository;

    @PostMapping("/saveProducts")
    public ResponseEntity<?> addProductHandover(@RequestBody List<ProductHandoverDTO> dto) {
        try {
            log.info("----------------api search auto complete nhan su-----------------");
            //save product
            productHandoverService.saveProductHandover(dto);
            //send mail
            mailService.sendMailAddProductHandover(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewProducts/{pjId}")
    public ResponseEntity<List<ProductHandoverEntity>> viewProductHandover(@PathVariable Long pjId) {
        try {
            log.info("----------------api search auto complete nhan su-----------------");
            return new ResponseEntity(productHandoverService.viewProductHandover(pjId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteProducts")
    public ResponseEntity<?> deleteProductHandover(@RequestBody List<Long> listDeleteId) {
        try {
            log.info("----------------api search auto complete nhan su-----------------");
            productHandoverService.deleteProductHandover(listDeleteId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/exportFile/{id}")
    public ResponseEntity<?> exportFile(@PathVariable("id") long id) {
        try {
            ByteArrayInputStream byteArrayInputStream = productHandoverService.exportFile(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            String fileName = CommonUtils.getFileNameReportUpdate("DanhSachSanPhamBanGiaoKH");

            headers.add("File", fileName);
            headers.add("Content-Disposition", "attachment; filename=" + fileName);
            headers.add("Access-Control-Expose-Headers", "File");
            return new ResponseEntity<>(new InputStreamResource(byteArrayInputStream), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

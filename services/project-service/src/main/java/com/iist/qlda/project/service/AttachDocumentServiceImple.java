package com.iist.qlda.project.service;

import com.iist.qlda.project.entity.AttachDocumentEntity;
import com.iist.qlda.project.repository.jparepository.AttachDocumentRepository;
import common.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class AttachDocumentServiceImple implements AttachDocumentService{
    @Autowired
    private AttachDocumentRepository attachDocumentRepository;

    @Value("${folderUpload}")
    private String diskSaveFile; //folder luu file upload

    @Override
    public void uploadFile(String projectCode, Long parentId, Long createBy, MultipartFile[] files) {
//        String diskSaveFile = "D:/fileUpload/";

        if(files.length > 0){
            for(MultipartFile file : files){
                String isNotExist = projectCode + "_" + new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + "_";
                String path = diskSaveFile + isNotExist + file.getOriginalFilename();
                File fileUpload = new File(path);
                try {
                    FileOutputStream fileOut = new FileOutputStream(fileUpload);
                    AttachDocumentEntity documentEntity = new AttachDocumentEntity();
                    documentEntity.setName(file.getOriginalFilename());
                    documentEntity.setPath(path);
                    documentEntity.setType(1);
                    documentEntity.setIsActive(1);
                    documentEntity.setParentId(parentId);
                    documentEntity.setCreateDate(new Date());
                    documentEntity.setUpdateDate(new Date());
                    documentEntity.setCreateBy(createBy);
                    documentEntity.setSize( Double.parseDouble( String.valueOf(file.getSize()/(1024*1024))));
                    attachDocumentRepository.save(documentEntity);


                fileOut.write(file.getBytes());

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }

    }
}

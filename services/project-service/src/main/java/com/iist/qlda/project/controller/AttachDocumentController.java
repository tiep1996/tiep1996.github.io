package com.iist.qlda.project.controller;

import common.CommonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/attachDocument")
public class AttachDocumentController {
//    @GetMapping("download-file")
//    public ResponseEntity<?> downloadFile(@RequestParam("pathFile") String pathFile) {
//        try {
//            File file = new File(pathFile);
//            InputStream inputStream = new FileInputStream(file);
//            return CommonUtils.getResponseFromByte(file.getName(), IOUtils.toByteArray(inputStream));
//        } catch (Exception e) {
//            return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}

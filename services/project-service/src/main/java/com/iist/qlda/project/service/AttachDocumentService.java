package com.iist.qlda.project.service;

import org.springframework.web.multipart.MultipartFile;

public interface AttachDocumentService {
    public void uploadFile(String projectCode,Long parentId, Long createBy, MultipartFile[] files);
}

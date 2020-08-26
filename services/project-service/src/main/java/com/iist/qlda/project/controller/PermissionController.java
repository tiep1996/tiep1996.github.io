package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.GroupPermissionDTO;
import com.iist.qlda.project.dto.TreeDTO;
import com.iist.qlda.project.service.PermissionService;
import common.ErrorCode;
import common.ResultResp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@CrossOrigin("*")
public class PermissionController {

    private final Logger log = LogManager.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/list-permissions")
    public ResultResp getAll(@RequestBody GroupPermissionDTO dto) {
        try {
            List<TreeDTO> result = permissionService.getTreePermission(dto.getId());
            return ResultResp.success(result);
        } catch (Exception ex) {
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

}

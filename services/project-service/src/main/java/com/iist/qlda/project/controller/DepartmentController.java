package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.DepartmentDTO;
import com.iist.qlda.project.repository.jparepository.DepartmentRepository;
import com.iist.qlda.project.service.DepartmentService;
import com.iist.qlda.project.service.mapper.DepartmentMapper;
import common.ErrorCode;
import common.ResultResp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dangnp
 * @created 24/07/2020 - 5:22 PM
 * @project services
 **/

@RestController
@RequestMapping("/department")
@CrossOrigin("*")
public class DepartmentController {

    private Logger log = LogManager.getLogger(DepartmentController.class);
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public ResultResp getAll() {
        log.info("<--- controller - getAll: start --->");
        try {
            List<DepartmentDTO> result = departmentService.findAll();
            log.info("<--- list department result: ");
            log.info(result);
            log.info("<--- controller - getAll: end --->");
            return ResultResp.success(result);
        } catch (Exception ex) {
            log.error("<--- controller - getAll: error --->");
            log.error(ex);
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

    @GetMapping("/list-by-status")
    public ResultResp getAllByStatus(@RequestParam String status) {
        log.info("<--- controller - getAllByStatus: start --->");
        try {
            List<DepartmentDTO> result = departmentService.findAllByStatus(status);
            log.info("<--- list department result: ");
            log.info(result);
            log.info("<--- controller - getAllByStatus: end --->");
            return ResultResp.success(result);
        } catch (Exception ex) {
            log.error("<--- controller - getAllByStatus: error --->");
            log.error(ex);
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

    @GetMapping("/by-id")
    public ResultResp getById(@RequestParam Long id) {
        log.info("<--- controller - getById: start --->");
        try {
            DepartmentDTO result = departmentService.findById(id);
            log.info("<--- list department result: ");
            log.info(result);
            log.info("<--- controller - getById: end --->");
            return ResultResp.success(result);
        } catch (Exception ex) {
            log.error("<--- controller - getById: error --->");
            log.error(ex);
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

}

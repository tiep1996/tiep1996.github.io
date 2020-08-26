package com.iist.qlda.project.service.impl;

import com.iist.qlda.project.dto.DepartmentDTO;
import com.iist.qlda.project.repository.jparepository.DepartmentRepository;
import com.iist.qlda.project.service.DepartmentService;
import com.iist.qlda.project.service.mapper.DepartmentMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dangnp
 * @created 24/07/2020 - 4:58 PM
 * @project services
 **/

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private Logger log = LogManager.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDTO> findAll() {
        log.info("<--- service - findAll : start --->");
        try {
            List<DepartmentDTO> result = departmentMapper.toDto(departmentRepository.findAll());
            log.info("<--- result: ");
            log.info(result);
            log.info("<--- service - findAll : end --->");
            return result;
        } catch (Exception ex) {
            log.error("<--- service - findAll : error --->");
            log.error(ex);
            throw ex;
        }

    }

    @Override
    public List<DepartmentDTO> findAllByStatus(String status) {
        log.info("<--- service - findAllByStatus: start --->");
        try {
            List<DepartmentDTO> result = departmentMapper.toDto(departmentRepository.findAllByStatus(status));
            log.info("<--- result: ");
            log.info(result);
            log.info("<--- service - findAllByStatus : end --->");
            return result;
        } catch (Exception ex) {
            log.error("<--- service - findAllByStatus : error --->");
            log.error(ex);
            throw ex;
        }

    }

    @Override
    public DepartmentDTO findById(Long id) {
        log.info("<--- service - findById: " + id + "start --->");
        try {
            DepartmentDTO result = departmentMapper.toDto(departmentRepository.findById(id).get());
            log.info("<--- result: ");
            log.info(result);
            log.info("<--- service - findById " + id + ": end --->");
            return result;
        } catch (Exception ex) {
            log.error("<--- service - findById " + id + ": error --->");
            log.error(ex);
            throw ex;
        }

    }
}

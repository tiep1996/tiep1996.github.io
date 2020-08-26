package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.DepartmentDTO;

import java.util.List;

/**
 * @author dangnp
 * @created 24/07/2020 - 4:58 PM
 * @project services
 **/
public interface DepartmentService {

    List<DepartmentDTO> findAll();

    List<DepartmentDTO> findAllByStatus(String status);

    DepartmentDTO findById(Long id);


}

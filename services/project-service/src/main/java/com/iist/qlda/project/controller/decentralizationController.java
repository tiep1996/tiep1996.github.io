package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.PermissionDTO;
import com.iist.qlda.project.service.decentralizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author haiNH - IIST
 * created on 7/14/2020
 */
@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class decentralizationController {
    decentralizationService decentralizationService;
    @PostMapping("/serchdecentralization")
    public ResponseEntity<List<PermissionDTO>> findByField(@RequestBody PermissionDTO permissionDTO) {
        List<PermissionDTO> dataCategoryDtos = decentralizationService.findByParams(permissionDTO);
        return new ResponseEntity<>(dataCategoryDtos, HttpStatus.OK);
    }

    @PostMapping("/saveDecentralization")
    public ResponseEntity<List<PermissionDTO>> saveDecentralization(@RequestBody PermissionDTO permissionDTO) {
        decentralizationService.saveDecentralization(permissionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.AppParamDTO;
import com.iist.qlda.project.dto.DTOSearch;
import com.iist.qlda.project.dto.RoleDTO;
import com.iist.qlda.project.service.AppParamService;
import common.ObjectError;
import common.ResultResp;
import exception.CustomExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app-param")
@CrossOrigin("*")
public class AppParamController {
    @Autowired
    private AppParamService paramService;


    @GetMapping("/getAppParamById/{name}")
    public AppParamDTO getAppParamDetail(@PathVariable String name) {
        return paramService.getAppParamByNam(name);
    }

    @PostMapping("/getAppParam")
    public ResponseEntity<List<AppParamDTO>> getAppParam(@RequestBody DTOSearch dto) {
        return new ResponseEntity<List<AppParamDTO>>(paramService.getPartnerByPartype(dto), HttpStatus.OK);
    }

    @GetMapping("/code-position")
    public ResultResp getCodePosition() {
        try {
            List<RoleDTO> result = paramService.getCodePosition();
            return ResultResp.success(result);
        } catch (CustomExceptionHandler ex) {
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }


}

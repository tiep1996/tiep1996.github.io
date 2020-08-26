package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.GroupPermissionDTO;
import com.iist.qlda.project.dto.HumanResourcesShowDTO;
import com.iist.qlda.project.dto.IHumanResourcesShowDTO;
import com.iist.qlda.project.dto.PageApp;
import com.iist.qlda.project.entity.GroupPermissionUserEntity;
import com.iist.qlda.project.service.GroupPermissionService;
import common.ErrorCode;
import common.ObjectError;
import common.ResultResp;
import exception.CustomExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.iist.qlda.project.common.StatusCode.SYSTEM_ERROR;

/**
 * @author dangnp
 * @created 25/07/2020 - 11:04 AM
 * @project services
 **/
@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping("/group-permission")
public class GroupPermissionController {
    private final Logger log = LogManager.getLogger(GroupPermissionController.class);
    @Autowired
    private GroupPermissionService groupPermissionService;

    @GetMapping("/find")
    public ResultResp getById(@RequestParam(name = "id") Long gpId) {
        try {
            GroupPermissionDTO rs = groupPermissionService.findById(gpId);
            log.info("Result: " + rs);
            return ResultResp.success(rs);
        } catch (CustomExceptionHandler ex) {
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @GetMapping("/find-all")
    public ResultResp getAll(@RequestParam("page") int pageIndex,
                             @RequestParam("size") int itemsPerPage,
                             @RequestParam("sort") String sort,
                             @RequestParam("name") String name,
                             @RequestParam("status") String status,
                             @RequestParam("code") String code) {
        try {
            if ("".equals(code) || "--Tất cả--".equals(code)) {
                code = null;
            }
            if ("".equals(name)) {
                name = null;
            }
            if ("".equals(status)) {
                status = null;
            }

            PageApp pageApp = new PageApp(pageIndex, itemsPerPage, sort);
            PageApp<GroupPermissionDTO> rs = groupPermissionService.searchAllByUnit(code, name, status, pageApp);
            log.info("Result: " + rs);
            return ResultResp.success(rs);
        } catch (CustomExceptionHandler ex) {
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @PostMapping("/create")
    public ResultResp create(@RequestBody GroupPermissionDTO dto) {
        log.info("<--- api - create group permission : start --->");
        try {
            GroupPermissionDTO result = groupPermissionService.create(dto);
            log.info("<--- api - create group permission : success --->");
            log.info(result);
            return ResultResp.success(result);
        } catch (CustomExceptionHandler ex) {
            log.error(ex);
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @PutMapping("/update")
    public ResultResp update(@RequestBody GroupPermissionDTO dto) {
        log.info("<--- api - updalte group permission : start --->");
        try {
            GroupPermissionDTO result = groupPermissionService.update(dto, dto.getId());
            log.info("<--- api - update group permission : success --->");
            log.info(result);
            return ResultResp.success(result);
        } catch (CustomExceptionHandler ex) {
            log.error(ex);
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @GetMapping("/delete")
    public ResultResp delete(@RequestParam("id") Long id) {
        log.info("<--- api - delete group permission : start --->");
        try {
            GroupPermissionDTO result = groupPermissionService.delete(id);
            log.info("<--- api - delete group permission : success --->");
            log.info(result);
            return ResultResp.success(result);
        } catch (CustomExceptionHandler ex) {
            log.error(ex);
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @PostMapping("/checked")
    public ResultResp checkedGroupPermission(@RequestBody GroupPermissionDTO dto) {
        log.info("<--- api - checkedGroupPermission : start --->");
        try {
            GroupPermissionDTO result = groupPermissionService.checkedGroupPermission(dto.getId());
            log.info("<--- api - checkedGroupPermission : success --->");
            log.info(result);
            return ResultResp.success(result);
        } catch (CustomExceptionHandler ex) {
            log.error(ex);
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    //TanNV
    @PostMapping("/createGroupPermission")
    public ResultResp createGroupPermission(@RequestBody GroupPermissionUserEntity data) {
        try {
            groupPermissionService.addUser(data.getGroupPermissionId(), data.getUserId());
            return ResultResp.success(ErrorCode.OK);
        } catch (CustomExceptionHandler ex) {
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @GetMapping("/getlistHumanResourcesDepatment")
    public ResultResp getlistHumanResourcesDepatment(@RequestParam String name) {
        try {
            List<IHumanResourcesShowDTO> list = groupPermissionService.getlistHumanResourcesDepatment(name);
            return ResultResp.success(list);
        } catch (CustomExceptionHandler ex) {
            return ResultResp.badRequest((ObjectError) ex.getData());
        }
    }

    @PostMapping("/addUserPermission")
    public ResponseEntity<?> addUserPermission(@RequestBody List<Long> lstId) {
        try {
            groupPermissionService.addUserPermission(lstId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteUserPermission")
    public ResponseEntity<?> deleteUserPermission(@RequestBody List<Long> lstId) {
        try {
            groupPermissionService.deleteUserPermission(lstId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewUserPermission/{grId}")
    public ResponseEntity<List<HumanResourcesShowDTO>> viewUserPermission(@PathVariable Long grId) {
        try {
            return new ResponseEntity(groupPermissionService.viewUserPermission(grId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findUser/{uId}")
    public ResponseEntity<GroupPermissionUserEntity> isUserAdded(@PathVariable Long uId) {
        try {
            return new ResponseEntity(groupPermissionService.isUserAdded(uId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}

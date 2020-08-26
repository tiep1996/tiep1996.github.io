package com.iist.qlda.project.controller;

import com.iist.qlda.project.config.security.JWTProvider;
import com.iist.qlda.project.dto.DTOSearch;
import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.HumanResourcesShowDTO;
import com.iist.qlda.project.service.HumanResourcesService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//ANHTT_IIST
@RestController
@RequestMapping("/humanResources")
@CrossOrigin("*")
public class HumanResourcesController {
    private final Logger log = LogManager.getLogger(HumanResourcesController.class);

    @Autowired
    private HumanResourcesService resourcesService;

    @Autowired
    private JWTProvider jwtProvider;

    //ANHTT_IIST api
    @PostMapping("/getListUserByNameOrCode")
    public ResponseEntity<List<HumanResourcesDTO>> getListHumanResourceByNameOrCode(@RequestBody DTOSearch dto) {
        log.info("----------------api search auto complete nhan su-----------------");
        return new ResponseEntity(resourcesService.getListHumanResourceByNameOrCode(dto), HttpStatus.OK);
    }

    //ANHTT_IIST API GET thong tin user dang nhap
    @GetMapping("/getUserInfo")
    public ResponseEntity<HumanResourcesDTO> getUserInfo(@RequestParam("username") String username) {
        log.info("----------------api get thong tin nhan su----------------");
        return new ResponseEntity(resourcesService.getUserInfo(username), HttpStatus.OK);
    }

    // TanNV get HumanResourcesShowDTO danh sach
    @PostMapping("/searchHumanResources")
    public ResponseEntity<List<HumanResourcesShowDTO>> searchHumanResources(@RequestBody HumanResourcesShowDTO dto){
        log.info("----------------api searchHumanResources nhan su-----------------");
        try {
            log.info("----------------api searchHumanResources nhan su Ok-----------------");
            return new ResponseEntity( resourcesService.getPageHumanResourcesSeach(dto), HttpStatus.OK);
        }catch (Exception e){
            log.info("----------------api searchHumanResources nhan su fail-----------------");
            throw e;
        }

    }
    @DeleteMapping ("/deleteHumanResources/{id}")
    public ResultResp deleteProject(@PathVariable("id") Long id){
        log.info("----------------api delete nhan su-----------------");
        try {
            log.info("----------------api delete nhan su Ok-----------------");
            if(resourcesService.deleteHumanResources(id)){
                if(resourcesService.getActiveFromHumanResourceId(id) == 3){
                    log.info("<--- DELETE HUMAN_RESOURCES COMPLETE");
                    return ResultResp.success(ErrorCode.DELETE_HR_OK);
                }else if(resourcesService.getActiveFromHumanResourceId(id) == 1){
                    log.info("<--- UNLOCK HUMAN_RESOURCES COMPLETE");
                    return ResultResp.success(ErrorCode.UNLOCK_HR_OK);
                }
            }else {
                log.error("<--- DELTE HUMAN_RESOURCES FAIL, HAVE ASSOCIATION");
                return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
            }
        } catch (CustomExceptionHandler e) {
            log.info("----------------api delete nhan su faile-----------------");
            return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
        }
        return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
    }
    //end TanNV
    //Hungnv change password
    @PutMapping ("/changePassword")
    public ResultResp changePasswordHumanResouces (@RequestBody HumanResourcesDTO humanResourcesDTO){
        try {
            Long id = resourcesService.changePassword(humanResourcesDTO);
            return ResultResp.success(ErrorCode.UPDATED_OK);
        }catch (CustomExceptionHandler e) {
            if(e.getMsgCode().equalsIgnoreCase("sai_password")){
                return ResultResp.badRequest(ErrorCode.OLD_PASSWORD_FAILE);
            }
            return ResultResp.badRequest(new ObjectError("BK014"," Nhập lại mật khẩu không khớp"));
        }catch (Exception e){
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }

    }
    @PostMapping(value = "/check-password")
    public ResultResp checkPassword(@RequestBody HumanResourcesDTO  humanResourcesDTO) {
        log.info("<-- api check password: start, ");
        try {
            return ResultResp.success(resourcesService.checkPassword(humanResourcesDTO));
        } catch (CustomExceptionHandler e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.OLD_PASSWORD_FAILE);
        }
    }


    //end hungnv

    /*duc controller*/
    @GetMapping("/getGroupUser")
    public ResultResp getAllGroupUser() {
        return ResultResp.success(resourcesService.getAllGroupUser());
    }

    @GetMapping("/getHumanCenter")
    public ResultResp getHumanCenter() {
        return ResultResp.success(resourcesService.getCenters());
    }

    @PostMapping("/add")
    public ResultResp createHR(@RequestBody HumanResourcesDTO humanResourcesDTO) {
        log.info("<--- api createNewHr: start,", humanResourcesDTO);
        try {
            return ResultResp.success(ErrorCode.CREATED_HR_OK,resourcesService.create(humanResourcesDTO));

        } catch (CustomExceptionHandler e) {
            if (e.getMsgCode().equalsIgnoreCase(ErrorCode.CREATED_HR_EXIST.getCode()))
                return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
        return ResultResp.badRequest(ErrorCode.CREATED_HR_FALSE);
    }

    @PutMapping(value = "/update")
    public ResultResp updateHr(@RequestBody HumanResourcesDTO humanResourcesDTO) {
        log.info("<-- api updateHumanResources: start, ", humanResourcesDTO);
        try {
            return ResultResp.success(ErrorCode.UPDATED_OK, resourcesService.update(humanResourcesDTO));

        } catch (Exception e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

    @GetMapping(value = "/check-email/{email}")
    public ResultResp checkEmail(@PathVariable("email") String email) {
        log.info("<-- api check Email: start, ");
        try {
            return ResultResp.success(resourcesService.findByEmail(email));

        } catch (CustomExceptionHandler e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.EMAIL_IS_EXIST);
        }
    }

    @GetMapping(value = "/check-username/{username}")
    public ResultResp checkUsername(@PathVariable("username") String username) {
        log.info("<-- api check username: start, ");
        try {
            return ResultResp.success(resourcesService.checkUsername(username));

        } catch (CustomExceptionHandler e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
    }

    @GetMapping("/get-human-by-id/{id}")
    public ResultResp getOneById(@PathVariable("id") Long id){
        log.info("<-- api updateHumanResources: start, ", id);
        try {
            return ResultResp.success(resourcesService.findById(id));
        }catch (CustomExceptionHandler e){
            return ResultResp.badRequest(ErrorCode.USERNAME_NOT_FOUND);
        }
        catch (Exception e){
            log.error("<--- api find HumanResources: error, ");
            e.printStackTrace();
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }

    }
    /*end duc*/

    //nuctv 30/07
    @GetMapping("/synchronized/{humanResourceID}")
    public ResponseEntity<?> insert(@PathVariable("humanResourceID") Long id) {
        try {
            return new ResponseEntity<>( resourcesService.updateDataHumanResources(id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    /*
      *@author ThaoLC - IIST
      *created on 7/30/2020
     */
    @PutMapping("/reset-password/{id}")
    public ResultResp resetPassword(@PathVariable("id") Long humanResourceID, HttpServletRequest req){
        String header = req.getHeader("Authorization");
        String username = jwtProvider.getUsernameFromHeaders(header);
        log.info("<--- USERNAME FROM TOKEN"+username);
        log.info("<--- Reset Password HumanResource Start!");
        return resourcesService.resetPassword(humanResourceID,username);
    }
}

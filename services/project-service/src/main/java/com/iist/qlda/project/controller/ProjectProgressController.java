package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.ProjectProgressLinkRedmineDto;
import com.iist.qlda.project.service.ProjectProgressService;
import common.ErrorCode;
import common.ObjectError;
import common.ResultResp;
import exception.CustomExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nuctv
 * @since 12 ,7/24/2020 , 2020
 */
@RestController
@RequestMapping("/project-progress")
@CrossOrigin("*")
public class ProjectProgressController {
    private final Logger log = LogManager.getLogger(ProjectProgressController.class);
    @Autowired
    private ProjectProgressService projectProgressService;

    @PutMapping(value = "/save-link-redmine")
    public ResultResp saveLinkRedmine(@RequestBody ProjectProgressLinkRedmineDto projectProgressLinkRedmineDto){
        log.info("---->gọi api lưu lại link tiến độ dự án");
        try{
            return ResultResp.success(ErrorCode.UPDATED_OK,projectProgressService.saveLinkRedmine(projectProgressLinkRedmineDto));
        } catch (CustomExceptionHandler e){
           return ResultResp.badRequest(new ObjectError("CANT_SAVE_LINK",e.getMsgCode()));
        } catch (Exception e){
            log.error("---->lỗi server");
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }
    @GetMapping(value = "/synchronized-with-redmine")
    public ResultResp synchronizedWithRedmine() {
        log.info("---->gọi api đồng bộ tiến độ dự án với redmine");
        try {
            List<String> error = projectProgressService.synchronizedWithRedmine();
            log.info("---->đồng bộ thành công");
            return ResultResp.success(error);
        } catch (CustomExceptionHandler e) {
            log.error("---->convert dữ liệu từ redmine lỗi");
            return ResultResp.badRequest(new ObjectError("CANT_CALL_REDMINE","Không gọi được api từ redmine"));
        } catch (Exception e) {
            log.error("---->lỗi server");
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get-link-redmine")
    public ResultResp getLinkRedmineByProjectID(@RequestParam(value = "projectID",required = true) Long projectID){
        return ResultResp.success(projectProgressService.getLInkRedmineByProjectID(projectID));
    }

    @PostMapping(value = "/check-link")
    public ResultResp checkLinkRedMine(@RequestBody ProjectProgressLinkRedmineDto dto){

       try{
           List<String> err= projectProgressService.checkLinkExist(dto);
           return ResultResp.success(err);
       } catch (Exception e){
           return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
       }

    }
}

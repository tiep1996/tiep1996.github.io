package com.iist.qlda.project.controller;

import com.iist.qlda.project.config.security.JWTProvider;
import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.MasterPlanDTO;
import com.iist.qlda.project.dto.ProjectPlanDTO;
import com.iist.qlda.project.entity.MasterPlanEntity;
import com.iist.qlda.project.entity.ProjectPlanEntity;
import com.iist.qlda.project.repository.jparepository.MasterPlanRepository;
import com.iist.qlda.project.repository.jparepository.ProductHandoverRepository;
import com.iist.qlda.project.repository.jparepository.ProjectPlanRepository;
import com.iist.qlda.project.service.*;
import common.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static com.iist.qlda.project.common.StatusCode.*;

//TiepLN
@RestController
@RequestMapping("/projectPlan")
@CrossOrigin("*")
public class ProjectPlanController {

//    private Logger log = LogManager.get;
    @Autowired
    private MasterPlanService masterPlanService;

    @Autowired
    private ProjectPlanService projectPlanService;

    @Autowired
    private HumanResourcesService humanResourcesService;

	@Autowired
	private MasterPlanRepository masterPlanRepository;

	@Autowired
	private MailServiceMasterplan mailServiceMasterplan;

	@Autowired
    private ProjectPlanRepository projectPlanRepository;

	@GetMapping("/info/{id}")
	public ResponseEntity<ProjectPlanDTO> getMasterPlan(@PathVariable("id") long i) {
		return new ResponseEntity(masterPlanService.findAllByProjectId(i), HttpStatus.OK);
	}

    @GetMapping("/human/{id}")
    public ResponseEntity<HumanResourcesDTO> getHumanProject(@PathVariable("id") long i) {
        return new ResponseEntity(humanResourcesService.getListHumanResourceByProjectID(i), HttpStatus.OK);
    }

    @PostMapping("/savemasterplan")
    public ResponseEntity saveProject(@RequestBody MasterPlanDTO dto) {
        List<ProjectPlanDTO> projectPlanDTOList = dto.getLstprojectPlanDTO();
        //save master plan
        masterPlanService.saveProject(dto);
        //send mail
        mailServiceMasterplan.sendMailAddMasterplanProject(dto, projectPlanDTOList);

		return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/confirmmasterplan")
    public ResponseEntity confirmProject(@RequestBody MasterPlanDTO dto) {
	    //dangnp
	    MasterPlanEntity entity = masterPlanRepository.getByProjectId(dto.getProjectId());
	    MasterPlanDTO oldStatus = new MasterPlanDTO();
	    if (entity != null){
	        oldStatus.setStatusPM(entity.getStatusPM());
	        oldStatus.setStatusBA(entity.getStatusBA());
	        oldStatus.setStatusTest(entity.getStatusTest());
	        oldStatus.setStatusQA(entity.getStatusQA());
        }
        //save master plan
        masterPlanService.confirmProject(dto);
	    //send mail
        mailServiceMasterplan.sendMailAgreeOrDeclineMasterplan(dto, oldStatus);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletemasterplan")
    public ResponseEntity<ProjectPlanDTO> deleteMasterPlan(@RequestBody List<Long> lstDelete) {
        return new ResponseEntity(projectPlanService.deleteProjectPlan(lstDelete), HttpStatus.OK);
    }

    @GetMapping("/exportFile/{id}")
    public ResponseEntity<?> exportFile(@PathVariable("id") long id) {
        try {
            ByteArrayInputStream byteArrayInputStream = projectPlanService.exportFile(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            String fileName = CommonUtils.getFileNameReportUpdate("MASTER_PLAN");

            headers.add("File", fileName);
            headers.add("Content-Disposition", "attachment; filename=" + fileName);
            headers.add("Access-Control-Expose-Headers", "File");
            return new ResponseEntity<>(new InputStreamResource(byteArrayInputStream), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(SYSTEM_ERROR.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}

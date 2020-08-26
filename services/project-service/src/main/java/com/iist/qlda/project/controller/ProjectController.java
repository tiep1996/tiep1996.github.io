package com.iist.qlda.project.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iist.qlda.project.config.security.JWTConstants;
import com.iist.qlda.project.config.security.JWTProvider;
import com.iist.qlda.project.dto.AttachDocumentDTO;
import com.iist.qlda.project.dto.IHumanResourceOfProject;
import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.entity.ProjectEntity;
import com.iist.qlda.project.message.ResponseMessage;
import com.iist.qlda.project.repository.jparepository.ProjectPlanRepository;
import com.iist.qlda.project.repository.jparepository.ProjectRepository;
import com.iist.qlda.project.service.MailService;
import com.iist.qlda.project.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//ANHTT_IIST
@RestController
@RequestMapping("/project")
@CrossOrigin("*")
public class ProjectController {
    private final Logger log = LogManager.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private JWTProvider jwtProvider;

    //anhtt_iist luu thong tin du an
    @PostMapping("/saveProject")
    public ResponseEntity<ProjectDTO> saveProject(@RequestPart("data") String data, @RequestParam(value = "listAttachDocument", required = false) MultipartFile[] lstAttachFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProjectDTO dto = null;
        try{
            dto = objectMapper.readValue(data, new TypeReference<ProjectDTO>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //save project
       ProjectDTO dtoRes= projectService.saveProject(dto, lstAttachFile);

        //send mail start
        mailService.sendMailAddProject(dtoRes);

//        return new ResponseEntity(projectService.saveProject(dto), HttpStatus.OK);
        return new ResponseEntity(HttpStatus.OK);
    }

    //anhtt_iist xoa du an
    @GetMapping("/deleteProject")
    public ResponseEntity<ProjectDTO> deleteProject(@RequestParam("projectId") Long projectId){

        projectService.deleteProject(projectId);
        return new ResponseEntity(projectId, HttpStatus.OK);
    }

    //anhtt_iist tim kiem tin du an
    @PostMapping("/searchProject")
    public ResponseEntity<List<ProjectDTO>> searchProject(@RequestBody ProjectDTO dto){
        return new ResponseEntity( projectService.listProject(dto), HttpStatus.OK);
    }

    //anhtt_iist check ma du an
    @GetMapping("/checkCodeExist")
    public ResponseEntity checkCodeExist(@RequestParam String code){
        return new ResponseEntity( projectService.checkCodeExist(code), HttpStatus.OK);
    }

    //ANHTT_IIST
    @GetMapping("/countMember")
    public ResponseEntity countProjectMember(@RequestParam(value = "department", required = false) String department,
                                             @RequestParam(value = "projectId", required = false) Long projectId,
                                             @RequestParam(value = "role", required = false) String role){
        return new ResponseEntity(projectService.countProjectMember(department, projectId, role), HttpStatus.OK);
    }



    @PostMapping("/editProject")
    public ResponseEntity<ProjectDTO> editProject(HttpServletRequest request, @RequestPart("data") String data, @RequestParam(value = "listAttachDocument", required = false) MultipartFile[] lstAttachFile){
        ObjectMapper objectMapper = new ObjectMapper();
        ProjectDTO dto = null;
        try{
            dto = objectMapper.readValue(data, new TypeReference<ProjectDTO>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //dangnp
        ProjectEntity entityOld=projectRepository.findByProjectId(dto.getProjectId());
        String code = entityOld.getCode();
        Double estimatePrelimiinary=0D;
        Double estimateInternal =0D;
        if(entityOld.getEstimatePrelimiinary()!=null){
            estimatePrelimiinary=entityOld.getEstimatePrelimiinary();
        }
        if (entityOld.getEstimateInternal() != null) {
            estimateInternal = entityOld.getEstimateInternal();
        }
        //4 lead cũ
        List<IHumanResourceOfProject> oldHuman = projectPlanRepository.findAllHumanResourceByProjectID(dto.getProjectId());
        //---->edit project
        projectService.editProject(dto,lstAttachFile);

        //----send mail start update----
        if (entityOld.getCode()!=null){
            dto.setCode(code);
        }
        //lấy ra username đang đăng nhập
        String header = request.getHeader(JWTConstants.HEADER_STRING);
        String username = jwtProvider.getUsernameFromHeaders(header);
        mailService.sendMailULNLOrChangeHuman(dto, estimatePrelimiinary, estimateInternal, username, oldHuman);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/saveEstimate")
    public ResponseEntity<ProjectDTO> saveEstimate(@RequestBody ProjectDTO dto){
         projectService.saveEstimate(dto);
        return new ResponseEntity(HttpStatus.OK);
    }



    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String message = "";

        projectService.save(file);

          message = "Uploaded the file successfully: " + file.getOriginalFilename();
          return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

      }



    @GetMapping("/files")
    public ResponseEntity<List<AttachDocumentDTO>> getListFiles() {
//      List<AttachDocumentDTO> fileInfos = projectService.loadAll().map(path -> {
//        String filename = path.getFileName().toString();
//        String url = MvcUriComponentsBuilder
//            .fromMethodName(ProjectController.class, "getFile", path.getFileName().toString()).build().toString();
//
//        return new FileInfo(filename, url);
//      }).collect(Collectors.toList());
//
//      return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    	return null;
    }



    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
      Resource file = projectService.load(filename);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}

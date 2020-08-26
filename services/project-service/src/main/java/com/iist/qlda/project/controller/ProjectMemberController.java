package com.iist.qlda.project.controller;

import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import com.iist.qlda.project.entity.ProjectMemberEntity;
import com.iist.qlda.project.repository.jparepository.ProjectMemberRespository;
import com.iist.qlda.project.service.MailService;
import com.iist.qlda.project.service.ProjectMemberService;
import net.bytebuddy.asm.Advice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//anhtt_iist
@RestController
@RequestMapping("/projectMember")
@CrossOrigin("*")
public class ProjectMemberController {
    private final Logger log = LogManager.getLogger(ProjectMemberController.class);

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ProjectMemberRespository projectMemberRespository;

    @PostMapping("/saveProjectMember")
    public ResponseEntity<ProjectDTO> saveProjectMember(@RequestBody ProjectDTO dto){
        //list dto project membet
        List<ProjectMemberDTO> listDTO = dto.getLstProjectMember();
        //save project member
        projectMemberService.saveProjectMember(dto);
        //send mail add project member
        mailService.sendMailAddHumanResourceProject(dto, listDTO);
        //return new ResponseEntity<ProjectDTO>(projectMemberService.saveProjectMember(dto), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/getListMemberProject")
    public ResponseEntity<List<ProjectMemberDTO>> getListMemberProject(@RequestBody ProjectDTO dto){
        return new ResponseEntity(projectMemberService.getListMemberProject(dto),HttpStatus.OK);
    }

//    nuctv 10/08
    @GetMapping("/findByProjectIDAndHumanID/{proID}/{humanID}")
    public ResponseEntity<List<ProjectMemberEntity>> findMemberByProjectId(@PathVariable("proID") Long proID,@PathVariable("humanID") Long humanID){
        try{
        return new ResponseEntity<>(projectMemberService.findByProjectIDAndHumanID(proID,humanID),HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

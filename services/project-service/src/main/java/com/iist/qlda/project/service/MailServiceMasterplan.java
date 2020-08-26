package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.IEmailBOD;
import com.iist.qlda.project.dto.IProjectAndHumanMasterplan;
import com.iist.qlda.project.dto.MasterPlanDTO;
import com.iist.qlda.project.dto.ProjectPlanDTO;
import com.iist.qlda.project.entity.MasterPlanEntity;
import com.iist.qlda.project.entity.ProjectEntity;
import com.iist.qlda.project.entity.ProjectPlanEntity;
import com.iist.qlda.project.repository.jparepository.MasterPlanRepository;
import com.iist.qlda.project.repository.jparepository.ProjectPlanRepository;
import com.iist.qlda.project.repository.jparepository.ProjectRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dangnp
 * @created 04/08/2020 - 9:45 AM
 * @project services
 **/
@Service
public class MailServiceMasterplan {


    @Autowired
    private JavaMailSender javaMailSender;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration config;

    @Autowired
    private ProjectPlanRepository projectPlanRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private final Logger log = LogManager.getLogger(MailServiceImpl.class);

    //sen mail add masterplan project
    @Async("asyncExecutor")
    public void sendMailAddMasterplanProject(MasterPlanDTO masterPlanDTO, List<ProjectPlanDTO> projectPlanDTOS){
        if (projectPlanDTOS.isEmpty()){
            return;
        }
        for (ProjectPlanDTO projectPlanDTO:projectPlanDTOS){
            if (projectPlanDTO.getProjectPlanId()==null){
                MimeMessage message = javaMailSender.createMimeMessage();
                Long projectID = masterPlanDTO.getProjectId();
                ProjectEntity projectEntity = projectRepository.findByProjectId(projectID);
                //put properti
                Map<String,Object> models = new HashMap<>();
                models.put("codeProject", projectEntity.getCode());
                models.put("nameProject", projectEntity.getName());
                List<IProjectAndHumanMasterplan> list =  projectPlanRepository.getMailByProjectID(projectID);
                //mailTo and CC
                List<String> listEmailTo = new ArrayList<>();
                List<String> listEmailCc = new ArrayList<>();
                for (IProjectAndHumanMasterplan iProjectAndHumanMasterplan:list){
                    listEmailTo.add(iProjectAndHumanMasterplan.getEmail());
                }
                //List BOD
                List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
                for (IEmailBOD iEmailBOD:listMailBOD){
                    listEmailCc.add(iEmailBOD.getEmail());
                }
                //Array
                String[] emailToArr = new String[listEmailTo.size()];
                String[] emailCcArr = new String[listEmailCc.size()];
                //Convert list to array
                emailToArr = listEmailTo.toArray(emailToArr);
                emailCcArr = listEmailCc.toArray(emailCcArr);
                log.info("---------->Send mail add master plan project start!<----------");
                try{
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                    Template template = config.getTemplate("email-send-add-masterplan-project-template.ftl");
                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

                    helper.setTo(emailToArr);
                    helper.setCc(emailCcArr);
                    helper.setSubject("Thông Báo Thêm Mới Master Plan Dự Án: " + projectEntity.getCode() + "-" + projectEntity.getName());
                    helper.setText(html, true);
                    javaMailSender.send(message);
                    log.info("---------->Send mail add master plan project success!<----------");
                }catch (MessagingException | IOException | TemplateException exception){
                    exception.getMessage();
                    log.info("---------->Send mail add master plan project fail<----------",exception.getMessage());
                }
            }
            break;
        }
    }

    //send mail từ chối/đồng ý master plan
    @Async("asyncExecutor")
    public void sendMailAgreeOrDeclineMasterplan(MasterPlanDTO dto, MasterPlanDTO oldStatus){
        MimeMessage message = javaMailSender.createMimeMessage();
        Map<String,Object> models = new HashMap<>();
        Boolean pmShow = false;
        Boolean baShow = false;
        Boolean tlShow = false;
        Boolean qaShow = false;
        Boolean pmChange =false;
        Boolean baChange =false;
        Boolean tlChange =false;
        Boolean qaChange =false;
        String title=null;
        ProjectEntity projectEntity = projectRepository.findByProjectId(dto.getProjectId());
        //pm
        if (dto.getStatusPM()!=oldStatus.getStatusPM()){
            pmShow = true;
            if (dto.getStatusPM()==1){
                pmChange=true;
                title = "PM Đồng Ý";
            }else {
                pmChange=false;
                title = "PM Từ Chối";
                models.put("pmReason", dto.getReasonPM());
            }
        }
        models.put("pmShow", pmShow);
        models.put("pmChange", pmChange);
        //ba
        if (dto.getStatusBA()!=oldStatus.getStatusBA()){
            pmShow =true;
            if (dto.getStatusBA()==1){
                baChange=true;
                title = "BA Đồng Ý";
            }else {
                baChange=false;
                title = "BA Từ Chối";
                models.put("baReason", dto.getReasonBA());
            }
        }
        models.put("baShow", baShow);
        models.put("baChange", baChange);
        //test lead
        if (dto.getStatusTest()!=oldStatus.getStatusTest()){
            tlShow = true;
            if (dto.getStatusTest()==1){
                tlChange=true;
                title = "Test Lead Đồng Ý";
            }else {
                tlChange=false;
                title = "Test Lead Từ Chối";
                models.put("tlReason", dto.getReasonTest());
            }
        }
        models.put("tlShow", tlShow);
        models.put("tlChange", tlChange);
        //qa
        if (dto.getStatusQA()!=oldStatus.getStatusQA()){
            qaShow = true;
            if (dto.getStatusQA()==1){
                qaChange=true;
                title = "QA Đồng Ý";
            }else {
                qaChange=false;
                title = "QA Từ Chối";
                models.put("qaReason", dto.getStatusQA());
            }
        }
        models.put("qaShow", qaShow);
        models.put("qaChange", qaChange);
        //put property
        models.put("codeProject", projectEntity.getCode());
        models.put("nameProject", projectEntity.getName());
        List<IProjectAndHumanMasterplan> list = projectPlanRepository.getMailByProjectID(dto.getProjectId());
        //mailTo and CC
        List<String> listEmailTo = new ArrayList<>();
        List<String> listEmailCc = new ArrayList<>();
        for (IProjectAndHumanMasterplan iProjectAndHumanMasterplan:list){
            listEmailTo.add(iProjectAndHumanMasterplan.getEmail());
        }
        //List BOD
        List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
        for (IEmailBOD iEmailBOD:listMailBOD){
            listEmailCc.add(iEmailBOD.getEmail());
        }
        //Array
        String[] emailToArr = new String[listEmailTo.size()];
        String[] emailCcArr = new String[listEmailCc.size()];
        //Convert list to array
        emailToArr = listEmailTo.toArray(emailToArr);
        emailCcArr = listEmailCc.toArray(emailCcArr);
        log.info("---------->Send mail update master plan project start!<----------");
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-agreeOrdecline-masterplan-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setTo(emailToArr);
            helper.setCc(emailCcArr);
            helper.setSubject("Xác Nhận Đồng Ý/Từ Chối Master Plan: " + models.get("codeProject") + "-" + models.get("nameProject") +", "+ title);
            helper.setText(html, true);
            javaMailSender.send(message);
            log.info("---------->Send mail update master plan project success!<----------");
        }catch (MessagingException | IOException | TemplateException exception){
            exception.getMessage();
            log.info("---------->Send mail update master plan project fail<----------",exception.getMessage());
        }
    }
}

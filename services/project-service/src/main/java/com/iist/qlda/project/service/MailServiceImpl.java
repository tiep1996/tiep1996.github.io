package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.*;
import com.iist.qlda.project.entity.*;
import com.iist.qlda.project.repository.AppParamRepository;
import com.iist.qlda.project.repository.jparepository.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author dangnp
 * @created 24/07/2020 - 11:14 AM
 * @project services
 **/
@Service
public class MailServiceImpl implements MailService {
    private final Logger log = LogManager.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HumanResourcesRepository humanResourcesRepository;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration config;

    @Autowired
    private AttachDocumentRepository attachDocumentRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private AppParamRepository appParamRepository;

    private  MimeMessage message;
    private HumanResourcesEntity pm=new HumanResourcesEntity();
    private HumanResourcesEntity ba=new HumanResourcesEntity();
    private HumanResourcesEntity testLead=new HumanResourcesEntity();
    private HumanResourcesEntity qa=new HumanResourcesEntity();
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;


    //send mail add project
    @Async("asyncExecutor")
    @Override
    public void sendMailAddProject(ProjectDTO projectDTO) {
        PartnerEntity partnerEntity = partnerRepository.findById(projectDTO.getPartnerID()).get();
        AppParamEntity appParamEntity = appParamRepository.findById(partnerEntity.getPartnerID()).get();

        getEntity(projectDTO);
        ///put property
        Map<String, Object> models=map(projectDTO);
        //put tên đối tác
        models.put("namedoitac", appParamEntity.getName());
        //email to
        String[] emailToArr = new String[4];
        emailToArr[0] = pm.getEmail();
        emailToArr[1] = ba.getEmail();
        emailToArr[2] = testLead.getEmail();
        emailToArr[3] = qa.getEmail();
        //email cc
        List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
        String[] emailCcArr = new String[listMailBOD.size()];
        emailCcArr = listMailBOD.toArray(emailCcArr);
        log.info("---------->Send mail start!<----------");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setTo(emailToArr);
            helper.setCc(emailCcArr);
            helper.setSubject("Phân Công Quản Lý Dự Án: " + projectDTO.getCode() + "-" + projectDTO.getName());
            helper.setText(html, true);
            try {
                List<FileSystemResource> list=getFIle(projectDTO.getProjectId());
                long size = 0;
                for (FileSystemResource resource: list){
                    size += resource.getFile().length();
                    if(size>(25*1024*1024)){
                        break;
                    }
                    helper.addAttachment(Objects.requireNonNull(resource.getFilename()),resource);
                }
            }catch (NullPointerException e){
                javaMailSender.send(message);
                log.info("---------->Send mail add project success no file!<----------");
            }

            javaMailSender.send(message);
            log.info("---------->Send mail add project success!<----------");
        } catch (MessagingException | IOException | TemplateException exception) {
            exception.getMessage();
            log.info("---------->Send mail fail<----------", exception.getMessage());
        }
    }

    //get file
    private List<FileSystemResource> getFIle(long prodcutId) {
        List<AttachDocumentEntity> list = attachDocumentRepository.getList(prodcutId);
        List<FileSystemResource> file = new ArrayList<>();
        try {
            for (AttachDocumentEntity entity:list){
                String path=entity.getPath();
                FileSystemResource fileSystemResource = new FileSystemResource(new File(path));
                file.add(fileSystemResource);
            }
            return file;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //send mail ulnl sơ bộ
    @Override
    public void sendMailEstimatePrelimiinary(ProjectDTO projectDTO, Double estimatePrelimiinary, String username) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEntity(projectDTO);
        //thằng đang đăng nhập
        HumanResourcesEntity humanResourcesEntity = humanResourcesRepository.findByUsername(username);
        //put property
        Map<String, Object> models=map(projectDTO);
        //old ulnl sơ bộ
        if (estimatePrelimiinary == null){
            models.put("oldulnlsobo", 0);
        }else {
            models.put("oldulnlsobo", estimatePrelimiinary);
        }
        //put ulnl sơ bộ
        models.put("ulnlsobo", projectDTO.getEstimatePrelimiinary());
        //put username đang đăng nhập
        models.put("nameHeader", humanResourcesEntity.getLastName()+ " " + humanResourcesEntity.getFirstName());
        //email to
        String emailTo = qa.getEmail();
        //email cc
        List<String> listEmailCC = new ArrayList<>();
        List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
        for (IEmailBOD iEmailBOD: listMailBOD){
            listEmailCC.add(iEmailBOD.getEmail());
        }
        listEmailCC.add(humanResourcesEntity.getEmail());
        String [] emailCcArr = new String[listEmailCC.size()];
        emailCcArr = listEmailCC.toArray(emailCcArr);
        log.info("---------->Send mail EstimatePrelimiinary  start!<----------");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-ulnlsobo-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setTo(emailTo);
            helper.setCc(emailCcArr);
            helper.setSubject("Xác nhận ULNL Sơ Bộ Dự Án: " + projectDTO.getCode() + "-" + projectDTO.getName());
            helper.setText(html, true);
            javaMailSender.send(message);
            log.info("---------->Send mail EstimatePrelimiinary success!<----------");
        } catch (MessagingException | IOException | TemplateException exception) {
            exception.getMessage();
            log.info("---------->Send mail EstimatePrelimiinary fail<----------", exception.getMessage());
        }
    }

    //send mail ulnl nội bộ
    @Override
    public void sendMailEstimateInternal(ProjectDTO projectDTO, Double estimateInternal, String username) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEntity(projectDTO);
        //Thằng đang đăng nhập
        HumanResourcesEntity humanResourcesEntity = humanResourcesRepository.findByUsername(username);
        //put property
        Map<String, Object> models =map(projectDTO);
        //old ulnl nội bộ
        if (estimateInternal == null){
            models.put("oldulnlnoibo", 0);
        }else {
            models.put("oldulnlnoibo", estimateInternal);
        }
        //put ulnl nội bộ
        models.put("ulnlnoibo", projectDTO.getEstimateInternal());
        //put username đang đăng nhập
        models.put("nameHeader", humanResourcesEntity.getLastName()+ " " + humanResourcesEntity.getFirstName());
        //email to
        String emailTo = qa.getEmail();
        //email cc
        List<String> listEmailCC = new ArrayList<>();
        List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
        for (IEmailBOD iEmailBOD: listMailBOD){
            listEmailCC.add(iEmailBOD.getEmail());
        }
        listEmailCC.add(humanResourcesEntity.getEmail());
        String [] emailCcArr = new String[listEmailCC.size()];
        emailCcArr = listEmailCC.toArray(emailCcArr);
        log.info("---------->Send mail EstimateInternal  start!<----------");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-ulnlnoibo-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setTo(emailTo);
            helper.setCc(emailCcArr);
            helper.setSubject("Xác nhận ULNL Nội Bộ Dự Án: " + projectDTO.getCode() + "-" + projectDTO.getName());
            helper.setText(html, true);
            javaMailSender.send(message);
            log.info("---------->Send mail EstimateInternal success!<----------");
        } catch (MessagingException | IOException | TemplateException exception) {
            exception.getMessage();
            log.info("---------->Send mail EstimateInternal fail<----------", exception.getMessage());
        }
    }

    //send mai change pm/ba/qa/tl
    @Override
    public void sendMailChangeHumanProject(ProjectDTO projectDTO, List<IHumanResourceOfProject> oldHumans) {
        getEntity(projectDTO);
        //4 thăng cũ
        boolean pmChange=false;
        boolean baChange=false;
        boolean tlChange=false;
        boolean qaChange=false;
        List<String> emailTo = new ArrayList<>();
        List<String> emailCC = new ArrayList<>();
        Map<String, Object> models = new HashMap<>();
        for (IHumanResourceOfProject iHumanResourceOfProject: oldHumans){
            if (iHumanResourceOfProject.getRole().equals("PM") && !iHumanResourceOfProject.getId().equals(projectDTO.getPmId())){
                    pmChange=true;
                    models.put("pmNew",pm.getLastName()+" "+pm.getFirstName()+"( "+pm.getUsername()+" )" );
                    models.put("pmOld",iHumanResourceOfProject.getLastname()+" "+iHumanResourceOfProject.getFirstname()+"( "+iHumanResourceOfProject.getUsername()+" )");
            }
            models.put("pmChange",pmChange);
            if (iHumanResourceOfProject.getRole().equals("BM") && !iHumanResourceOfProject.getId().equals(projectDTO.getBaId())){
                    baChange=true;
                    models.put("baNew",ba.getLastName()+" "+ba.getFirstName()+"( "+ba.getUsername()+" )" );
                    models.put("baOld",iHumanResourceOfProject.getLastname()+" "+iHumanResourceOfProject.getFirstname()+"( "+iHumanResourceOfProject.getUsername()+" )");
            }
            models.put("baChange",baChange);
            if (iHumanResourceOfProject.getRole().equals("TL") && !iHumanResourceOfProject.getId().equals(projectDTO.getTestLeadId())){
                    tlChange=true;
                    models.put("tlNew",testLead.getLastName()+" "+testLead.getFirstName()+"( "+testLead.getUsername()+" )" );
                    models.put("tlOld",iHumanResourceOfProject.getLastname()+" "+iHumanResourceOfProject.getFirstname()+"( "+iHumanResourceOfProject.getUsername()+" )");
            }
            models.put("tlChange",tlChange);
            if (iHumanResourceOfProject.getRole().equals("QM")&& !iHumanResourceOfProject.getId().equals(projectDTO.getQaId())){
                    qaChange=true;
                    models.put("qaNew",qa.getLastName()+" "+qa.getFirstName()+"( "+qa.getUsername()+" )" );
                    models.put("qaOld",iHumanResourceOfProject.getLastname()+" "+iHumanResourceOfProject.getFirstname()+"( "+iHumanResourceOfProject.getUsername()+" )");

            }
            models.put("qaChange",qaChange);
        }
        if (!pmChange && !baChange && !tlChange && !qaChange ){
            return;
        }else {
            if(pmChange){
                emailTo.add(pm.getEmail());
            }else {
                emailCC.add(pm.getEmail());
            }
            if(baChange){
                emailTo.add(ba.getEmail());
            }else {
                emailCC.add(ba.getEmail());
            }
            if(tlChange){
                emailTo.add(testLead.getEmail());
            }else {
                emailCC.add(testLead.getEmail());
            }
            if(qaChange){
                emailTo.add(qa.getEmail());
            }else {
                emailCC.add(qa.getEmail());
            }
        }
        //email send
        //email bod
        List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
        for (IEmailBOD iEmailBOD: listMailBOD){
            emailCC.add(iEmailBOD.getEmail());
        }
        String[] emailToArr = new String[emailTo.size()];
        String[] emailCCArr = new String[emailCC.size()];
        //Convert list to array
        emailToArr = emailTo.toArray(emailToArr);
        emailCCArr = emailCC.toArray(emailCCArr);
        //vai trò quản lý dự án mới
        if (projectDTO.getCode() == null) {
            models.put("codeProject", "  ");
        } else {
            models.put("codeProject", projectDTO.getCode());
        }
        models.put("nameProject", projectDTO.getName());
        models.put("namePM", pm.getLastName() + " " + pm.getFirstName());
        models.put("nameBA", ba.getLastName() + " " + ba.getFirstName());
        models.put("nameTestLead", testLead.getLastName() + " " + testLead.getFirstName());
        models.put("nameQA", qa.getLastName() + " " + qa.getFirstName());
        log.info("---------->Send mail update human project start!<----------");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-change-pm-ba-tl-qa-project-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

            helper.setTo(emailToArr);
            helper.setCc(emailCCArr);
            helper.setSubject("Thay Đổi Nhân Sự Quản Lý Dự Án: " + projectDTO.getCode() + "-" + projectDTO.getName());
            helper.setText(html, true);
            javaMailSender.send(message);
            log.info("---------->Send mail update human project success!<----------");
        } catch (MessagingException | IOException | TemplateException exception) {
            exception.getMessage();
            log.info("---------->Send mail update human project fail<----------", exception.getMessage());
        }
    }

    //send mail add-product handover
    @Async("asyncExecutor")
    @Override
    public void sendMailAddProductHandover(List<ProductHandoverDTO> dtoList) {
        if (dtoList.isEmpty()){
            return;
        }
        for (ProductHandoverDTO productHandoverDTO:dtoList){
            if (productHandoverDTO.getProductHandoverId()==null){
                message = javaMailSender.createMimeMessage();
                Long projectID=dtoList.get(0).getProjectId();
                Map<String, Object> models = new HashMap<>();
                ProjectEntity project = projectRepository.findByProjectId(projectID);
                models.put("codeProject",project.getCode());
                models.put("nameProject",project.getName());
                //Email
                List<IProjectAndHumanMasterplan> projectMailDTOList = projectPlanRepository.getMailByProjectID(projectID);
                List<String> listEmailTo = new ArrayList<>();
                List<String> listEmailCC = new ArrayList<>();
                for(IProjectAndHumanMasterplan projectMailDTO : projectMailDTOList){
                    listEmailTo.add(projectMailDTO.getEmail());
                }
                List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
                for (IEmailBOD iEmailBOD:listMailBOD){
                    listEmailCC.add(iEmailBOD.getEmail());
                }
                String[] emailToArr = new String[listEmailTo.size()];
                String[] emailCcArr = new String[listEmailCC.size()];
                emailToArr = listEmailTo.toArray(emailToArr);
                emailCcArr = listEmailCC.toArray(emailCcArr);
                log.info("---------->Send mail add product handover start!<----------");
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                    Template template = config.getTemplate("email-product-handover-template.ftl");
                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

                    helper.setTo(emailToArr);
                    helper.setCc(emailCcArr);
                    helper.setSubject("Thông Báo Thêm Mới Sản Phẩm Bàn Giao Khách Hàng Dự Án: "+project.getCode()+"-"+ project.getName());
                    helper.setText(html, true);
                    javaMailSender.send(message);
                    log.info("---------->Send mail add product handover success!<----------");
                } catch (MessagingException | IOException | TemplateException exception) {
                    exception.getMessage();
                    log.info("---------->Send mail add product handover fail<----------", exception.getMessage());
                }
                break;
            }
        }
    }

    //send mail add-projet member
    @Async("asyncExecutor")
    @Override
    public void sendMailAddHumanResourceProject(ProjectDTO projectDTO, List<ProjectMemberDTO> listDTO){
        for (ProjectMemberDTO projectMemberDTO: listDTO){
            if (projectMemberDTO.getProjectMemberId()==null){
                message = javaMailSender.createMimeMessage();
                Map<String, Object> models = new HashMap<>();
                ProjectEntity project = projectRepository.findByProjectId(projectDTO.getProjectId());
                models.put("codeProject",project.getCode());
                models.put("nameProject",project.getName());
                //Email
                List<IProjectAndHumanMasterplan> projectMailDTOList = projectPlanRepository.getMailByProjectID(projectDTO.getProjectId());
                List<String> listEmailTo = new ArrayList<>();
                List<String> listEmailCC = new ArrayList<>();
                for(IProjectAndHumanMasterplan projectMailDTO : projectMailDTOList){
                    listEmailTo.add(projectMailDTO.getEmail());
                }
                List<IEmailBOD> listMailBOD = projectPlanRepository.getEmailBOD();
                for (IEmailBOD iEmailBOD:listMailBOD){
                    listEmailCC.add(iEmailBOD.getEmail());
                }
                String[] emailToArr = new String[listEmailTo.size()];
                String[] emailCcArr = new String[listEmailCC.size()];
                emailToArr = listEmailTo.toArray(emailToArr);
                emailCcArr = listEmailCC.toArray(emailCcArr);
                log.info("---------->Send mail add project member start!<----------");
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                    Template template = config.getTemplate("email-add-humanresource-project-template.ftl");
                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

                    helper.setTo(emailToArr);
                    helper.setCc(emailCcArr);
                    helper.setSubject("Kế Hoạch Sử Dụng Nhân Sự Dự Án: "+project.getCode()+"-"+ project.getName());
                    helper.setText(html, true);
                    javaMailSender.send(message);
                    log.info("---------->Send mail add project member success!<----------");
                } catch (MessagingException | IOException | TemplateException exception) {
                    exception.getMessage();
                    log.info("---------->Send mail add project member fail<----------", exception.getMessage());
                }
                break;
            }
        }
    }

    //hàm chung
    private Map<String,Object> map(ProjectDTO projectDTO){
        Map<String, Object> models = new HashMap<>();
        if (projectDTO.getCode() == null) {
            models.put("codeProject", "  ");
        } else {
            models.put("codeProject", projectDTO.getCode());
        }
        models.put("nameProject", projectDTO.getName());
        //name
        models.put("namePM", pm.getLastName() + " " + pm.getFirstName());
        models.put("nameBA", ba.getLastName() + " " + ba.getFirstName());
        models.put("nameTestLead", testLead.getLastName() + " " + testLead.getFirstName());
        models.put("nameQA", qa.getLastName() + " " + qa.getFirstName());
        return models;
     }

     //hàm chung
     private void getEntity(ProjectDTO projectDTO){
        message = javaMailSender.createMimeMessage();
        pm = humanResourcesRepository.findById(projectDTO.getPmId()).get();
        ba = humanResourcesRepository.findById(projectDTO.getBaId()).get();
        testLead = humanResourcesRepository.findById(projectDTO.getTestLeadId()).get();
        qa = humanResourcesRepository.findById(projectDTO.getQaId()).get();
     }

     //hàm này để chạy luồng riêng
     @Async("asyncExecutor")
     @Override
     public void sendMailULNLOrChangeHuman(ProjectDTO dto, Double estimatePrelimiinary, Double estimateInternal, String username, List<IHumanResourceOfProject> oldHumans){
        sendMailChangeHumanProject(dto, oldHumans);
        if(dto.getEstimatePrelimiinary() != null){
             if((!dto.getEstimatePrelimiinary().equals(estimatePrelimiinary))){
                 sendMailEstimatePrelimiinary(dto, estimatePrelimiinary,username);
             }
         }
         if (dto.getEstimateInternal() != null) {
             if ((!dto.getEstimateInternal().equals(estimateInternal))) {
                 sendMailEstimateInternal(dto,estimateInternal, username);
             }
         }
     }

}

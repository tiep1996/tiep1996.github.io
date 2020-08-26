package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.IProjectMailDTO;
import com.iist.qlda.project.entity.ProjectPlanEntity;
import com.iist.qlda.project.repository.jparepository.HumanResourcesRepository;
import com.iist.qlda.project.repository.jparepository.ProjectPlanRepository;
import common.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dangnpp
 * @created 01/08/2020 - 10:06 AM
 * @project services
 **/
@Service
public class MailServiceScheduled {
    private final Logger log = LogManager.getLogger(MailServiceScheduled.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration config;

    @Autowired
    private HumanResourcesRepository humanResourcesRepository;

    @Autowired
    private ProjectPlanRepository projectPlanRepository;

    @Scheduled(cron = "${autoSendMail}")
    public void sendMailAutoScheduled() {
        List<ProjectPlanEntity> list = projectPlanRepository.projectPlanAutoMail();
        if (!list.isEmpty()) {
            MimeMessage message = javaMailSender.createMimeMessage();
            Map<String, Object> models = new HashMap<>();
            for (ProjectPlanEntity e : list) {
                List<IProjectMailDTO> dtos = projectPlanRepository.getProjectAndHumanResource(e.getProjectPlanId());
                if (dtos.size() != 0) {
                    for (IProjectMailDTO dto : dtos) {
                        //put code, name project
                        if(dto.getCode()==null){
                            models.put("codeProject", " ");
                        }else {
                            models.put("codeProject", dto.getCode());
                        }
                        models.put("nameProject", dto.getName());
                        //put module/chức năng, người thực hiện plan
                        if (e.getModule()==null){
                            models.put("module", " ");
                        }else {
                            models.put("module", e.getModule());
                        }
                        models.put("nameAdmin", humanResourcesRepository.findById(e.getHumanResourceId()).get().getUsername());
                        //put deadline project
                        if (e.getDeadline()==null){
                            models.put("deadline", " ");
                        }else {
                            models.put("deadline", DateUtils.formatDateTwo(e.getDeadline()));
                        }
                        try {
                            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                            Template template = config.getTemplate("email-scheduled-deadline.ftl");
                            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, models);

                            helper.setTo(humanResourcesRepository.findById(e.getHumanResourceId()).get().getEmail());
                            helper.setCc(dto.getEmail());
                            helper.setSubject("Thông Báo Thời Gian Deadline Module: " + e.getModule() + ", Dự Án: " + dto.getCode() + "-" + dto.getName());
                            helper.setText(html, true);
                            javaMailSender.send(message);
                            log.info("<------------------Send mail auto success-------------->");
                        } catch (MessagingException | IOException | TemplateException exception) {
                            exception.getMessage();
                            log.info("<-----------------------send mail error-------", exception.getMessage());
                        }
                    }
                }

            }
        }
    }

}

package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.IHumanResourceOfProject;
import com.iist.qlda.project.dto.ProductHandoverDTO;
import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import com.iist.qlda.project.entity.ProductHandoverEntity;
import com.iist.qlda.project.entity.ProjectMemberEntity;

import java.util.List;
import java.util.Map;


/**
 * @author dangnp
 * @created 24/07/2020 - 11:14 AM
 * @project services
 **/
public interface MailService {
    void sendMailAddProject(ProjectDTO projectDTO);

    void sendMailEstimatePrelimiinary(ProjectDTO projectDTO, Double estimatePrelimiinary, String username);

    void sendMailEstimateInternal(ProjectDTO projectDTO, Double estimateInternal, String username);

    //hàm send mail khi thay đổi pm, ba, tl, qa
    void sendMailChangeHumanProject(ProjectDTO projectDTO, List<IHumanResourceOfProject> oldHumans);

    //hàm chạy luồng riêng ulnl
    void sendMailULNLOrChangeHuman(ProjectDTO dto, Double estimatePrelimiinary, Double estimateInternal, String username, List<IHumanResourceOfProject> oldHumans);

    void sendMailAddProductHandover(List<ProductHandoverDTO> dtoList);

    void sendMailAddHumanResourceProject(ProjectDTO projectDTO, List<ProjectMemberDTO> listDTO);

//    void sendMailCustomer(Map<String, Object> data, String[] toMail, String[] ccMail, String template, String subject);
}

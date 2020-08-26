package com.iist.qlda.project.service;

import java.util.Date;
import java.util.List;

import com.iist.qlda.project.entity.ProjectEntity;
import com.iist.qlda.project.repository.jparepository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.iist.qlda.project.dto.MasterPlanDTO;
import com.iist.qlda.project.dto.ProjectPlanDTO;
import com.iist.qlda.project.entity.MasterPlanEntity;
import com.iist.qlda.project.entity.ProjectPlanEntity;
import com.iist.qlda.project.repository.customreporsitory.MasterPlanCustomRepository;
import com.iist.qlda.project.repository.jparepository.MasterPlanRepository;
import com.iist.qlda.project.repository.jparepository.ProjectPlanRepository;
import com.iist.qlda.project.repository.jparepository.ProjectRepository;

@Service
public class MasterPlanServiceImpl implements MasterPlanService {

    @Autowired
    private MasterPlanRepository masterPlanRepository;
    @Autowired
    private MasterPlanCustomRepository masterPlanCustomRepository;
    @Autowired
    private ProjectPlanRepository projectPlanRepository;
    @Autowired
    private ProjectRepository proRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;


    @Override
    public MasterPlanDTO findAllByProjectId(Long i) {
        MasterPlanEntity entity = masterPlanRepository.findAllByProjectId(i);
        ProjectEntity projectEntity = proRepository.findById(i).get();

        MasterPlanDTO dto = new MasterPlanDTO();
        if (entity != null) {
            dto.setMasterPlanId(entity.getMasterPlanId());
            dto.setProjectId(entity.getProjectId());
            dto.setName(entity.getName());
            dto.setActualDateDelivery(entity.getActualDateDelivery());
            dto.setDateSendingPlan(entity.getDateSendingPlan());
            dto.setDateDeliveryKBKT(entity.getDateDeliveryKBKT());
            dto.setEndDate(entity.getEndDate());
            dto.setDateDemo(entity.getDateDemo());
            dto.setLstprojectPlanDTO(masterPlanCustomRepository.getListProjectPlanDTO(i));
            dto.setStatusBA(entity.getStatusBA());
            dto.setNoteBA(entity.getNoteBA());
            dto.setReasonBA(entity.getReasonBA());
            dto.setStatusTest(entity.getStatusTest());
            dto.setNoteTest(entity.getNoteTest());
            dto.setReasonTest(entity.getReasonTest());
            dto.setStatusPM(entity.getStatusPM());
            dto.setNotePM(entity.getNotePM());
            dto.setReasonPM(entity.getReasonPM());
            dto.setStatusQA(entity.getStatusQA());
            dto.setNoteQA(entity.getNoteQA());
            dto.setReasonQA(entity.getReasonQA());
            dto.setCreateDate(entity.getCreateDate());
            dto.setCreateBy(entity.getCreateBy());
            dto.setIsBA(projectEntity.getBA_MAN());
            dto.setCode(proRepository.findByProjectId(i).getCode());
        } else {
            dto.setProjectId(i);
            dto.setIsBA(projectEntity.getBA_MAN());
            dto.setCode(proRepository.findByProjectId(i).getCode());
            dto.setName(proRepository.findNameByProjectId(i).getName());
            dto.setLstprojectPlanDTO(masterPlanCustomRepository.getListProjectPlanDTO(i));
        }
        return dto;
    }

    @Override
    public void saveProject(MasterPlanDTO dto) {
        MasterPlanEntity check = masterPlanRepository.findAllByProjectId(dto.getProjectId());
        MasterPlanDTO mpdto = new MasterPlanDTO();

        if (check != null) {
            /*dangnp- 05/08*/
//            mpdto.setStatusBA(check.getStatusBA());
//            mpdto.setStatusPM(check.getStatusPM());
//            mpdto.setStatusQA(check.getStatusQA());
//            mpdto.setStatusTest(check.getStatusTest());
            /*end- dangnp*/
            MasterPlanEntity entity = new MasterPlanEntity();
            entity.setMasterPlanId(check.getMasterPlanId());
            entity.setProjectId(dto.getProjectId());
            entity.setName(dto.getName());
            entity.setEndDate(dto.getEndDate());
            entity.setDateSendingPlan(dto.getDateSendingPlan());
            entity.setActualDateDelivery(dto.getActualDateDelivery());
            entity.setDateDeliveryKBKT(dto.getDateDeliveryKBKT());
            entity.setDateDemo(dto.getDateDemo());
            String role = projectMemberRepository.findByProjectIdAndHumanResourceId(dto.getProjectId(), dto.getHumanId()).get(0).getRole();
            if (role.equals("BM")&&dto.getLstprojectPlanDTO().size()!=0) {
                entity.setStatusBA(1);
            } else {
                entity.setStatusBA(0);
                entity.setNoteBA(null);
                entity.setReasonBA(null);
            }
            if (role.equals("TL")&&dto.getLstprojectPlanDTO().size()!=0) {
                entity.setStatusTest(1);
            } else {
                entity.setStatusTest(0);
                entity.setNoteTest(null);
                entity.setReasonTest(null);
            }
            if (role.equals("PM")&&dto.getLstprojectPlanDTO().size()!=0) {
                entity.setStatusPM(1);
            } else {
                entity.setStatusPM(0);
                entity.setNotePM(null);
                entity.setReasonPM(null);
            }
            entity.setStatusQA(0);
            entity.setNoteQA(null);
            entity.setReasonQA(null);
            entity.setCreateDate(dto.getCreateDate());
            entity.setCreateBy(dto.getCreateBy());
            entity.setUpdateDate(new Date());
            entity.setUpdateBy(dto.getHumanId());
            masterPlanRepository.save(entity);
//			if(projectPlanRepository.findProjectPlanIdByMasterPlanId(check.getMasterPlanId())!= null) {
//				ProjectPlanEntity projectPlanEntity = new ProjectPlanEntity();
            for (ProjectPlanDTO proDto : dto.getLstprojectPlanDTO()) {
                ProjectPlanEntity projectPlanEntity = new ProjectPlanEntity();
                projectPlanEntity.setProjectPlanId(proDto.getProjectPlanId());
                projectPlanEntity.setMasterPlanId(check.getMasterPlanId());
                projectPlanEntity.setMileStone(proDto.getMileStone().trim());
                projectPlanEntity.setDescription(proDto.getDescription().trim());
                projectPlanEntity.setDeadline(proDto.getDeadline());
                projectPlanEntity.setModule(proDto.getModule().trim());
                projectPlanEntity.setEndDate(proDto.getEndDate());
                projectPlanEntity.setStartDate(proDto.getStartDate());
                projectPlanEntity.setHumanResourceId(proDto.getHumanResourceId());
                projectPlanRepository.save(projectPlanEntity);
            }

//			}

        } else {
            MasterPlanEntity entity = new MasterPlanEntity();
            entity.setProjectId(dto.getProjectId());
            entity.setName(dto.getName());
            entity.setEndDate(dto.getEndDate());
            entity.setDateSendingPlan(dto.getDateSendingPlan());
            entity.setActualDateDelivery(dto.getActualDateDelivery());
            entity.setDateDeliveryKBKT(dto.getDateDeliveryKBKT());
            entity.setDateDemo(dto.getDateDemo());
            String role = projectMemberRepository.findByProjectIdAndHumanResourceId(dto.getProjectId(), dto.getHumanId()).get(0).getRole();
            if (role.equals("BM")&&dto.getLstprojectPlanDTO().size()!=0) {
                entity.setStatusBA(1);
            } else {
                entity.setStatusBA(0);
                entity.setNoteBA(null);
                entity.setReasonBA(null);
            }
            if (role.equals("TL")&&dto.getLstprojectPlanDTO().size()!=0) {
                entity.setStatusTest(1);
            } else {
                entity.setStatusTest(0);
                entity.setNoteTest(null);
                entity.setReasonTest(null);
            }
            if (role.equals("PM")&&dto.getLstprojectPlanDTO().size()!=0) {
                entity.setStatusPM(1);
            } else {
                entity.setStatusPM(0);
                entity.setNotePM(null);
                entity.setReasonPM(null);
            }
            entity.setStatusQA(0);
            entity.setNoteQA(null);
            entity.setReasonQA(null);
            entity.setCreateDate(new Date());
            entity.setCreateBy(dto.getHumanId());
            masterPlanRepository.save(entity);
            long id = masterPlanRepository.findAllByProjectId(dto.getProjectId()).getMasterPlanId();
            for (ProjectPlanDTO proDto : dto.getLstprojectPlanDTO()) {
                ProjectPlanEntity projectPlanEntity = new ProjectPlanEntity();
                projectPlanEntity.setProjectPlanId(proDto.getProjectPlanId());
                projectPlanEntity.setMasterPlanId(id);
                projectPlanEntity.setMileStone(proDto.getMileStone().trim());
                projectPlanEntity.setDescription(proDto.getDescription().trim());
                projectPlanEntity.setDeadline(proDto.getDeadline());
                projectPlanEntity.setModule(proDto.getModule().trim());
                projectPlanEntity.setStartDate(proDto.getStartDate());
                projectPlanEntity.setEndDate(proDto.getEndDate());
                projectPlanEntity.setHumanResourceId(proDto.getHumanResourceId());
                projectPlanRepository.save(projectPlanEntity);
            }
        }

    }

    @Override
    public void confirmProject(MasterPlanDTO dto) {
        MasterPlanEntity check = masterPlanRepository.findAllByProjectId(dto.getProjectId());
        MasterPlanEntity entity = new MasterPlanEntity();
        entity.setMasterPlanId(check.getMasterPlanId());
        entity.setProjectId(dto.getProjectId());
        entity.setName(dto.getName());
        entity.setEndDate(dto.getEndDate());
        entity.setDateSendingPlan(dto.getDateSendingPlan());
        entity.setActualDateDelivery(dto.getActualDateDelivery());
        entity.setDateDeliveryKBKT(dto.getDateDeliveryKBKT());
        entity.setDateDemo(dto.getDateDemo());
        entity.setStatusBA(dto.getStatusBA());
        entity.setNoteBA(dto.getNoteBA());
        entity.setReasonBA(dto.getReasonBA());
        entity.setStatusTest(dto.getStatusTest());
        entity.setNoteTest(dto.getNoteTest());
        entity.setReasonTest(dto.getReasonTest());
        entity.setStatusPM(dto.getStatusPM());
        entity.setNotePM(dto.getNotePM());
        entity.setReasonPM(dto.getReasonPM());
        entity.setStatusQA(dto.getStatusQA());
        entity.setNoteQA(dto.getNoteQA());
        entity.setReasonQA(dto.getReasonQA());
        masterPlanRepository.save(entity);
    }

    @Override
    public MasterPlanDTO editProject(MasterPlanDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }

}

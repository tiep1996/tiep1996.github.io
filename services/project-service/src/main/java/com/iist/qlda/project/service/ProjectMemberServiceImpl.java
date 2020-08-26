package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.DataPage;
import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import com.iist.qlda.project.entity.ProjectEntity;
import com.iist.qlda.project.entity.ProjectMemberEntity;
import com.iist.qlda.project.repository.customreporsitory.ProjectMemberCustomRepository;
import com.iist.qlda.project.repository.jparepository.ProjectMemberRepository;
import com.iist.qlda.project.repository.jparepository.ProjectRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//AnhTT_IIST
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final Logger log = LogManager.getLogger(ProjectMemberServiceImpl.class);
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectMemberCustomRepository customRepository;

    @Autowired
    private ProjectRepository projectRepository;

    //AnhTT_IIST luu thanh vien vao du an
    @Override
    public ProjectDTO saveProjectMember(ProjectDTO projectDTO) {
        //luu cac thanh vien duoc them vao du an
        List<ProjectMemberDTO> lstProjectMember = projectDTO.getLstProjectMember();

        //luu cac thanh vien break khoi du an
        List<ProjectMemberDTO> lstProjectMemberDelete = projectDTO.getLstProjectMemberDelete();

        //them hoac sua thanh vien trong du an
        log.info("------------------them hoac sua thanh vien trong du an--------------------");
        if (CollectionUtils.isNotEmpty(lstProjectMember)) {
            for (ProjectMemberDTO projectMemberDTO : lstProjectMember) {
                ProjectMemberEntity projectMemberEntity = new ProjectMemberEntity();
                //check xem thanh vien da co trong du an chua va co roi thi co trang thi la gi
                Long check1 = (customRepository.checkMemberProject(projectDTO.getProjectId(), projectMemberDTO.getHumanResourceId(),1)).longValue();
              //  Long check2 = customRepository.checkMemberProject(projectDTO.getProjectId(), projectMemberDTO.getHumanResourceId(),0);

                //anhtt_iist them moi thanh vien trong turong hop thqanh vien do chua co trong du an hoac da tung break khoi du an
                log.info("---------them moi thanh vien du an------------");
                if (projectMemberDTO.getProjectMemberId() == null) {
                    projectMemberEntity.setProjectId(projectDTO.getProjectId());
                    projectMemberEntity.setHumanResourceId(projectMemberDTO.getHumanResourceId());
                    projectMemberEntity.setDepartment(projectMemberDTO.getDepartment());
                    projectMemberEntity.setDateJoin(projectMemberDTO.getDateJoin());
                    projectMemberEntity.setDateOut(projectMemberDTO.getDateOut());
                    projectMemberEntity.setResources(projectMemberDTO.getResources());
                    projectMemberEntity.setRole("Member");

                    //lay ra lan gan nhat thanh vien duoc join vao du an
                    Integer maxNojoin = customRepository.getMaxNoJoin(projectDTO.getProjectId(), projectMemberDTO.getHumanResourceId());

                    
                    projectMemberEntity.setNoJoin(maxNojoin + 1);
                    projectMemberEntity.setIsActive(1);
                    
                    projectMemberEntity.setKPI((customRepository.countDateInProject( projectMemberDTO.getDateJoin(), projectMemberDTO.getDateOut()))*(projectMemberDTO.getResources()/100.0));

                    projectMemberRepository.save(projectMemberEntity);
                }
                //sua thong tin nhan su trong du an, chi duoc sua ngay ket thuc
        
                else if (projectMemberDTO.getProjectMemberId() != null) {
                    log.info("---------sua thanh vien du an------------");
                    ProjectMemberEntity projectMemberEntity1 = projectMemberRepository.findByProjectMemberId(projectMemberDTO.getProjectMemberId());
                    projectMemberEntity1.setProjectMemberId(projectMemberDTO.getProjectMemberId());
                    projectMemberEntity1.setDateOut(projectMemberDTO.getDateOut());
                    //neu sua ngay ket thuc tinh kpi tu thoi diemm sua - ngay join du an

                    projectMemberEntity1.setKPI((customRepository.countDateInProject(projectMemberDTO.getDateJoin(), projectMemberDTO.getDateOut())) * (projectMemberDTO.getResources() / 100.0));

//                    if(projectMemberDTO.getResources() != projectMemberEntity1.getResources()){
//                        projectMemberEntity1.setResources(projectMemberDTO.getResources());
//                        //neu sua %nguon luc tinh kpi tu thoi diem sua den ngay out du an voi % nguon luc moi nhap vao (va co hieu luc tu ngay hom sau)
//                        projectMemberEntity1.setKPI(projectMemberEntity1.getKPI()+((customRepository.countDateInProject(new Date(), projectMemberDTO.getDateOut()))-1)*(projectMemberDTO.getResources()/100.0));
//                    }
                    projectMemberEntity1.setResources(projectMemberDTO.getResources());
                    projectMemberRepository.save(projectMemberEntity1);
                }

                //anhtt_iist cap nhat kpi cua du an
                log.info("----------cap nhat kpi cua du an-----------------");
                ProjectEntity projectEntity = projectRepository.findByProjectId(projectDTO.getProjectId());
                if (projectEntity != null) {
                    projectEntity.setProjectId(projectDTO.getProjectId());
                    projectEntity.setEstimateActual(customRepository.countKpiProject(projectDTO.getProjectId()));
                    projectRepository.save(projectEntity);
                }
            }
        }

        //anhtt_iist xoa thanh vien khoi du an
        if(CollectionUtils.isNotEmpty(lstProjectMemberDelete)){
            log.info("------------------xoa nhan su cua du an-------------------");
            for(ProjectMemberDTO memberDTO : lstProjectMemberDelete){
                if(memberDTO.getProjectMemberId() != null){
                    //cap nhat lai isActive =0 va ngay break du an
                    ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProjectMemberId(memberDTO.getProjectMemberId());
                    projectMemberEntity.setProjectMemberId(memberDTO.getProjectMemberId());
                    projectMemberEntity.setDateOut(new Date());
                    projectMemberEntity.setIsActive(0);
                    projectMemberEntity.setKPI((customRepository.countDateInProject( memberDTO.getDateJoin(), new Date()))*(memberDTO.getResources()/100.0));
                    projectMemberRepository.save(projectMemberEntity);
                }
            }
        }

        return projectDTO;
    }


    //anhtt_iist get danh sach nhan su du an
    @Override
    public DataPage<ProjectMemberDTO> getListMemberProject(ProjectDTO dto) {
        log.info("-------------danh sach nhan su cua du an----------------");
        DataPage<ProjectMemberDTO> data = new DataPage<>();
//        dto.setPage(dto.getPage() != null ? dto.getPage() : 1);
//        dto.setPageSize(dto.getPageSize() != null ? dto.getPageSize() : 10);
        List<ProjectMemberDTO> lst = customRepository.getListMemberProject(dto);
        if (CollectionUtils.isNotEmpty(lst)) {
            for (ProjectMemberDTO memberDTO : lst) {
                memberDTO.setResourcesUsed((customRepository.countResourceOfMember(memberDTO.getHumanResourceId())).doubleValue());
            }
            data.setData(lst);
        } else {
            data.setData(new ArrayList<ProjectMemberDTO>());
        }

//        data.setPageSize(dto.getPageSize());
//        data.setPageIndex(dto.getPage());
//        data.setDataCount(dto.getTotalRecord());
//        data.setPageCount(dto.getTotalRecord() / dto.getPageSize());
//        if (dto.getTotalRecord() % dto.getPageSize() != 0) {
//            data.setPageCount(data.getPageCount() + 1);
//        }

        return data;
    }


    //nuctv 10/08
    @Override
    public List<ProjectMemberEntity> findByProjectIDAndHumanID(Long proID, Long humanID) {
        return projectMemberRepository.findByProjectIdAndHumanResourceId(proID, humanID);
    }

}

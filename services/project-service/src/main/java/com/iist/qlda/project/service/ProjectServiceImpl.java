package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.AttachDocumentDTO;
import com.iist.qlda.project.dto.DataPage;
import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import com.iist.qlda.project.entity.*;
import com.iist.qlda.project.repository.customreporsitory.ProjectCustomRepository;
import com.iist.qlda.project.repository.customreporsitory.ProjectMemberCustomRepository;
import com.iist.qlda.project.repository.jparepository.*;
import common.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

//ANHTT_IIST
@Service
@EnableAsync
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private MailService mailService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectCustomRepository projectCustomRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private AttachDocumentRepository documentRepository;

    @Autowired
    private ProjectMemberCustomRepository memberCustomRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private AttachDocumentRepository attachDocumentRepository;

    @Autowired
    private AttachDocumentService attachDocumentService;

    @Autowired
    private MasterPlanRepository masterPlanRepository;

    private final Path root = Paths.get("uploads");


    private final Logger log = LogManager.getLogger(ProjectServiceImpl.class);

    //ANHTT_IIST LUU THONG TIN DU AN
    @Override
    public ProjectDTO saveProject(ProjectDTO dto, MultipartFile[] lstAttachFile) {
        //LUU THONG TIN DOI TAC
        log.info("-----------------luu thong tin doi tac---------------");
        PartnerEntity partnerEntity = new PartnerEntity();
        partnerEntity.setPartnerID(dto.getPartnerID());
        partnerEntity.setCustomerPmName(dto.getCustomerPmName());
        partnerEntity.setCustomerEmail(dto.getCustomerEmail());
        partnerEntity.setAmName(dto.getAmName());
        partnerEntity.setAmEmail(dto.getAmEmail());
        partnerEntity.setAmPhone(dto.getAmPhone());
        partnerEntity.setCustomerPmPhone(dto.getCustomerPmPhone());
        partnerEntity.setIsActive(1);
        log.info("-----------------save partner info--------------------");
        PartnerEntity newPartner = partnerRepository.save(partnerEntity);

        //LUU THONG TIN DU AN
        log.info("-----------------luu thong tin du an---------------");
        ProjectEntity entity = new ProjectEntity();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setBa(dto.getBa());
        //entity.setPM_MAN(dto.getPmMan());
        entity.setDev(dto.getDev());
        entity.setTest(dto.getTest());
        entity.setBA_MAN(dto.getBaMan());
//        try {
//            entity.setStartDate(new SimpleDateFormat("yyyy/MM/dd").parse(dto.getStartDate1()));
//            entity.setEndDate(new SimpleDateFormat("yyyy/MM/dd").parse(dto.getEndDate1()));
//        }catch (Exception e){
//
//        }
        entity.setStatusOverview(14);
        entity.setStatusPayment(19);

        entity.setCreateDate(new Date());
        entity.setUpdateDate(new Date());
        entity.setIsActive(1);
        entity.setDescription(dto.getDescription());
        entity.setPartnerId(newPartner.getId());
        entity.setBa(dto.getBa());
        //entity.setPM_MAN(dto.getPmMan());
        entity.setDev(dto.getDev());
        entity.setTest(dto.getTest());
        entity.setCreateBy(dto.getHumanResourcesId());
        entity.setStatusPreliinary(0);
        entity.setStatusLatch(0);
        entity.setStatusOffer(0);
        entity.setStatusInternal(0);
        log.info("-----------------save project info--------------------");
        //dangnp sua
        ProjectEntity entity1 = projectRepository.save(entity);

        //LUU THONG TIN THANH VIEN DU AN
        log.info("-----------------luu thong tin thanh vien du an---------------");

        ProjectMemberEntity projectMemberEntityPm = new ProjectMemberEntity();
        projectMemberEntityPm.setProjectId(entity.getProjectId());
        projectMemberEntityPm.setHumanResourceId(dto.getPmId());
        projectMemberEntityPm.setRole("PM");
        projectMemberEntityPm.setDepartment("Dev");
        projectMemberEntityPm.setDateJoin(new Date());
        projectMemberEntityPm.setDateOut(new Date());
        projectMemberEntityPm.setIsActive(1);
        projectMemberEntityPm.setResources(0);
        projectMemberEntityPm.setNoJoin(memberCustomRepository.getMaxNoJoin(dto.getPmId(), entity.getProjectId()) + 1);
        log.info("-----------------save pm member--------------------");
        projectMemberRepository.save(projectMemberEntityPm);

        ProjectMemberEntity projectMemberEntityBa = new ProjectMemberEntity();
        projectMemberEntityBa.setProjectId(entity.getProjectId());
        projectMemberEntityBa.setHumanResourceId(dto.getBaId());
        projectMemberEntityBa.setRole("BM");
        projectMemberEntityBa.setDepartment("BA");
        projectMemberEntityBa.setDateJoin(new Date());
        projectMemberEntityBa.setDateOut(new Date());
        projectMemberEntityBa.setIsActive(1);
        projectMemberEntityBa.setResources(0);
        projectMemberEntityBa.setNoJoin(memberCustomRepository.getMaxNoJoin(dto.getBaId(), entity.getProjectId()) + 1);
        log.info("-----------------save ba member--------------------");
        projectMemberRepository.save(projectMemberEntityBa);

        ProjectMemberEntity projectMemberEntityTest = new ProjectMemberEntity();
        projectMemberEntityTest.setProjectId(entity.getProjectId());
        projectMemberEntityTest.setHumanResourceId(dto.getTestLeadId());
        projectMemberEntityTest.setRole("TL");
        projectMemberEntityTest.setDepartment("Tester");
        projectMemberEntityTest.setDateJoin(new Date());
        projectMemberEntityTest.setDateOut(new Date());
        projectMemberEntityTest.setIsActive(1);
        projectMemberEntityTest.setResources(0);
        projectMemberEntityTest.setNoJoin(memberCustomRepository.getMaxNoJoin(dto.getTestLeadId(), entity.getProjectId()) + 1);
        log.info("-----------------save test lead member--------------------");
        projectMemberRepository.save(projectMemberEntityTest);

        ProjectMemberEntity projectMemberEntityQA = new ProjectMemberEntity();
        projectMemberEntityQA.setProjectId(entity.getProjectId());
        projectMemberEntityQA.setHumanResourceId(dto.getQaId());
        projectMemberEntityQA.setRole("QM");
        projectMemberEntityQA.setDepartment("QA");
        projectMemberEntityQA.setIsActive(1);
        projectMemberEntityQA.setResources(0);
        projectMemberEntityQA.setDateOut(new Date());
        projectMemberEntityQA.setDateJoin(new Date());
        projectMemberEntityQA.setNoJoin(memberCustomRepository.getMaxNoJoin(dto.getQaId(), entity.getProjectId()) + 1);
        log.info("-----------------save qa member--------------------");
        projectMemberRepository.save(projectMemberEntityQA);

        log.info("-----------------luu thong tin cac tep dinh kem cua du an---------------");

//        List<AttachDocumentDTO> listAttDocument = dto.getLstAttachDocument();
//
//        if (CollectionUtils.isNotEmpty(listAttDocument)) {
//            for (AttachDocumentDTO attachDocumentDTO : listAttDocument) {
//                AttachDocumentEntity documentEntity = new AttachDocumentEntity();
//                documentEntity.setCode(attachDocumentDTO.getCode());
//                documentEntity.setName(attachDocumentDTO.getName());
//                documentEntity.setPath(attachDocumentDTO.getPath());
//                documentEntity.setType(1);
//                documentEntity.setIsActive(1);
//                documentEntity.setParentId(entity.getProjectId());
//                documentEntity.setCreateDate(new Date());
//                documentEntity.setUpdateDate(new Date());
//                log.info("-----------------save attachDocument file--------------------");
//                documentRepository.save(documentEntity);
//            }
//        }
        if(lstAttachFile.length > 0 ){
            attachDocumentService.uploadFile(entity1.getCode(),  entity1.getProjectId(), entity1.getCreateBy(),  lstAttachFile);
        }

        //send mail start
        dto.setProjectId(entity1.getProjectId());
//        mailService.sendMailAddProject(dto);
        dto.setPartnerID(entity1.getPartnerId());
        return dto;
    }


    //MANHNX_IIST SUA THONG TIN DU AN
    @Override
    public ProjectDTO editProject(ProjectDTO dto, MultipartFile[] lstAttachFile) {
        //LUU THONG TIN DOI TAC
        log.info("-----------------luu thong tin doi tac---------------");
        PartnerEntity partnerEntity = new PartnerEntity();
        partnerEntity.setPartnerID(dto.getPartnerID());
        partnerEntity.setCustomerPmName(dto.getCustomerPmName());
        partnerEntity.setCustomerEmail(dto.getCustomerEmail());
        partnerEntity.setAmName(dto.getAmName());
        partnerEntity.setAmEmail(dto.getAmEmail());
        partnerEntity.setAmPhone(dto.getAmPhone());
        partnerEntity.setCustomerPmPhone(dto.getCustomerPmPhone());

        PartnerEntity newPartner = partnerRepository.save(partnerEntity);

        //LUU THONG TIN DU AN
        log.info("-----------------luu thong tin du an---------------");
        ProjectEntity entity = projectRepository.findByProjectId(dto.getProjectId());
        entity.setName(dto.getName());
        entity.setStatusOverview(dto.getStatusOverview());
        entity.setStatusDetail(dto.getStatusDetail());
        entity.setStatusPayment(dto.getStatusPayment());
        entity.setEstimatePrelimiinary(dto.getEstimatePrelimiinary());// uoc tinh tong quan
        //entity.setEstimateActual(dto.getEstimateActual()); // udc tinh no luc thuc te
        entity.setEstimateLatch(dto.getEstimateLatch()); //chot
        entity.setEstimateOffer(dto.getEstimateOffer()); //chao gia
        entity.setEstimateInternal(dto.getEstimateInternal());
        entity.setMonth(dto.getMonth());
        entity.setDateExpected(dto.getDateExpected());
        entity.setBa(dto.getBa());
        entity.setDev(dto.getDev());
        entity.setTest(dto.getTest());
        entity.setBA_MAN(dto.getBaMan());
        try {
            entity.setStartDate(new SimpleDateFormat("yyyy/MM/dd").parse(dto.getStartDate()));
            entity.setEndDate(new SimpleDateFormat("yyyy/MM/dd").parse(dto.getEndDate()));
        } catch (Exception e) {

        }
        entity.setCreateDate(new Date());
        entity.setUpdateDate(new Date());
        entity.setIsActive(1);
        entity.setDescription(dto.getDescription());
        entity.setPartnerId(newPartner.getId());
//        entity.setCode("ghkjhkhjkhjk");
        ProjectEntity entity1 = projectRepository.save(entity);

        List<Long> lstId = dto.getLstId();
        if (CollectionUtils.isNotEmpty(lstId)) {
            for (Long humanId : lstId) {
                ProjectMemberEntity memberEntity = new ProjectMemberEntity();
                memberEntity.setHumanResourceId(humanId);
                memberEntity.setProjectId(entity.getProjectId());

                projectMemberRepository.save(memberEntity);
            }
        }


        log.info("-----------------luu thong tin thanh vien du an---------------");

//        ProjectMemberEntity projectMemberEntityPm = new ProjectMemberEntity();
//        projectMemberEntityPm.setProjectId(entity.getProjectId());
//        projectMemberEntityPm.setHumanResourceId(dto.getPmId());
//        projectMemberEntityPm.setRole("PM");
//        projectMemberEntityPm.setDepartment("Dev");
//        projectMemberEntityPm.setDateJoin(new Date());
//        projectMemberEntityPm.setDateOut(new Date());
//        projectMemberEntityPm.setIsActive(1);
//        log.info("-----------------save pm member--------------------");
//        memberRespository.save(projectMemberEntityPm);

        log.info("-----------------Xu ly Pm--------------------");


        ProjectMemberEntity projectMemberEntityPm = new ProjectMemberEntity();
        ProjectMemberEntity projectMemberEntityPmNew = new ProjectMemberEntity();
        List<ProjectMemberDTO> projectMemberDTOList = projectCustomRepository.findProjectMemberId("PM", dto.getProjectId(), 1);

        if (projectMemberDTOList.get(0).getHumanResourceId().longValue() != dto.getPmId().longValue()) {

            projectMemberEntityPm.setProjectMemberId(projectMemberDTOList.get(0).getProjectMemberId());
            ProjectMemberEntity projectMemberEntityPm1 = projectMemberRepository.findByProjectMemberId(projectMemberDTOList.get(0).getProjectMemberId());
            projectMemberEntityPm1.setIsActive(0);

            projectMemberEntityPmNew.setProjectId(entity.getProjectId());
            projectMemberEntityPmNew.setHumanResourceId(dto.getPmId());
            projectMemberEntityPmNew.setRole("PM");
            projectMemberEntityPmNew.setDepartment("Dev");
            projectMemberEntityPmNew.setDateJoin(new Date());
            projectMemberEntityPmNew.setDateOut(new Date());
            projectMemberEntityPmNew.setIsActive(1);
            log.info("-----------------save pm member--------------------");
            projectMemberRepository.save(projectMemberEntityPmNew);
        }

        log.info("-----------------Xu ly Ba--------------------");

        ProjectMemberEntity projectMemberEntityBa = new ProjectMemberEntity();
        ProjectMemberEntity projectMemberEntityBaNew = new ProjectMemberEntity();
        List<ProjectMemberDTO> projectMemberDTOList1 = projectCustomRepository.findProjectMemberId("BM", dto.getProjectId(), 1);

        if (projectMemberDTOList1.get(0).getHumanResourceId().longValue() != dto.getBaId().longValue()) {

            projectMemberEntityBa.setProjectMemberId(projectMemberDTOList1.get(0).getProjectMemberId());
            ProjectMemberEntity projectMemberEntityBa1 = projectMemberRepository.findByProjectMemberId(projectMemberDTOList1.get(0).getProjectMemberId());
            projectMemberEntityBa1.setIsActive(0);

            projectMemberEntityBaNew.setProjectId(entity.getProjectId());
            projectMemberEntityBaNew.setHumanResourceId(dto.getBaId());
            projectMemberEntityBaNew.setRole("BM");
            projectMemberEntityBaNew.setDepartment("BA");
            projectMemberEntityBaNew.setDateJoin(new Date());
            projectMemberEntityBaNew.setDateOut(new Date());
            projectMemberEntityBaNew.setIsActive(1);
            log.info("-----------------save ba member--------------------");
            projectMemberRepository.save(projectMemberEntityBaNew);
        }


        log.info("-----------------Xu ly TL--------------------");


        ProjectMemberEntity projectMemberEntityTL = new ProjectMemberEntity();
        ProjectMemberEntity projectMemberEntityTLNew = new ProjectMemberEntity();
        List<ProjectMemberDTO> projectMemberDTOList2 = projectCustomRepository.findProjectMemberId("TL", dto.getProjectId(), 1);

        if (projectMemberDTOList2.get(0).getHumanResourceId().longValue() != dto.getTestLeadId().longValue()) {
            projectMemberEntityTL.setProjectMemberId(projectMemberDTOList2.get(0).getProjectMemberId());
            ProjectMemberEntity projectMemberEntityTL1 = projectMemberRepository.findByProjectMemberId(projectMemberDTOList2.get(0).getProjectMemberId());
            projectMemberEntityTL1.setIsActive(0);


            projectMemberEntityTLNew.setProjectId(entity.getProjectId());
            projectMemberEntityTLNew.setHumanResourceId(dto.getTestLeadId());
            projectMemberEntityTLNew.setRole("TL");
            projectMemberEntityTLNew.setDepartment("Tester");
            projectMemberEntityTLNew.setDateJoin(new Date());
            projectMemberEntityTLNew.setDateOut(new Date());
            projectMemberEntityTLNew.setIsActive(1);
            log.info("-----------------save test lead member--------------------");
            projectMemberRepository.save(projectMemberEntityTLNew);

        }


        log.info("-----------------Xu ly Qa--------------------");


        ProjectMemberEntity projectMemberEntityQa = new ProjectMemberEntity();
        ProjectMemberEntity projectMemberEntityQaNew = new ProjectMemberEntity();
        List<ProjectMemberDTO> projectMemberDTOList3 = projectCustomRepository.findProjectMemberId("QM", dto.getProjectId(), 1);

        if (projectMemberDTOList3.get(0).getHumanResourceId().longValue() != dto.getQaId().longValue()) {
            projectMemberEntityQa.setProjectMemberId(projectMemberDTOList3.get(0).getProjectMemberId());
            ProjectMemberEntity projectMemberEntityQa1 = projectMemberRepository.findByProjectMemberId(projectMemberDTOList3.get(0).getProjectMemberId());
            projectMemberEntityQa1.setIsActive(0);

            projectMemberEntityQaNew.setProjectId(entity.getProjectId());
            projectMemberEntityQaNew.setHumanResourceId(dto.getQaId());
            projectMemberEntityQaNew.setRole("QM");
            projectMemberEntityQaNew.setDepartment("QA");
            projectMemberEntityQaNew.setDateOut(new Date());
            projectMemberEntityQaNew.setDateJoin(new Date());
            projectMemberEntityQaNew.setIsActive(1);
            log.info("-----------------save qa member--------------------");
            projectMemberRepository.save(projectMemberEntityQaNew);
        }


        log.info("-----------------luu thong tin cac tep dinh kem cua du an---------------");

        if(lstAttachFile.length > 0 ){
            attachDocumentService.uploadFile(entity1.getCode(), entity1.getProjectId(), entity1.getCreateBy(), lstAttachFile);
        }

        return dto;
    }


    @Override
    public ProjectDTO saveEstimate(ProjectDTO dto){
        ProjectEntity entity = projectRepository.findByProjectId(dto.getProjectId());
        if(dto.getStatusPreliinary()==null){
            dto.setStatusPreliinary(0);
        }
        if(dto.getStatusLatch()==null){
            dto.setStatusLatch(0);
        }

        if(dto.getStatusOffer()==null){
            dto.setStatusOffer(0);
        }
        if(dto.getStatusInternal()==null){
            dto.setStatusInternal(0);
        }


        if(dto.getStatusPreliinary()!=0)
        {
            entity.setStatusPreliinary(dto.getStatusPreliinary());
            entity.setNotePreliinary(dto.getNotePreliinary());
            entity.setReasonPrelimiinary(dto.getReasonPrelimiinary());
            //entity.setProjectId(dto.getProjectId());
            projectRepository.save(entity);
        }
        if(dto.getStatusInternal()!=0){
            entity.setStatusInternal(dto.getStatusInternal());
            entity.setNoteInternal(dto.getNoteInternal());
            entity.setReasonInternal(dto.getReasonInternal());
            projectRepository.save(entity);
        }
        if(dto.getStatusOffer()!=0 ){
            entity.setStatusOffer(dto.getStatusOffer());
            entity.setNoteOffer(dto.getNoteOffer());
            entity.setReasonOffer(dto.getReasonOffer());
            projectRepository.save(entity);
        }
        if(dto.getStatusLatch()!=0){
            entity.setStatusLatch(dto.getStatusLatch());
            entity.setNoteLatch(dto.getNoteLatch());
            entity.setReasonLatch(dto.getReasonLatch());
            projectRepository.save(entity);
        }

        return dto;
    }


    //ANHTT_IIST xoa thong tin du an
    @Override
    public Long deleteProject(Long id) {
        log.info("-----------------Xoa du an---------------");
        if (id != null) {
            ProjectEntity projectEntity = projectRepository.findByProjectId(id);
            if (projectEntity != null) {
                projectEntity.setProjectId(id);
                projectEntity.setIsActive(0);

                projectRepository.save(projectEntity);
            }

        }

        return id;
    }

    //ANHTT_IIST list project
    @Override
    public DataPage<ProjectDTO> listProject(ProjectDTO dto) {
        log.info("-----------------Danh sach du an--------------");

        DataPage<ProjectDTO> data = new DataPage<>();
        String groupPermission = "";
        //get ten nhom quyen cua nhan su
        if (dto.getHumanResourcesId() != null) {
            groupPermission = projectCustomRepository.getGroupPermissionCodeByUserId(dto.getHumanResourcesId());
        }

        if (groupPermission.equals(Constants.BOD) || groupPermission.equals(Constants.QA_MANAGER) || groupPermission.equals(Constants.ADMIN)) {
            dto.setHumanResourcesId(null);
        }
        dto.setPage(dto.getPage() != null ? dto.getPage().intValue() : 1);
        dto.setPageSize(dto.getPageSize() != null ? dto.getPageSize().intValue() : 10);

        List<ProjectDTO> listProject = new ArrayList<>();
        try {
            if (CollectionUtils.isNotEmpty(projectCustomRepository.listProject(dto))) {
                listProject = projectCustomRepository.listProject(dto);
                for (ProjectDTO projectDTO : listProject) {
                    //check xem du an da co ke hoach chua
                    List<MasterPlanEntity> lstMaster = masterPlanRepository.findByProjectId(projectDTO.getProjectId());
                    if (CollectionUtils.isNotEmpty(lstMaster)) {
                        projectDTO.setMasterPlan("Có");
                    } else {
                        projectDTO.setMasterPlan("Chưa");
                    }
                    List<AttachDocumentDTO> lstAtt = new ArrayList<>();
                    //anhtt set danh sach file dinh kem cho du an anhtt_iist
                    List<AttachDocumentEntity> lstAttEntity = attachDocumentRepository.findByParentId(projectDTO.getProjectId());
                    for (AttachDocumentEntity attachDocumentEntity : lstAttEntity) {
                        AttachDocumentDTO attachDocumentDTO = new AttachDocumentDTO();
                        attachDocumentDTO.setAttachDocumentId(attachDocumentEntity.getAttachDocumentId());
                        attachDocumentDTO.setCreateDate(new SimpleDateFormat("dd/MM/yyyy").format(attachDocumentEntity.getCreateDate()));
                        attachDocumentDTO.setPath(attachDocumentEntity.getPath());
                        attachDocumentDTO.setName(attachDocumentEntity.getName());

                        lstAtt.add(attachDocumentDTO);
                    }
                    log.info("---------------------set danh sach file dinh kem cho du an---------------------");
                    projectDTO.setLstAttachDocument(lstAtt);
                }
                data.setData(listProject);
            }

        } catch (Exception e) {

        }

        data.setPageIndex(dto.getPage());
        data.setPageSize(dto.getPageSize());
        data.setDataCount(dto.getTotalRecord());
        data.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (data.getDataCount() % data.getPageSize() != 0) {
            data.setPageCount(data.getPageCount() + 1);
        }
        return data;
    }


    @Override
    public boolean checkCodeExist(String code) {
        List<ProjectEntity> lst = projectRepository.findByCodeAndIsActive(code, 1);
        if (CollectionUtils.isNotEmpty(lst)) {
            return true;
        }
        return false;
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    //ANHTT_IIST
    @Override
    public ProjectDTO countProjectMember(String type, Long projectId, String role) {
        log.info("--------------dem so nhan vien + get ten nhan vien theo chuc vu trong du an theo tung phong----------------------");
        return projectCustomRepository.countProjectMember(type, projectId, role);
    }

    @Override
    public void save(MultipartFile file) throws IOException {
        String path = "D:/report_in/" + file.getOriginalFilename();
        File dest = new File(path);
        FileUtils.copyInputStreamToFile(file.getInputStream(), dest);

        // file path
        // file name
        String pathsave;
        String namesave;
        pathsave = dest.getAbsolutePath();
        namesave = dest.getName();

//        create obj dto

        AttachDocumentDTO atd = new AttachDocumentDTO();


//        obj.setFilePath
//        obj.setFileName

//        return obj


    }


    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

}

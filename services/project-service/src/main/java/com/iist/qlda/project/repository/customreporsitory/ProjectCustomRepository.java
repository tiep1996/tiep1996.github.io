package com.iist.qlda.project.repository.customreporsitory;

import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import com.iist.qlda.project.dto.ProjectPlanDTO;
import com.iist.qlda.project.repository.jparepository.ProjectMemberRepository;
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//ANHTT_IIST
@Repository
public class ProjectCustomRepository {
    private final Logger log = LogManager.getLogger(ProjectMemberRepository.class);

    @Autowired
    EntityManager em;

    //ANHTT_IIST tim kiem du an
    public List<ProjectDTO> listProject(ProjectDTO dto) throws Exception{
        log.info("------------------------sql get list project--------------------------");
        StringBuilder sql = new StringBuilder();

        sql.append(" select DISTINCT ");
        sql.append(" pr.PROJECT_ID as projectId, ");
        sql.append(" pr.CODE as code, ");
        sql.append(" pr.NAME as name , ");
        sql.append(" app.PAR_CODE as partnerCode, ");

        //sql lay ten pm
        sql.append( "       ( select hr.USERNAME ");
        sql.append( "            from HUMAN_RESOURCES hr ");
        sql.append( "            left join PROJECT_MEMBER pm on hr.HUMAN_RESOURCES_ID = pm.HUMAN_RESOURCE_ID ");
        sql.append( "            where pm.IS_ACTIVE=1 and pm.ROLE='PM' and pm.PROJECT_ID=pr.PROJECT_ID ) as pmName, ");


        //sql lay ten test list
        sql.append( "       ( select hr.USERNAME");
        sql.append( "            from HUMAN_RESOURCES hr ");
        sql.append( "            left join PROJECT_MEMBER pm on hr.HUMAN_RESOURCES_ID = pm.HUMAN_RESOURCE_ID ");
        sql.append( "            where pm.IS_ACTIVE=1 and pm.ROLE='TL' and pm.PROJECT_ID=pr.PROJECT_ID ) as testLederName, ");


        sql.append( "  pr.STATUS_OVERVIEW as statusOverview, ");
        sql.append( "  pr.DATE_EXPECTED as dateExpected, ");
        sql.append( "  pr.ESTIMATE_LATCH_EFFORTS as estimateLatchEfforts,  ");
        sql.append( "  mp.DATE_DELIVERY_KBKT as deliveryKBKT, ");
        sql.append( "  mp.ACTUAL_DATE_DELIVERY as actualDelivery, ");
        sql.append( "  mp.END_DATE as actualComplete, mp.DATE_DEMO as dateDemo, ");
        sql.append( "  ppr.BA_PROGRESS as baProgress , ");
        sql.append( "  ppr.DEV_PROGRESS as devProgress , ");
        sql.append( "  ppr.TEST_PROGRESS as testProgress , ");
        sql.append( "  ppr.DOC_PROGRESS as docProgress, ");
        sql.append( "  ppr.KBKT_PROGRESS as kbktProgress,  ");
        sql.append( "  ppr.RF_PROGRESS as rfProgress , ");
        sql.append( "  (select app.PAR_NAME from APP_PARAMS app where app.APP_PARAMS_ID=pr.STATUS_OVERVIEW and app.PAR_TYPE='StatusO') as statusOverviewName, ");
        sql.append( "  pr.ESTIMATE_ACTUAL_EFFORTS as estimateActual,  ");
        sql.append( "  nvl((select pm.HUMAN_RESOURCE_ID from PROJECT_MEMBER pm where pm.IS_ACTIVE=1 and pm.ROLE='PM' and pm.PROJECT_ID=pr.PROJECT_ID), 0) as pmId, " );
        sql.append( "  nvl((select pm.HUMAN_RESOURCE_ID from PROJECT_MEMBER pm where pm.IS_ACTIVE=1 and pm.ROLE='QM' and pm.PROJECT_ID=pr.PROJECT_ID ), 0) as qmId, " );
        sql.append( "  (select hr.USERNAME from HUMAN_RESOURCES hr left join PROJECT_MEMBER pm on hr.HUMAN_RESOURCES_ID = pm.HUMAN_RESOURCE_ID where pm.IS_ACTIVE=1 and pm.ROLE='QM' and pm.PROJECT_ID=pr.PROJECT_ID ) AS qmName, " );
        sql.append( "  nvl((select pm.HUMAN_RESOURCE_ID from PROJECT_MEMBER pm where pm.IS_ACTIVE=1 and pm.ROLE='TL' and pm.PROJECT_ID=pr.PROJECT_ID), 0) as testLeadId, " );
        sql.append( "  nvl((select pm.HUMAN_RESOURCE_ID from PROJECT_MEMBER pm where pm.IS_ACTIVE=1 and pm.ROLE='BM' and pm.PROJECT_ID=pr.PROJECT_ID), 0) as bmId, " );
        sql.append( "  (select hr.USERNAME from HUMAN_RESOURCES hr left join PROJECT_MEMBER pm on hr.HUMAN_RESOURCES_ID = pm.HUMAN_RESOURCE_ID where pm.IS_ACTIVE=1 and pm.ROLE='BM' and pm.PROJECT_ID=pr.PROJECT_ID ) AS bmName, " );
        sql.append( "  pr.MONTH as month , " );
        sql.append( "  nvl(pn.PARTNER_ID, 0) as partnerId, " );
        sql.append( "  (select app.PAR_NAME from APP_PARAMS app where app.APP_PARAMS_ID=pn.PARTNER_ID ) as partnerName, " );
        sql.append( "  pn.CUSTOMER_PM_NAME as pmkhName,  " );
        sql.append( "  pn.CUSTOMER_EMAIL as pmkhEmail, " );
        sql.append( "  pn.AM_NAME as amName, " );
        sql.append( "  pn.AM_EMAIL as amEmail, " );
        sql.append( "  pn.AM_PHONE as amPhone, " );
        sql.append( "  pr.ESTIMATE_PRELIMIINARY_EFFORTS as estimatePrelimiinary,  ");
        sql.append( "  pr.ESTIMATE_OFFER_EFFORTS as estimateOffer, ");

        sql.append(" pr.BA as ba, ");
        sql.append(" pr.DEV as dev, ");
        sql.append(" pr.TEST as test, ");
        sql.append(" pr.BA_MAN as baMan, ");
//        sql.append(" pr.PM_MAN as pmMan, ");
//        sql.append(" pr.START_DATE as startDate, ");
//        sql.append(" pr.END_DATE as endDate, ");
        sql.append(" pr.DESCREPTION as descreption, ");
        sql.append(" pr.STATUS_DETAIL as statusDetail, ");
        sql.append(" pr.START_DATE as startDate, ");
        sql.append(" pr.END_DATE as endDate, ");
        sql.append("  (select app.PAR_NAME from APP_PARAMS app where app.APP_PARAMS_ID=pr.STATUS_DETAIL and app.PAR_TYPE='StatusD') as statusDetailName, ");
        sql.append("  (select app.PAR_NAME from APP_PARAMS app where app.APP_PARAMS_ID=pr.STATUS_PAYMENT and app.PAR_TYPE='StatusP') as statusPaymentName, ");
        sql.append(" pr.STATUS_PAYMENT as statusPayment, ");
        sql.append(" mp.DATE_SENDING_PLAN as dateSendingPlan, ");
        sql.append( "  pr.ESTIMATE_INTERNAL_EFFORTS as estimateInternal, ");
        sql.append( "  pn.CUSTOMER_PM_PHONE as pmkhPhone, " );
        sql.append(" pr.STATUS_PRELIMIINARY_EFORTS as statusPreliinary,");
        sql.append(" pr.NOTE_PRELIMIINARY_EFORTS as notePreliinary, ");
        sql.append(" pr.REASON_PRELIMIINARY_EFORTS as reasonPrelimiinary, ");
        sql.append(" pr.STATUS_INTERNAL_EFFORTS as statusInternal, ");
        sql.append(" pr.NOTE_INTERNAL_EFFORTS as noteInternal, ");
        sql.append(" pr.REASON_INTERNAL_EFFORTS as reasonInternal, ");
        sql.append(" pr.STATUS_OFFER_EFFORTS as statusOffer, ");
        sql.append(" pr.NOTE_OFFER_EFFORTS as noteOffer, ");
        sql.append(" pr.REASON_OFFER_EFFORTS as reasonOffer,");
        sql.append(" pr.STATUS_LATCH_EFFORTS as statusLatch,");
        sql.append(" pr.NOTE_LATCH_EFFORTS as noteLatch, ");
        sql.append(" pr.REASON_LATCH_EFFORTS as reasonLatch ");
        sql.append("  from PROJECT pr  ");
        sql.append("  left join PARTNER pn on pr.PARTNER_ID = pn.ID  ");
        sql.append("  left join APP_PARAMS app on pn.PARTNER_ID = app.APP_PARAMS_ID ");
        sql.append("  left join MASTERPLAN mp on pr.PROJECT_ID = mp.PROJECT_ID ");
        sql.append("  left join PROJECT_PROGRESS ppr on pr.PROJECT_ID = ppr.PROJECT_ID ");
        sql.append("  left join PROJECT_MEMBER pm on pr.PROJECT_ID = pm.PROJECT_ID");
        sql.append("  where 1=1 and pr.IS_ACTIVE=1 ");

        if (dto.getHumanResourcesId() != null) {
            sql.append(" and pm.HUMAN_RESOURCE_ID=:humanResourcesId and pm.IS_ACTIVE=1");
        }
        if(dto.getPmId() != null){
            sql.append( " and pm.HUMAN_RESOURCE_ID=:pmId");
        }
        if(dto.getProjectId()!=null){
            sql.append( " and pr.PROJECT_ID = :projectId");
        }
        if(StringUtils.isNotBlank(dto.getCode())){
            sql.append( " and ((upper(pr.CODE) LIKE (:projectCode)) or (upper(pr.NAME) LIKE (:projectCode)))");
        }
        if(dto.getPartnerID() != null){
            sql.append( " and pn.PARTNER_ID = :partnerId");
        }
        if(dto.getEstimateLatchFrom() != null ){
            sql.append(" and pr.ESTIMATE_LATCH_EFFORTS >= :estimateLatchFrom ");
        }
        if (dto.getEstimateLatchTo() != null) {
            sql.append(" and pr.ESTIMATE_LATCH_EFFORTS <= :estimateLatchTo ");
        }
        if (dto.getStatusOverview() != null) {
            sql.append(" and pr.STATUS_OVERVIEW = :statusOverview ");
        }
        if (CollectionUtils.isNotEmpty(dto.getLstStatusDetail())) {
            sql.append(" and pr.STATUS_DETAIL in (:lstStatusDetail) ");
        }
        if (StringUtils.isNotBlank(dto.getStartDate1())) {
            sql.append(" and pr.START_DATE >= :startDate");
        }
        if (StringUtils.isNotBlank(dto.getEndDate1())) {
            sql.append(" and pr.END_DATE <= :endDate");
        }

        sql.append(" ORDER BY pr.PROJECT_ID DESC ");

        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if (dto.getHumanResourcesId() != null) {
            query.setParameter("humanResourcesId", dto.getHumanResourcesId());
            queryCount.setParameter("humanResourcesId", dto.getHumanResourcesId());
        }
        if (dto.getPmId() != null) {
            query.setParameter("pmId", dto.getPmId());
            queryCount.setParameter("pmId", dto.getPmId());
        }
        if (dto.getProjectId() != null) {
            query.setParameter("projectId", dto.getProjectId());
            queryCount.setParameter("projectId", dto.getProjectId());
        }
        if (StringUtils.isNotBlank(dto.getCode())){
            query.setParameter("projectCode", "%" + dto.getCode() + "%");
            queryCount.setParameter("projectCode", "%" + dto.getCode() + "%");
        }
        if (dto.getPartnerID() != null) {
            query.setParameter("partnerId", dto.getPartnerID());
            queryCount.setParameter("partnerId", dto.getPartnerID());
        }
        if (dto.getEstimateLatchFrom() != null) {
            query.setParameter("estimateLatchFrom", dto.getEstimateLatchFrom());
            queryCount.setParameter("estimateLatchFrom", dto.getEstimateLatchFrom());
        }
        if (dto.getEstimateLatchTo() != null) {
            query.setParameter("estimateLatchTo", dto.getEstimateLatchTo());
            queryCount.setParameter("estimateLatchTo", dto.getEstimateLatchTo());
        }
        if (dto.getStatusOverview() != null) {
            query.setParameter("statusOverview", dto.getStatusOverview());
            queryCount.setParameter("statusOverview", dto.getStatusOverview());
        }
        if (CollectionUtils.isNotEmpty(dto.getLstStatusDetail())) {
            query.setParameter("lstStatusDetail", dto.getLstStatusDetail());
            queryCount.setParameter("lstStatusDetail", dto.getLstStatusDetail());
        }
        if(StringUtils.isNotBlank(dto.getStartDate1())){
            query.setParameter("startDate", dto.getStartDate1());
            queryCount.setParameter("startDate", dto.getStartDate1());
        }
        if (StringUtils.isNotBlank(dto.getEndDate1())) {
            query.setParameter("endDate", dto.getEndDate1());
            queryCount.setParameter("endDate", dto.getEndDate1());
        }

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }

        List<Object[]> lstObject = query.getResultList();

        return convertObjectToDto(lstObject);
    }

    //ANHTT_IIST convert object to dto
    public List<ProjectDTO> convertObjectToDto(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<ProjectDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setProjectId(((BigInteger) obj[0]).longValue());
                projectDTO.setCode((String) obj[1]);
                projectDTO.setName((String) obj[2]);
                projectDTO.setPartnerCode((String) obj[3]);
                projectDTO.setPmName((String) obj[4]);
                projectDTO.setTestLeaderName((String) obj[5]);
                projectDTO.setStatusOverview((Integer) obj[6]);
                projectDTO.setDateExpected((String) obj[7]);
                projectDTO.setEstimateLatch((Double) obj[8]);
                projectDTO.setDateDeliveryKbkt((Date) obj[9]);
                projectDTO.setDateActualDelivery((Date) obj[10]);
                projectDTO.setDateActualComplete((Date) obj[11]);
                projectDTO.setDateDemo((Date) obj[12]);
                projectDTO.setBaProgress((Double) obj[13]);
                projectDTO.setDevProgress((Double) obj[14]);
                projectDTO.setTestProgress((Double) obj[15]);
                projectDTO.setDocProgress((Double) obj[16]);
                projectDTO.setKbktProgress((Double) obj[17]);
                projectDTO.setRetestOrFixbugProgress((Double) obj[18]);
                projectDTO.setStatusOverviewName((String) obj[19]);
                projectDTO.setEstimateActual((Double) obj[20]);

                projectDTO.setPmId(((BigInteger) obj[21]).longValue());
                projectDTO.setQaId(((BigInteger) obj[22]).longValue());
                projectDTO.setQmName((String) obj[23]);
                projectDTO.setTestLeadId(((BigInteger) obj[24]).longValue());
                projectDTO.setBaId(((BigInteger) obj[25]).longValue());
                projectDTO.setBmName((String) obj[26]);
                projectDTO.setMonth((String) obj[27]);
                projectDTO.setPartnerID(((BigInteger) obj[28]).longValue());
                projectDTO.setCustomerPmName((String) obj[30]);
                projectDTO.setCustomerEmail((String) obj[31]);
                projectDTO.setAmName((String) obj[32]);
                projectDTO.setAmEmail((String) obj[33]);
                projectDTO.setAmPhone((String) obj[34]);
                projectDTO.setEstimatePrelimiinary((Double) obj[35]);
                projectDTO.setEstimateOffer((Double) obj[36]);
                projectDTO.setBa((Integer) obj[37]);
                projectDTO.setDev((Integer) obj[38]);
                projectDTO.setTest((Integer) obj[39]);
                projectDTO.setBaMan((Integer) obj[40]);
                //               projectDTO.setPmMan((Integer)obj[41]);
//                projectDTO.setStartDate((Date) obj[42]);
//                projectDTO.setEndDate((Date) obj[43]);
                projectDTO.setDescription((String) obj[41]);
                projectDTO.setStatusDetail((Integer) obj[42]);
                if (obj[43] != null) {
                    projectDTO.setStartDate(obj[43].toString());
                }
                if (obj[44] != null) {
                    projectDTO.setEndDate(obj[44].toString());
                }
                if (obj[45] != null) {
                    projectDTO.setStatusDetailName(obj[45].toString());

                }
                if (obj[46] != null) {
                    projectDTO.setStatusPaymentName(obj[46].toString());

                }
                projectDTO.setStatusPayment((Integer) obj[47]);
                projectDTO.setDateSendingPlan((Date)obj[48]);
                String ba = (Integer)obj[37]==1?"BA, ":"";
                String dev = (Integer)obj[38]==1?"Dev, ":"";
                String test = (Integer)obj[39]==1?"Tester":"";
                projectDTO.setRequireRange(ba + dev + test);
                if(obj[49] != null ){
                    projectDTO.setEstimateInternal((Double)obj[49]);

                }
                projectDTO.setCustomerPmPhone((String) obj[50]);
                projectDTO.setPartnerName((String) obj[29]);

                if(obj[51] != null){
                    projectDTO.setStatusPreliinary((Integer) obj[51]);
                }
                if(obj[52] != null){
                    projectDTO.setNotePreliinary((String) obj[52]);
                }
                if(obj[53] !=null){
                    projectDTO.setReasonPrelimiinary((String) obj[53]);
                }
                if(obj[54] !=null){
                    projectDTO.setStatusInternal((Integer) obj[54]);
                }
                if(obj[55] !=null){
                    projectDTO.setNoteInternal((String) obj[55]);
                }
                if(obj[56] !=null){
                    projectDTO.setReasonInternal((String) obj[56]);
                }
                if(obj[57] !=null){
                    projectDTO.setStatusOffer((Integer) obj[57]);
                }
                if(obj[58] !=null){
                    projectDTO.setNoteOffer((String) obj[58]);
                }
                if(obj[59] !=null){
                    projectDTO.setReasonOffer((String) obj[59]);
                }
                if(obj[60]!=null){
                    projectDTO.setStatusLatch((Integer) obj[60]);
                }
                if(obj[61]!=null){
                    projectDTO.setNoteLatch((String) obj[61]);
                }
                if(obj[62]!=null){
                    projectDTO.setReasonOffer((String) obj[62]);
                }

                listDto.add(projectDTO);
            }
        }

        return listDto;
    }

    public int checkCodeExist(String code) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) from PROJECT pr where 1=1 and pr.CODE=:code");
        Query query = em.createNativeQuery(sql.toString());

        query.setParameter("code", code);

        return query.getMaxResults();

    }

    //anhTT_iist
    public String getRoleUserById(Long humanResourcesId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select app.PAR_CODE as code " +
                "       from HUMAN_RESOURCES hr" +
                "       left Join APP_PARAMS app on hr.POSITION_ID = app.APP_PARAMS_ID" +
                "       WHERE 1=1 and hr.IS_ACTIVE=1 and   hr.HUMAN_RESOURCES_ID=:humanResourcesId   ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("humanResourcesId", humanResourcesId);

        return (String) query.getSingleResult();
    }

    //anhtt_iist
    public ProjectDTO countProjectMember(String type, Long projectId, String role) {

        log.info("--------------dem so thanh vien trong du an----------------------");
        StringBuilder sql = new StringBuilder();
        sql.append("   select CONCAT(  hr.LASTNAME, ' ' , hr.FIRSTNAME) as fullName, count(*) " +
                "           from PROJECT_MEMBER pm " +
                "       left join HUMAN_RESOURCES hr on pm.HUMAN_RESOURCE_ID = hr.HUMAN_RESOURCES_ID " +
                "       WHERE 1=1 and pm.PROJECT_ID=:projectId and pm.IS_ACTIVE=1 ");

        if (StringUtils.isNotBlank(type)) {
            sql.append("  and upper(pm.DEPARTMENT) like upper(:type)  ");
        }
        if (StringUtils.isNotBlank(role)) {
            sql.append("  and upper(pm.ROLE) like upper(:role)  ");
        }
        Query query = em.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        if (StringUtils.isNotBlank(type)) {
            query.setParameter("type", type);
        }
        if (StringUtils.isNotBlank(role)) {
            query.setParameter("role", role);
        }

        Object[] obj = (Object[]) query.getSingleResult();

        return convertObjectToDto2(obj);

    }

    public ProjectDTO convertObjectToDto2(Object[] obj) {
        log.info("-------------------------convert dto----------------------------");

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setFullName((String) obj[0]);
        projectDTO.setCountProjectMember(((BigInteger) obj[1]).longValue());

        return projectDTO;
    }

    public List<ProjectMemberDTO> findProjectMemberId(String lv, long projectId, int isActive) {
        StringBuilder sql = new StringBuilder();
        sql.append("    select * ");
        sql.append("    FROM PROJECT_MEMBER                                            ");
        sql.append("    WHERE PROJECT_MEMBER.ROLE = (:lv) AND PROJECT_MEMBER.PROJECT_ID = (:projectId) AND PROJECT_MEMBER.IS_ACTIVE = (:isActive) ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("lv", lv);
        query.setParameter("projectId", projectId);
        query.setParameter("isActive", isActive);
        List<Object[]> lstObject = query.getResultList();
        List<ProjectMemberDTO> lstprojectMemberDTO = new ArrayList<ProjectMemberDTO>();
        if(CollectionUtils.isNotEmpty(lstObject)){
            for(Object[] obj : lstObject){
                ProjectMemberDTO projectPlanDTO = new ProjectMemberDTO();
                projectPlanDTO.setProjectMemberId(((BigInteger)obj[0]).longValue());
                projectPlanDTO.setHumanResourceId(((BigInteger)obj[1]).longValue());
                lstprojectMemberDTO.add(projectPlanDTO);
            }
        }
        return lstprojectMemberDTO;
    }

    //anhtt_iist get nhom quyen cua nhan su (BOD, BA, QA....)
    public String getGroupPermissionCodeByUserId(Long userId) {
        log.info("-------------get nhom quyen cua nhan su-------------");
        StringBuilder sql = new StringBuilder();
        sql.append("    select gp.CODE " +
                "       from GROUPPERMISSION gp " +
                "       LEFT JOIN GROUPPERMISSION_USER gpu on gp.ID = gpu.GROUP_PERMISSION_ID " +
                "       where 1=1 and gpu.USER_ID=:userId ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        return (String) query.getSingleResult();
    }

}

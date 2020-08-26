package com.iist.qlda.project.repository.customreporsitory;

import com.iist.qlda.project.dto.ProjectDTO;
import com.iist.qlda.project.dto.ProjectMemberDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class ProjectMemberCustomRepository {
    @Autowired
    private EntityManager em;

    Logger log = LogManager.getLogger(ProjectMemberCustomRepository.class);

    //anhtt_iist get danh sach nhan su du an
    public List<ProjectMemberDTO> getListMemberProject(ProjectDTO dto){
        log.info("----------------sql get danh sach thanh vien du an theo id du an-----------------");
        StringBuilder sql = new StringBuilder();
        sql.append("    select " +
                "       pm.PROJECT_MEMBER_ID as projectMemberId, " +
                "       hr.USERNAME," +
                "       pm.DEPARTMENT, " +
                "       pm.DATE_JOIN, " +
                "       pm.DATE_OUT, " +
                "       pm.RESOURCES, " +
                "       hr.FIRSTNAME, " +
                "       pm.HUMAN_RESOURCE_ID, " +
                "       pm.ROLE, " +
                "       pm.IS_ACTIVE, " +
                "       pm.KPI " +
                "       from PROJECT_MEMBER pm " +
                "       left join HUMAN_RESOURCES hr on pm.HUMAN_RESOURCE_ID=hr.HUMAN_RESOURCES_ID " +
                "       where 1=1 " +
                "       and pm.PROJECT_ID=:projectId ");
        if(dto.getIsActive() != null){
            sql.append(" and pm.IS_ACTIVE = :isActive");
        }

        sql.append(" ORDER BY pm.HUMAN_RESOURCE_ID,pm.DATE_JOIN   ");

        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        query.setParameter("projectId", dto.getProjectId());
        queryCount.setParameter("projectId", dto.getProjectId());
        if(dto.getIsActive() != null){
            query.setParameter("isActive", dto.getIsActive());
            queryCount.setParameter("isActive", dto.getIsActive());
        }

        if(dto.getPage() != null && dto.getPageSize() != null){
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }

        List<Object[]> lstObject = query.getResultList();

        return convertToDto(lstObject);
    }

    public List<ProjectMemberDTO> convertToDto(List<Object[]> lstObject){
        log.info("---------convert to dto-------------");
        List<ProjectMemberDTO> lstDto = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(lstObject)){
            for(Object[] obj : lstObject){
                ProjectMemberDTO dto = new ProjectMemberDTO();
                dto.setProjectMemberId(((BigInteger)obj[0]).longValue());
                dto.setUsername((String)obj[1]);
                dto.setDepartment((String)obj[2]);
                dto.setDateJoin((Date)obj[3]);
                dto.setDateOut((Date)obj[4]);
                dto.setResources((Integer)obj[5]);
                dto.setFirstName((String)obj[6]);
                dto.setHumanResourceId(((BigInteger)obj[7]).longValue());
                dto.setRole((String)obj[8]);
                dto.setIsActive((Integer)obj[9]);
                dto.setKPI((Double) obj[10]);
                lstDto.add(dto);
            }
        }

        return lstDto;
    }

    //anhtt_iist get lan gan nhat thanh vien join du an
    public Integer getMaxNoJoin(Long projectId, Long humanResourceId){
        log.info("---------tinh lan gan nhat ma thanh vien duoc join vao du an--------------");
        StringBuilder sql = new StringBuilder();
        sql.append("    select nvl(MAX(pm.NO_JOIN), 0) " +
                "       from PROJECT_MEMBER pm " +
                "       where 1=1 " +
                "       and pm.PROJECT_ID=:projectId " +
                "       and pm.HUMAN_RESOURCE_ID=:humanId ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("humanId", humanResourceId);

        return (Integer) query.getSingleResult();
    }

    //anhtt_iist tinh so ngay du kien ma thanh vien lam viec trong du an
    public Double countDateInProject(Date dateJoin, Date dateOut) {
        log.info("----------tinh kpi du kien cua nhan su trong du an-----------");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(dateJoin);
        c2.setTime(dateOut);

        c1.set(Calendar.MILLISECOND, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.HOUR_OF_DAY, 0);

        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR_OF_DAY, 0);

        // Công thức tính số ngày giữa 2 mốc thời gian:
        Double countDate = Double.valueOf((c2.getTime().getTime() - c1.getTime().getTime()) / (24.0 * 3600.0 * 1000.0)) + 1;
        //dem so ngay t7
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateJoin);
        int cachT7 = 7 - calendar.get(Calendar.DAY_OF_WEEK);
        int countT7 = (int) ((countDate - cachT7 > 0) ? (Math.ceil((countDate - cachT7) / 7.0)) : 0);
        int cachT8 = cachT7 + 1;
        int countT8 = (int) ((countDate - cachT8 > 0) ? (Math.ceil((countDate - cachT8) / 7.0)) : 0);

        // tinh so ngay thu 7 chủ nhat, 1 tuan co 2 thu 7+cn, 2 tuan lam 1 lan t7 nen nhan 1,5
//        Double countWeeken = Double.valueOf((c2.getTime().getTime() - c1.getTime().getTime()) / (24.0 * 3600.0 * 1000.0)/7.0*1.5);

//        Double result = countDate-countWeeken + 1;
        Double result = countDate - countT7 * 0.5 - countT8;
        return result;
    }

    //anhtt_iist tinh KPI cua du an
    public Double countKpiProject(Long projectId){
        log.info("----------tinh kpi cua du an-----------");
        StringBuilder sql = new StringBuilder();
        sql.append("    select nvl(sum(pm.KPI), 0) from PROJECT_MEMBER pm where 1=1 and pm.PROJECT_ID=:projectId ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);

        return (Double) query.getSingleResult();
    }

    //anhtt_iist tinh phan tram nguon luc da su dung cua nhan su trong cac du an
    public BigDecimal countResourceOfMember(Long humanId){
        log.info("--------------tinh phan tram nguon luc cua nhan su da duoc su dung---------------");
        StringBuilder sql = new StringBuilder();
        sql.append("    select nvl(sum(pm.RESOURCES), 0) from PROJECT_MEMBER pm where pm.HUMAN_RESOURCE_ID=:humanId ");
        Query query = em.createNativeQuery(sql.toString());

        query.setParameter("humanId", humanId);

        return (BigDecimal) query.getSingleResult();
    }

    //anhtt_iist check thanh vien trong du an
    public BigInteger checkMemberProject(Long projectId, Long humanId , Integer isActive){
        log.info("-------------check thanh vien trong du an ------------------");
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) " +
                "   from PROJECT_MEMBER pm " +
                "   where pm.PROJECT_ID=:projectId " +
                "   and pm.HUMAN_RESOURCE_ID=:humanId " +
                "   and pm.IS_ACTIVE = :isActive ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("humanId", humanId);
        query.setParameter("isActive", isActive);
        return (BigInteger) query.getSingleResult();
    }

}

package com.iist.qlda.project.repository.customreporsitory;

import com.iist.qlda.project.dto.DTOSearch;
import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.HumanResourcesShowDTO;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import common.DateUtils;
import common.ResultResp;
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
public class HumanResourcesCustomRepository {
    @Autowired
    EntityManager em;

    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    //ANHTT_IIST api get danh sach nhan su theo ten hoac code
    public List<HumanResourcesDTO> getListHumanResourceByNameOrCode(DTOSearch dto) {
        log.info("-----------------------SQL lay thong tin nhan su theo tung chuc vu-----------------------");
        StringBuilder sql = new StringBuilder();

        sql.append("    select ");
        sql.append("    hr.HUMAN_RESOURCES_ID as humanResourceId ,                          ");
        sql.append("    hr.FIRSTNAME as name ,                                                   ");
        sql.append("    hr.USERNAME as  username ,                                                   ");
        sql.append("    hr.EMAIL as email                                                   ");
        sql.append("    FROM HUMAN_RESOURCES hr                                             ");
        sql.append("    left join APP_PARAMS app on hr.POSITION_ID=app.APP_PARAMS_ID        ");
        sql.append("    where 1=1 and hr.IS_ACTIVE=1      ");


        if (StringUtils.isNotBlank(dto.getKeySearch())) {
            sql.append("  and (( upper(hr.USERNAME) LIKE :keySearch ) or ( upper(hr.EMAIL) LIKE :keySearch ))");
        }
        if (StringUtils.isNotBlank(dto.getType())) {
            sql.append(" and upper(app.PAR_CODE) LIKE upper(:parCode)");
        }

        Query query = em.createNativeQuery(sql.toString());

        if (StringUtils.isNotBlank(dto.getKeySearch())) {
            query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");
        }
        if (StringUtils.isNotBlank(dto.getType())) {
            query.setParameter("parCode", dto.getType());
        }

        List<Object[]> lstObject = query.getResultList();


        return convertObjectToDto(lstObject);
    }

    //ANHTT_IIST convert object to dto
    public List<HumanResourcesDTO> convertObjectToDto(List<Object[]> lstObject) {
        log.info("------------------convert object to dto------------------");
        List<HumanResourcesDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesDTO resourcesDTO = new HumanResourcesDTO();
                resourcesDTO.setHumanResourceId(((BigInteger) obj[0]).longValue());
                resourcesDTO.setFirstName((String) obj[1]);
                resourcesDTO.setUsername((String) obj[2]);
                resourcesDTO.setEmail((String) obj[3]);

                listDto.add(resourcesDTO);
            }
        }

        return listDto;
    }


    //ANHTT_IIST
    // lay tat ca cac quyen cua theo userId
    public List<String> getListPermissionByUserId(Long userId) {
        log.info("----------------------sql get permission by userid-----------------------------");

        StringBuilder sql = new StringBuilder();
        sql.append("    select                                                                                          ");
        sql.append("    PER.CODE                                                                                        ");
        sql.append("    FROM PERMISSION PER                                                                             ");
        sql.append("    LEFT JOIN GROUPPERMISSION_PERMISSION GPP on PER.PERMISSION_ID = GPP.PERMISSION_ID                    ");
        sql.append("    LEFT JOIN GROUPPERMISSION GP on GP.ID = GPP.GROUP_PERMISSION_ID                                 ");
        sql.append("    LEFT JOIN GROUPPERMISSION_USER GPU on GPU.GROUP_PERMISSION_ID = GP.ID                          ");
        sql.append("    where GP.STATUS = 'ACTIVE'                           ");

        if (userId != null) {
            sql.append("  and GPU.USER_ID = :userId");
        }

        Query query = em.createNativeQuery(sql.toString());

        if (userId != null) {
            query.setParameter("userId", userId);
        }

        return query.getResultList();
    }

    public List<HumanResourcesDTO> getListHumanResourceByProjectID(long id) {

        StringBuilder sql = new StringBuilder();

        sql.append("    SELECT ");
        sql.append("    hr.USERNAME as NAME ,                          ");
        sql.append("    hr.HUMAN_RESOURCES_ID as resourceId                                                    ");
        sql.append("    FROM HUMAN_RESOURCES hr                                             ");
        sql.append("    LEFT JOIN PROJECT_MEMBER pm ON hr.HUMAN_RESOURCES_ID=pm.HUMAN_RESOURCE_ID        ");
        sql.append("    WHERE pm.IS_ACTIVE = 1 AND hr.IS_ACTIVE = 1 AND pm.PROJECT_ID= (:id)       ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("id", id);
        List<Object[]> lstObject = query.getResultList();
        List<HumanResourcesDTO> listDto = new ArrayList<HumanResourcesDTO>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesDTO humanDto = new HumanResourcesDTO();
                humanDto.setUsername((String) obj[0]);
                humanDto.setHumanResourceId(((BigInteger) obj[1]).longValue());
                listDto.add(humanDto);
            }
        }
        return listDto;
    }


    // TanNV
    public List<HumanResourcesShowDTO> getlistHumanResources(HumanResourcesShowDTO dto) {
        log.info("------------------------sql get list HumanResources--------------------------");

        StringBuilder sql = new StringBuilder();
        sql.append(" select    ");
        sql.append(" hr.HUMAN_RESOURCES_ID as humanResourceId,         ");
        sql.append(" hr.USERNAME as code,        ");
        // sql lay name
        sql.append(" ( GROUP_CONCAT(CONCAT_WS(' ', hr.FIRSTNAME, hr.LASTNAME))) as name,            ");
        sql.append(" hr.IS_ACTIVE as active,               ");
        sql.append(" hr.EMAIL as email,                  ");
        sql.append(" hr.PHONE as phone,                  ");
        sql.append(" a.PAR_CODE  as parcode              ");
        sql.append(" from HUMAN_RESOURCES as hr              ");
        sql.append(" LEFT JOIN APP_PARAMS as a on hr.DEPARTMENT_ID = a.APP_PARAMS_ID              ");
        sql.append(" LEFT JOIN CENTERS as st on hr.CENTER = st.ID          ");

        sql.append("  where 1 = 1 ");
        if (dto.getActive() != null) {
            sql.append(" and hr.IS_ACTIVE = :active ");
        }
        if (StringUtils.isNotBlank(dto.getUsername())) {
            sql.append(" and(( hr.USERNAME LIKE :hrCode  or :hrCode is null )");
            sql.append(" or (  (CONCAT_WS(' ', FIRSTNAME, LASTNAME)) LIKE  :hrCode  or :hrCode is null )   ");
            sql.append(" or (  hr.EMAIL LIKE :hrCode  or :hrCode is null ) )");
        }
        if ((StringUtils.isNotBlank(dto.getParcode()))) {
            sql.append(" and upper(a.PAR_CODE) LIKE :parcode ");
        }if (dto.getCenterId() != null) {
            sql.append("  and st.ID = :idCT ");
        }



        sql.append(" GROUP BY HUMAN_RESOURCES_ID ");
        sql.append(" ORDER BY hr.HUMAN_RESOURCES_ID DESC ");
        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if (dto.getActive() != null) {
            query.setParameter("active", dto.getActive());
            queryCount.setParameter("active", dto.getActive());
        }
        if (StringUtils.isNotBlank(dto.getUsername())) {
            query.setParameter("hrCode", "%" + dto.getUsername() + "%");
            queryCount.setParameter("hrCode", "%" + dto.getUsername() + "%");
        }
        if (StringUtils.isNotBlank(dto.getParcode())) {
            query.setParameter("parcode", "%" + dto.getParcode() + "%");
            queryCount.setParameter("parcode", "%" + dto.getParcode() + "%");
        } if(dto.getCenterId() != null){
            query.setParameter("idCT", dto.getCenterId());
            queryCount.setParameter("idCT", dto.getCenterId());
        }



        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
            dto.setTotalRecord((long) queryCount.getResultList().size());
        }

        List<Object[]> lstObject = query.getResultList();

        return convertObjectToDtoShow(lstObject);
    }


    //TanNV convert object to dto
    public List<HumanResourcesShowDTO> convertObjectToDtoShow(List<Object[]> lstObject) {
        log.info("-------------------------convert dto----------------------------");
        List<HumanResourcesShowDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesShowDTO humanResourcesDTO = new HumanResourcesShowDTO();
                humanResourcesDTO.setHumanResourceId((BigInteger) obj[0]);
                humanResourcesDTO.setCode((String) obj[1]);
                humanResourcesDTO.setName((String) obj[2]);
                humanResourcesDTO.setActive((Integer) obj[3]);
                humanResourcesDTO.setEmail((String) obj[4]);
                humanResourcesDTO.setPhone((String) obj[5]);
                humanResourcesDTO.setParcode((String) obj[6]);
                listDto.add(humanResourcesDTO);
            }
        }

        return listDto;
    }

    // TanNV
    public Integer deleteHumanGroupUser(Long id) {
        log.info("------------------------sql delete HumanResources--------------------------");

        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" COUNT(*) ");
        sql.append(" FROM HUMAN_RESOURCES hr ");
        sql.append(" INNER JOIN HUMAN_GROUP_USER as hgu on hgu.HUMAN_RESOURCE_ID = hr.HUMAN_RESOURCES_ID ");
        sql.append("  WHERE HUMAN_RESOURCES_ID = :id ");

        Query query = em.createNativeQuery(sql.toString());
        Query queryCount = em.createNativeQuery(sql.toString());

        if (id != null) {
            query.setParameter("id", id);
            queryCount.setParameter("id", id);
        }
        int count = ((BigInteger) query.getSingleResult()).intValue();
        return count;
    }

    public boolean checkAssociationBeforeDelete(Long id){
        Query query;
        StringBuilder sql;
        Date date = new Date();
        String currentDate = DateUtils.formatDate(date);

        log.info("<--- CHECK ASSOCIATION BEFORE DELETE START");

        log.info("<--- Check association between human_resources and project_plan");
        sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" COUNT(*) ");
        sql.append(" FROM PROJECT_PLAN AS P ");
        sql.append(" INNER JOIN HUMAN_RESOURCES AS HR ON P.HUMAN_RESOURCE_ID = HR.HUMAN_RESOURCES_ID ");
        sql.append("  WHERE (HR.HUMAN_RESOURCES_ID = :id AND P.IS_ACTIVE = 1 AND P.END_DATE >=  :date)");
        query = em.createNativeQuery(sql.toString());
        if(null != id){
            query.setParameter("id",id);
            query.setParameter("date",currentDate);
        }
        int count2 = ((BigInteger) query.getSingleResult()).intValue();


        log.info("<--- Check association between human_resources and project_member");
        sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" COUNT(*) ");
        sql.append(" FROM PROJECT_MEMBER AS PM ");
        sql.append(" INNER JOIN HUMAN_RESOURCES HR ON PM.HUMAN_RESOURCE_ID = HR.HUMAN_RESOURCES_ID ");
        sql.append("  INNER JOIN PROJECT AS P ON P.PROJECT_ID = PM.PROJECT_ID ");
        sql.append(" WHERE (PM.IS_ACTIVE = 1 AND P.END_DATE >= :date AND P.IS_ACTIVE = 1 AND HR.HUMAN_RESOURCES_ID = :id) ");
        query = em.createNativeQuery(sql.toString());
        if(null != id){
            query.setParameter("id",id);
            query.setParameter("date",currentDate);
        }
        int count3 = ((BigInteger) query.getSingleResult()).intValue();

        if(count2 !=0 || count3 !=0){
            log.error("<--- CHECK ASSOCIATION COMPLETE: HAVE ASSOCIATION, CAN'T DELETE HUMAN_RESOURCES");
            return false;
        }
        if(count2 ==0 && count3 ==0){
            log.error("<--- CHECK ASSOCIATION COMPLETE: DON'T HAVE ASSOCIATION, CAN DELETE HUMAN_RESOURCES");
            return true;
        }
        return true;
    }

}

package com.iist.qlda.project.repository.customreporsitory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.MasterPlanDTO;
import com.iist.qlda.project.dto.ProjectPlanDTO;

@Repository
public class MasterPlanCustomRepository {

	@Autowired
    EntityManager em;
	
	public List<ProjectPlanDTO> getListProjectPlanDTO(Long i){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("    select ");
        sql.append("    pp.MODULE, ");
        sql.append("    pp.MILESTONE, ");
        sql.append("    hr.USERNAME, ");
        sql.append("    pp.START_DATE, ");
        sql.append("    pp.END_DATE, ");
        sql.append("    pp.DEADLINE, ");
        sql.append("    pp.DESCRIPTION, ");
        sql.append("    pp.HUMAN_RESOURCE_ID, ");
        sql.append("    pp.PROJECT_PLAN_ID ");
        sql.append("    FROM PROJECT_PLAN pp                                              ");
        sql.append("    LEFT JOIN HUMAN_RESOURCES hr ON hr.HUMAN_RESOURCES_ID = pp.HUMAN_RESOURCE_ID    ");
        sql.append("    LEFT JOIN MASTERPLAN mp ON pp.MASTERPLAN_ID = mp.MASTERPLAN_ID where mp.PROJECT_ID=(:id)     ");
        sql.append("    ORDER BY pp.DEADLINE     ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("id", i);
        List<Object[]> lstObject = query.getResultList();
        List<ProjectPlanDTO> listDto = new ArrayList<ProjectPlanDTO>();
        if(CollectionUtils.isNotEmpty(lstObject)){
            for(Object[] obj : lstObject){
            	ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
            	projectPlanDTO.setModule((String)obj[0]);
            	projectPlanDTO.setProjectPlanId(((BigInteger)obj[8]).longValue());
            	projectPlanDTO.setMileStone((String)obj[1]);
                projectPlanDTO.setHumanName((String)obj[2]);
            	projectPlanDTO.setStartDate((Date)obj[3]);
            	projectPlanDTO.setEndDate((Date)obj[4]);
            	projectPlanDTO.setDeadline((Date) obj[5]);
            	projectPlanDTO.setDescription((String)obj[6]);
            	projectPlanDTO.setHumanResourceId(((BigInteger)obj[7]).longValue());
                listDto.add(projectPlanDTO);
            }
        }
        return listDto;
	}
public void deleteProjectPlan(Long i){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("    DELETE  ");
        sql.append("    FROM PROJECT_PLAN                                               ");
        sql.append("    WHERE PROJECT_PLAN_ID=(:id)     ");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("id", i);
        List<Object[]> lstObject = query.getResultList();
	}

}

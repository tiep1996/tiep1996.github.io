package com.iist.qlda.project.repository.jparepository;

import com.iist.qlda.project.dto.IEmailBOD;
import com.iist.qlda.project.dto.IHumanResourceOfProject;
import com.iist.qlda.project.dto.IProjectAndHumanMasterplan;
import com.iist.qlda.project.dto.IProjectMailDTO;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import com.iist.qlda.project.entity.ProjectPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface ProjectPlanRepository extends JpaRepository<ProjectPlanEntity, Long> {
	@Transactional
	ProjectPlanEntity deleteByProjectPlanId(BigInteger id);

	//dangnp 31/07

	/*-Lấy ra các dự án thực hiện nhỏ hơn 1 tháng và có deadline 7 ngày hoặc 3 ngày hoặc 1 ngày hoặc trong ngày
	 * -Hoặc lấy ra các dự án thực hiện trong vòng nhỏ hơn 3 tháng và có deadline 1 tháng hoặc 14 ngày hoặc 7 ngày hoặc 3 ngày hoặc 1 ngày hoặc trong ngày
	 * -Hoặc lấy ra dự án thực hiện trong vòng lớn hơn 3 tháng và có deadline 3 tháng hoặc 1 tháng hoặc 14 ngày hoặc 7 ngày hoặc 3 ngày hoặc 1 ngày hoặc trong ngày. */
	String sql = "SELECT * FROM PROJECT_PLAN WHERE DEADLINE>=CURRENT_DATE AND(  " +
			"(  " +
			"  DATE_SUB(END_DATE, INTERVAL 1 MONTH)<=START_DATE   " +
			"  AND   " +
			"   (DATE_SUB(DEADLINE, INTERVAL 7 DAY)=CURRENT_DATE Or DATE_SUB(DEADLINE, INTERVAL 3 DAY)=CURRENT_DATE Or DATE_SUB(DEADLINE, INTERVAL 1 DAY)=CURRENT_DATE Or DEADLINE=CURRENT_DATE)   " +
			")  " +
			" OR(  " +
			"  DATE_SUB(END_DATE, INTERVAL 3 MONTH)<=START_DATE   " +
			"  AND   " +
			"   (  " +
			"  DATE_SUB(DEADLINE, INTERVAL 1 MONTH)=CURRENT_DATE Or  " +
			"  DATE_SUB(DEADLINE, INTERVAL 14 Day)=CURRENT_DATE Or  " +
			"  DATE_SUB(DEADLINE, INTERVAL 7 DAY)=CURRENT_DATE Or DATE_SUB(DEADLINE, INTERVAL 3 DAY)=CURRENT_DATE Or DATE_SUB(DEADLINE, INTERVAL 1 DAY)=CURRENT_DATE Or DEADLINE=CURRENT_DATE)   " +
			" )  " +
			" OR(  " +
			"  DATE_SUB(END_DATE, INTERVAL 3 MONTH)>START_DATE   " +
			"  AND   " +
			"   (  " +
			"  DATE_SUB(DEADLINE, INTERVAL 3 MONTH)=CURRENT_DATE Or  " +
			"  DATE_SUB(DEADLINE, INTERVAL 1 MONTH)=CURRENT_DATE Or  " +
			"  DATE_SUB(DEADLINE, INTERVAL 14 Day)=CURRENT_DATE Or  " +
			"  DATE_SUB(DEADLINE, INTERVAL 7 DAY)=CURRENT_DATE Or DATE_SUB(DEADLINE, INTERVAL 3 DAY)=CURRENT_DATE Or DATE_SUB(DEADLINE, INTERVAL 1 DAY)=CURRENT_DATE Or DEADLINE=CURRENT_DATE)   " +
			" )  " +
			")";
	@Query(value= sql, nativeQuery=true)
	List<ProjectPlanEntity> projectPlanAutoMail();

	String sql2 ="select p.`CODE` as code,p.`NAME` as name,hr.EMAIL as email   FROM   " +
			"    PROJECT_PLAN pp   " +
			"        join MASTERPLAN mp   " +
			"             on pp.MASTERPLAN_ID=mp.MASTERPLAN_ID   " +
			"        join PROJECT p   " +
			"             on p.PROJECT_ID=mp.PROJECT_ID   " +
			"        join PROJECT_MEMBER pm   " +
			"             on pm.PROJECT_ID= p.PROJECT_ID   " +
			"        join HUMAN_RESOURCES hr   " +
			"             on hr.HUMAN_RESOURCES_ID=pm.HUMAN_RESOURCE_ID   " +
			"WHERE (pm.ROLE='QM' " +
//			"or pm.ROLE='TL' or pm.ROLE='BM' or pm.ROLE='PM' " +
			") and pp.PROJECT_PLAN_ID=?1 and pm.IS_ACTIVE=1 GROUP BY hr.EMAIL";
	@Query(value= sql2, nativeQuery=true)
	List<IProjectMailDTO> getProjectAndHumanResource(Long id);

	String sql3 = "select p.`CODE`  code ,p.`NAME` name,hr.EMAIL as email, pm.ROLE as role from MASTERPLAN mp\n" +
			"join PROJECT p\n" +
			"on mp.PROJECT_ID=p.PROJECT_ID\n" +
			"join PROJECT_MEMBER pm \n" +
			"on pm.PROJECT_ID=p.PROJECT_ID\n" +
			"join HUMAN_RESOURCES hr\n" +
			"on hr.HUMAN_RESOURCES_ID=pm.HUMAN_RESOURCE_ID WHERE mp.MASTERPLAN_ID=?1 and pm.IS_ACTIVE=1";
	@Query(value= sql3, nativeQuery=true)
	List<IProjectAndHumanMasterplan> getProjectAndHumanMasterplan(Long id);

	String sql4="SELECT p.`CODE` as code ,p.`NAME` as name, hr.EMAIL as email , pm.ROLE as role\n" +
			"FROM PROJECT as p\n" +
			"INNER JOIN PROJECT_MEMBER as pm \n" +
			"on pm.PROJECT_ID=p.PROJECT_ID \n" +
			"INNER JOIN HUMAN_RESOURCES  as hr \n" +
			"on pm.HUMAN_RESOURCE_ID=hr.HUMAN_RESOURCES_ID\n" +
			"WHERE p.PROJECT_ID=?1 and pm.IS_ACTIVE=1 and p.IS_ACTIVE=1 and (pm.ROLE in('PM','BM','TL','QM'))";
	@Query(value=sql4, nativeQuery=true)
	List<IProjectAndHumanMasterplan> getMailByProjectID(Long projectID);

	@Query(value="SELECT EMAIL FROM HUMAN_RESOURCES as hr JOIN APP_PARAMS as ap on hr.DEPARTMENT_ID = ap.APP_PARAMS_ID WHERE ap.PAR_CODE='BOD' AND hr.IS_ACTIVE=1", nativeQuery=true)
	List<IEmailBOD> getEmailBOD();


	@Query(value="SELECT * FROM PROJECT_PLAN as pl JOIN MASTERPLAN as mp on pl.MASTERPLAN_ID=mp.MASTERPLAN_ID " +
			"JOIN PROJECT as p on p.PROJECT_ID=mp.PROJECT_ID WHERE p.PROJECT_ID = ?1", nativeQuery=true)
	List<ProjectPlanEntity> findAllProjectPlanByProjectID(Long id);

	//Lấy ra thông tin pm,ba,tl,qa project
	@Query(value="SELECT hr.HUMAN_RESOURCES_ID as id,hr.USERNAME as username, hr.FIRSTNAME as firstname, hr.LASTNAME as lastname, p.`CODE` as code ,p.`NAME` as nameproject, hr.EMAIL as email , pm.ROLE as role " +
			"FROM PROJECT as p " +
			"INNER JOIN PROJECT_MEMBER as pm " +
			"on pm.PROJECT_ID=p.PROJECT_ID " +
			"INNER JOIN HUMAN_RESOURCES  as hr " +
			"on pm.HUMAN_RESOURCE_ID=hr.HUMAN_RESOURCES_ID " +
			"WHERE p.PROJECT_ID=?1 and pm.IS_ACTIVE=1 and p.IS_ACTIVE=1 and (pm.ROLE in('PM','BM','TL','QM'))", nativeQuery=true)
	List<IHumanResourceOfProject> findAllHumanResourceByProjectID(Long id);
}

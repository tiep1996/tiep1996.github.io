package com.iist.qlda.project.service;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.iist.qlda.project.repository.jparepository.ProjectRepository;
import common.CommonUtils;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iist.qlda.project.dto.ProjectPlanDTO;
import com.iist.qlda.project.repository.customreporsitory.MasterPlanCustomRepository;

@Service
public class ProjectPlanServiceImpl implements ProjectPlanService {
	
	@Autowired
	private MasterPlanCustomRepository masterPlanCustomRepository;

	@Autowired
	private ProjectRepository projectRepository;


	@Override
	public ProjectPlanDTO deleteProjectPlan(List<Long> lstDelete) {
		for(long i: lstDelete) {
		masterPlanCustomRepository.deleteProjectPlan(i);
		}
		return null;
	}

	@Override
	public ByteArrayInputStream exportFile(long id) throws IOException {
		Workbook workbook = null;
		InputStream in = null;
		byte[] exportInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			//TODO: get data:
			List<ProjectPlanDTO> lstProjectPlanDTOS = masterPlanCustomRepository.getListProjectPlanDTO(id);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			for (int i = 0; i < lstProjectPlanDTOS.size(); i++) {

				lstProjectPlanDTOS.get(i).setDeadLineString(df.format(lstProjectPlanDTOS.get(i).getDeadline()));

				if (lstProjectPlanDTOS.get(i).getEndDate() != null) {
					lstProjectPlanDTOS.get(i).setEndDateString(df.format(lstProjectPlanDTOS.get(i).getEndDate()));
				} else {
					lstProjectPlanDTOS.get(i).setEndDateString(" ");
				}

				if (lstProjectPlanDTOS.get(i).getStartDate() != null) {
					lstProjectPlanDTOS.get(i).setStartDateString(df.format(lstProjectPlanDTOS.get(i).getStartDate()));
				} else {
					lstProjectPlanDTOS.get(i).setStartDateString(" ");
				}
				lstProjectPlanDTOS.get(i).setStt(i + 1);
			}

			String nameProject = projectRepository.findNameByProjectId(id).getName();

			//TODO: read file template
			in = CommonUtils.getInputStreamByFileName("master-plan.xlsx");

			Map<String, Object> beans = new HashedMap();
			beans.put("title", nameProject);
			beans.put("books", Collections.singletonList(lstProjectPlanDTOS));

			XLSTransformer transformer = new XLSTransformer();
			workbook = transformer.transformXLS(in, beans);
			workbook.write(byteArrayOutputStream);
			exportInputStream = byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) in.close();
		}
		return new ByteArrayInputStream(exportInputStream);
	}

}

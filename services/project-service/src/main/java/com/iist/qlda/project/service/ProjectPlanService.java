package com.iist.qlda.project.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


import com.iist.qlda.project.dto.ProjectPlanDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


public interface ProjectPlanService {
	ProjectPlanDTO deleteProjectPlan(List<Long> lstDelte);
//	byte[] exportFile(long id, String path) throws InvalidFormatException;
	ByteArrayInputStream exportFile(long id) throws IOException;

}

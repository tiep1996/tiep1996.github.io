package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.DTOSearch;
import com.iist.qlda.project.dto.MasterPlanDTO;

import java.util.List;


public interface MasterPlanService {
	MasterPlanDTO findAllByProjectId(Long i);
	
	void saveProject(MasterPlanDTO dto);

	void confirmProject(MasterPlanDTO dto);
    
	MasterPlanDTO editProject(MasterPlanDTO dto);
	
}

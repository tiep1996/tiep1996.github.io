package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.AppParamDTO;
import com.iist.qlda.project.dto.DTOSearch;
import com.iist.qlda.project.dto.RoleDTO;
import com.iist.qlda.project.entity.AppParamEntity;

import java.util.List;

public interface AppParamService {
    AppParamDTO getAppParamByNam(String name);

    List<AppParamDTO> getPartnerByPartype(DTOSearch dto);

    List<RoleDTO> getCodePosition();
}

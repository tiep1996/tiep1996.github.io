package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.AppParamDTO;
import com.iist.qlda.project.dto.DTOSearch;
import com.iist.qlda.project.dto.RoleDTO;
import com.iist.qlda.project.entity.AppParamEntity;
import com.iist.qlda.project.repository.customreporsitory.AppParamCustomRepository;
import com.iist.qlda.project.repository.jparepository.ApparamJpaRepository;
import common.ErrorCode;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppParamServiceImpl implements AppParamService{
    private final Logger log = LogManager.getLogger(AppParamServiceImpl.class);

    @Autowired
    private ApparamJpaRepository apparamJpaRepository;

    @Autowired
    private AppParamCustomRepository appParamCustomRepository;

    @Override
    public AppParamDTO getAppParamByNam(String name) {
      return null;
    }

    //ANHTT_IIST
    @Override
    public List<AppParamDTO> getPartnerByPartype(DTOSearch dto) {
        log.info("-----------------lay danh sach app-param theo partype va key search----------------");
        List<AppParamDTO> appParamDTOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(appParamCustomRepository.getAppParamBytype(dto))){
            appParamDTOList = appParamCustomRepository.getAppParamBytype(dto);
        }
        return appParamDTOList;
    }

    @Override
    public List<RoleDTO> getCodePosition() {
        log.info("<---- service: getCodePosition ---->");
        try {
            List<AppParamEntity> entities = apparamJpaRepository.findAllCodeByPosition();
            List<RoleDTO> result = new ArrayList<>();
            for (AppParamEntity e: entities) {
                result.add(new RoleDTO(e.getId(),e.getCode(),e.getName()));
            }
            return result;
        }catch (Exception ex){
            log.error(ex);
            throw new CustomExceptionHandler(HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
    }

}

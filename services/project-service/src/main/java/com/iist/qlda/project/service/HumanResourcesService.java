package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.*;
import com.iist.qlda.project.dto.DataPage;
import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.HumanResourcesShowDTO;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import common.ResultResp;


import java.util.List;
import java.util.Map;

public interface HumanResourcesService {
    List<HumanResourcesDTO> getListHumanResourceByNameOrCode(DTOSearch dto);
    HumanResourcesDTO getUserInfo(String username);
    List<HumanResourcesDTO> getListHumanResourceByProjectID(long i);

    //tinpd
    Map<String,Object> updateDataHumanResources(Long id);
    // end
    /*duc service*/
    HumanResourcesDTO create(HumanResourcesDTO humanResourcesDTO);

    HumanResourcesDTO update(HumanResourcesDTO humanResourcesDTO);

    List<IGroupUserDTO> getAllGroupUser();

    List<ICentersDTO> getCenters();

    HumanResourcesDTO findById(Long Id);

    HumanResourcesDTO findByCode(String code);

    HumanResourcesDTO checkUsername(String username);

    List<HumanResourcesDTO> findByEmail(String email);

    HumanResourcesDTO getByEmail(String email);

    ResultResp resetPassword(Long userID,String usernameAdmin);

    /*end */

    //TanNV
    DataPage<HumanResourcesShowDTO> getPageHumanResourcesSeach(HumanResourcesShowDTO dto);
    Boolean deleteHumanResources(Long id);
    Integer getActiveFromHumanResourceId(Long id);
    // end TanNV
//hungnv change password
    Long changePassword (HumanResourcesDTO humanResourcesDTO);
    Long checkPassword(HumanResourcesDTO humanResourcesDTO);
}

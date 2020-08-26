//package com.iist.qlda.project.repository.customreporsitory;
//import com.iist.qlda.project.dto.PermissionDTO;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Repository;
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//@Repository
//public class decentralizationCustomRepository {
//    EntityManager em;
//    public List<PermissionDTO> getListHumanResourceByNameOrCode(PermissionDTO dto){
//        StringBuilder sql = new StringBuilder();
//        sql.append("select DISTINCT pm.`CODE` , pm.`NAME` , pm.IS_ACTIVE,pm.DESCRIPTION from \n" +
//                "PERMISSION_GROUP_USER pgu JOIN PERMISSION pm on pgu.PERMISSION_ID = pm.PERMISSION_ID\n" +
//                "join GROUP_USER gu on pgu.GROUP_USER_ID= gu.GROUP_USER_ID\n" +
//                "where 1=1 and  pm.IS_ACTIVE = 1 \n");
//        if(StringUtils.isNotBlank(dto.getCode())){
//            sql.append(" and  upper(pm.`CODE`) LIKE upper(:code))\n" );
//        }
//        if(StringUtils.isNotBlank(dto.getName())){
//            sql.append(" and  upper(pm.`NAME`) LIKE upper(:code))\n" );
//        }
//        Query query = em.createNativeQuery(sql.toString());
//        List<Object[]> lstObject = query.getResultList();
//        return convertObjectToDto(lstObject);
//    }
//    public List<PermissionDTO> convertObjectToDto(List<Object[]> lstObject){
//        List<PermissionDTO> listDto = new ArrayList<>();
//        if(CollectionUtils.isNotEmpty(lstObject)){
//            for(Object[] obj : lstObject){
//                PermissionDTO resourcesDTO = new PermissionDTO();
//                resourcesDTO.setCode(( obj[0]).toString());
//                resourcesDTO.setName(obj[1].toString());
//                resourcesDTO.setDescription(obj[2].toString());
//                resourcesDTO.setIsActive(((BigInteger) obj[3]).intValue());
//                listDto.add(resourcesDTO);
//            }
//        }
//
//        return listDto;
//    }
//}

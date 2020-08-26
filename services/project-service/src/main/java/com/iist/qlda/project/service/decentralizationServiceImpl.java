//package com.iist.qlda.project.service;
//
//import com.iist.qlda.project.dto.PermissionDTO;
//import com.iist.qlda.project.entity.PermissionEntity;
//import com.iist.qlda.project.repository.decentralizationRepository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//public class decentralizationServiceImpl implements decentralizationService {
//    decentralizationRepository decentralizationRepository;
//    EntityManager em;
//    @Override
//    public List<PermissionDTO> findByParams(PermissionDTO dto) {
//
//
//
//
//        return null;
//    }
//
//    @Override
//    public void saveDecentralization(PermissionDTO organizationDto) {
//        if (organizationDto.getPermissionId() != null) {
//            Optional<PermissionEntity> organizationSave = this.decentralizationRepository.findById(organizationDto.getPermissionId());
//            if (organizationSave.isPresent()) {
//                String pathChildren = organizationSave.get().getPathId();
//                List<PermissionDTO>getPermisstion = getListPemisstionByGroupUser(organizationDto.getParentId(),organizationDto.getDescription());
//                List<PermissionDTO> checkPermission = organizationDto.getCheckPermission();
//                for(PermissionDTO check :checkPermission){
//                    for(PermissionDTO Permisstion: getPermisstion){
//
//
//                    }
//                }
//                /*  this.decentralizationRepository.sa*/
//                organizationSave.get().setName(organizationDto.getName());
//                organizationSave.get().setCode(organizationDto.getCode());
//                organizationSave.get().setDescription(organizationDto.getDescription());
//                organizationSave.get().setParentId(organizationDto.getParentId());
//                this.decentralizationRepository.save(organizationSave.get());
//            }
//        }
//        PermissionEntity organizationEntity = new PermissionEntity();
//        organizationEntity.setCreateDate(new Date());
//        organizationEntity.setCode(organizationDto.getCode());
//        organizationEntity.setDescription(organizationDto.getDescription());
//        organizationEntity.setName(organizationDto.getName());
//        organizationEntity.setParentId(organizationDto.getParentId());
//        organizationEntity.setIsActive(1);
//        PermissionEntity organizationEntityAdd = this.decentralizationRepository.save(organizationEntity);
//        Optional<PermissionEntity> organizationEntities = this.decentralizationRepository.findById(organizationDto.getParentId());
//        if (organizationEntities.isPresent()) {
//            String pathId = organizationEntities.get().getPathId() + organizationEntityAdd.getPermissionId() + "/";
//            organizationEntityAdd.setPathId(pathId);
//        }
//        this.decentralizationRepository.save(organizationEntity);
//    }
//
//    public List<PermissionDTO> getListPemisstionByGroupUser( Long groupUser , String code ){
//        StringBuilder sql = new StringBuilder();
//        sql.append("select ps.PERMISSION_ID as pemission ,ps.CODE as codào  from PERMISSION ps join PERMISSION_GROUP_USER gsu on " +
//                   "ps.PERMISSION_ID = gsu.PERMISSION_ID  where gsu.GROUP_USER_ID = :groupUser and ps.IS_ACTIVE =1  ");
//        Query query = em.createNativeQuery(sql.toString());
//        query.setParameter("groupUser",groupUser );
//        List<Object[]> lstObject = query.getResultList();
//        List<PermissionDTO> listPemission= new ArrayList<>();
//        for(Object[] objects: lstObject){
//            PermissionDTO  permissionDTO= new PermissionDTO();
//            permissionDTO.setPermissionId(((BigInteger) objects[0]).longValue());
//            permissionDTO.setCode(objects[1].toString());
//            listPemission.add(permissionDTO);
//        }
//        return listPemission;
//    }
//
//
//}

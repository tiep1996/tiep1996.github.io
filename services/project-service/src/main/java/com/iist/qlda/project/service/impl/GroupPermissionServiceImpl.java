package com.iist.qlda.project.service.impl;

import com.iist.qlda.project.dto.*;
import com.iist.qlda.project.entity.*;
import com.iist.qlda.project.repository.jparepository.*;
import com.iist.qlda.project.service.GroupPermissionService;
import com.iist.qlda.project.service.mapper.DepartmentMapper;
import com.iist.qlda.project.service.mapper.GroupPermissionMapper;
import com.iist.qlda.project.service.mapper.HumanResourcesMapper;
import com.iist.qlda.project.service.mapper.PermissionMapper;
import common.CommonUtils;
import common.Constants;
import common.ErrorCode;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author dangnp
 * @created 25/07/2020 - 9:51 AM
 * @project services
 **/

@Service
public class GroupPermissionServiceImpl implements GroupPermissionService {

    private final Logger log = LogManager.getLogger(GroupPermissionService.class);
    @Autowired
    private GroupPermissionRepository gpRepository;
    @Autowired
    private GroupPermissionUserRepository gpUserRepository;
    @Autowired
    private GroupPermissionPermissionRepository gpPermissionRepository;
    @Autowired
    private HumanResourcesRepository hrRepository;
    @Autowired
    private GroupPermissionMapper gpMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private HumanResourcesMapper humanResourcesMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private EntityManager em;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)
    public GroupPermissionDTO create(GroupPermissionDTO dto) {
        log.info("<--- service - create: start --->");
        log.info("<--- validation Params --->");
        CommonUtils.validParams(dto);
        log.info("<--- save group permission --->");
        GroupPermissionEntity result = null;
        try {
            result = gpRepository.findIsActiveByCode(dto.getCode());
            if (null == result) {
                dto.setState(1);
                result = gpRepository.save(gpMapper.toEntity(dto));
                saveListPermissionsForGroupPermission(result.getId(), dto.getPermissionList());
                log.info("<--- result: " + result + " --->");
                log.info("<--- service - create: success --->");
                return gpMapper.toDto(result);
            } else {
                throw new CustomExceptionHandler(HttpStatus.BAD_REQUEST, ErrorCode.PERMISSION_LIST_EXIST);
            }
        } catch (CustomExceptionHandler ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            log.error("<--- save group permission: " + Constants.SAVE_GROUP_PERMISSION_ERROR + " --->");
            throw new CustomExceptionHandler(Constants.SAVE_GROUP_PERMISSION_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.SAVE_GROUP_PERMISSION_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)
    public GroupPermissionDTO update(GroupPermissionDTO dto, Long id) {
        log.info("<--- service - update: start --->");
        log.info("<--- validation Params --->");
        CommonUtils.validParams(dto);
        GroupPermissionEntity entity = null;
        try {
            entity = gpRepository.findById(id).get();
            if(null == entity){
                log.error("<--- save group permission: " + Constants.OBJ_NOT_FOUND + " --->");
                throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
            }
            entity.setName(dto.getName());
            entity.setNote(dto.getNote());
            entity.setStatus(dto.getStatus());
            GroupPermissionEntity result = gpRepository.save(entity);
            saveListPermissionsForGroupPermission(id, dto.getPermissionList());
            log.info("<--- result: " + result + " --->");
            log.info("<--- service - create: success --->");
            return gpMapper.toDto(result);
        } catch (CustomExceptionHandler ex) {
            log.error(ex);
            throw ex;
        } catch (Exception ex) {
            log.error("<--- save group permission: " + Constants.SAVE_GROUP_PERMISSION_ERROR + " --->");
            throw new CustomExceptionHandler(Constants.SAVE_GROUP_PERMISSION_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.SAVE_GROUP_PERMISSION_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)
    public GroupPermissionDTO delete(Long id) {
        log.info("<--- service - delete: start --->");
        try {
            GroupPermissionEntity entity = gpRepository.findById(id).get();
            entity.setState(Constants.DELETE_GROUP_PERMISSION);
            gpRepository.save(entity);
            deleteListPermissionsForGroupPermission(id);
            return gpMapper.toDto(entity);
        } catch (Exception ex) {
            log.error("<--- service - update: " + Constants.OBJ_NOT_FOUND + " --->");
            throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }

    }

    @Override
    public GroupPermissionDTO checkedGroupPermission(Long id) {
        log.info("<--- service - checkedGroupPemission: start --->");
        try {
            GroupPermissionEntity entity = gpRepository.findById(id).get();
            if (entity.getStatus() == Constants.INACTIVE_GROUP_PERMISSION) {
                log.error("<--- service - checkedGroupPermission: " + Constants.GROUP_PERMISSION_INACTIVE + " --->");
                throw new CustomExceptionHandler(Constants.GROUP_PERMISSION_INACTIVE, HttpStatus.BAD_REQUEST, ErrorCode.GROUP_PERMISSION_INACTIVE);
            }
            return gpMapper.toDto(entity);
        } catch (Exception ex) {
            log.error("<--- service - checkedGroupPermission: " + Constants.OBJ_NOT_FOUND + " --->");
            throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
    }

    @Override
    public List<String> getChilPermission(String code) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = CustomExceptionHandler.class)

    //TanNV

    public void addUser(Long groupId, Long userId) {
        log.info("<--- service - add user: start --->");
        GroupPermissionUserEntity gpUser = null;
        GroupPermissionEntity gpEntity = null;
        HumanResourcesEntity hrEntity = null;
        try {
            gpEntity = gpRepository.findById(groupId).get();
        } catch (Exception ex) {
            log.error("<--- GroupPermission : " + gpEntity + " : " + Constants.OBJ_NOT_FOUND);
            throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
        try {
            hrEntity = hrRepository.findById(userId).get();
        } catch (Exception ex) {
            log.error("<--- HumanRescource : " + hrEntity + " : " + Constants.USER_NOT_FOUND);
            throw new CustomExceptionHandler(Constants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
        try {
            gpUser = gpUserRepository.findByGroupPermissionIdAndUserId(groupId, userId);
            if (null != gpUser) {
                log.error("<--- " + Constants.OBJ_EXIST + " --->");
                throw new CustomExceptionHandler(Constants.OBJ_EXIST, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_EXIST);
            }
        } catch (NoSuchElementException ex) {
            gpUser = new GroupPermissionUserEntity(groupId, userId);
        } catch (Exception ex) {
            log.error("<--- server error --->");
            throw new CustomExceptionHandler(Constants.SERVER_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.SERVER_ERROR);
        }
        gpUser = new GroupPermissionUserEntity(groupId, userId);
        if (null != gpUser) {
            log.info("<--- save record GroupPermissionUser --->");
            GroupPermissionUserEntity gpUserEntity = gpUserRepository.save(gpUser);
            log.info("<--- service - add user: success --->");
            if (null == gpUserEntity) {
                log.error("<--- " + Constants.SAVE_USER_ERROR + " --->");
                throw new CustomExceptionHandler(Constants.SAVE_USER_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.SAVE_USER_ERROR);
            }
        }
    }

    @Override
    public List<IHumanResourcesShowDTO> getlistHumanResourcesDepatment(String name) {
        if (name != null) {
            name = "%" + name + "%";
            List<IHumanResourcesShowDTO> list = gpUserRepository.getlistHumanResourcesDepatment(name);
            return list;
        } else {
            return null;
        }


    }

    @Override
    public void addUserPermission(List<Long> lstId) {
        List<GroupPermissionUserEntity> groupPermissionUserEntityList = new ArrayList<>();
        for(int i = 1; i<lstId.size(); i++) {
            if (null == gpUserRepository.findByGroupPermissionIdAndUserId(lstId.get(0), lstId.get(i))) {
                GroupPermissionUserEntity pGrEntity = new GroupPermissionUserEntity();
                pGrEntity.setGroupPermissionId(lstId.get(0));
                pGrEntity.setUserId(lstId.get(i));
                groupPermissionUserEntityList.add(pGrEntity);
            }
        }
        gpUserRepository.saveAll(groupPermissionUserEntityList);

    }

    @Override
    @Transactional
    public void deleteUserPermission(List<Long> lstId) {
        for(int i = 1; i<lstId.size(); i++) {
            gpUserRepository.deleteByGroupPermissionIdAndUserId(lstId.get(0), lstId.get(i));
        }

    }

    @Override
    public GroupPermissionUserEntity isUserAdded(Long uId){
        return gpUserRepository.findByUserId(uId);
    }

    @Override
    public List<HumanResourcesShowDTO> viewUserPermission(Long grId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  hr.HUMAN_RESOURCES_ID as humanResourceId, hr.USERNAME as username, ap.PAR_CODE  as parcode ");
        sql.append("FROM HUMAN_RESOURCES as hr ");
        sql.append("LEFT JOIN APP_PARAMS as ap ");
        sql.append("on hr.DEPARTMENT_ID = ap.APP_PARAMS_ID ");
        sql.append("INNER JOIN GROUPPERMISSION_USER grU ");
        sql.append("ON grU.USER_ID = hr.HUMAN_RESOURCES_ID ");
        sql.append("WHERE  grU.GROUP_PERMISSION_ID = ?1");
        Query q = em.createNativeQuery(sql.toString());
        q.setParameter(1, grId);
        List<Object[]> lstObj = null;
        try {
            lstObj = q.getResultList();
        }catch(NullPointerException e){
        }
        return convertObjectToDto(lstObj);
    }

    public List<HumanResourcesShowDTO> convertObjectToDto(List<Object[]> lstObject) {
        List<HumanResourcesShowDTO> listDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstObject)) {
            for (Object[] obj : lstObject) {
                HumanResourcesShowDTO humanDto = new HumanResourcesShowDTO();
                humanDto.setHumanResourceId((BigInteger) obj[0]);
                humanDto.setUsername((String) obj[1]);
                humanDto.setParcode((String) obj[2]);

                listDto.add(humanDto);
            }
        }

        return listDto;
    }
    // End TanNV

    @Override
    public GroupPermissionDTO findById(Long id) {
        log.info("<--- service - findById:" + id + " start --->");
        try {
            GroupPermissionEntity entity = gpRepository.findById(id).get();
            log.info("<--- service - findById:" + id + " success --->");
            return gpMapper.toDto(entity);
        } catch (Exception ex) {
            log.error("<--- GroupPermission: " + Constants.OBJ_NOT_FOUND + " --->");
            throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
    }

    @Override
    public PageApp<GroupPermissionDTO> findAll(PageApp page) {
        log.info("<--- service - findAll: start  --->");
        try {
            Pageable pageable = null;
            if (page.isASC()) {
                pageable = PageRequest.of(page.getPageIndex(), page.getItemsPerPage(), Sort.by(page.getSortBy()).ascending());
            } else {
                pageable = PageRequest.of(page.getPageIndex(), page.getItemsPerPage(), Sort.by(page.getSortBy()).descending());
            }
            Page<GroupPermissionEntity> entity = gpRepository.findAll(pageable);
            PageApp<GroupPermissionDTO> result = new PageApp<>(page.getPageIndex(), page.getItemsPerPage(), page.getSort());
            result.setTotalItems(entity.getTotalElements());
            List<GroupPermissionDTO> dto = gpMapper.toDto(entity.getContent());
            for (GroupPermissionDTO d : dto) {
                d.setPermissionList(permissionMapper.toDto(gpPermissionRepository.findByGroupPermissionId(d.getId())));
            }
            result.setData(dto);
            log.info("<--- service - findAll: success  --->");
            log.info(result);
            return result;
        } catch (Exception ex) {
            log.error("<--- service - findAll: " + Constants.OBJ_NOT_FOUND + "  --->");
            log.error(ex);
            throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
    }

    @Override
    public PageApp<GroupPermissionDTO> searchAllByUnit(String code, String name, String status, PageApp page) {

        try {
            Pageable pageable = null;
            if (page.isASC()) {
                pageable = PageRequest.of(page.getPageIndex(), page.getItemsPerPage(), Sort.by(page.getSortBy()).ascending());
            } else {
                pageable = PageRequest.of(page.getPageIndex(), page.getItemsPerPage(), Sort.by(page.getSortBy()).descending());
            }
            Page<GroupPermissionEntity> entity = gpRepository.searchAllByUnit(code, name, status, pageable);
            PageApp<GroupPermissionDTO> result = new PageApp<>(page.getPageIndex(), page.getItemsPerPage(), page.getSort());
            result.setTotalItems(entity.getTotalElements());
            List<GroupPermissionDTO> dto = gpMapper.toDto(entity.getContent());
            result.setData(dto);
            log.info("<--- service - find by unit: success  --->");
            log.info(result);
            return result;
        } catch (Exception ex) {
            log.error("<--- service - find by unit: " + Constants.OBJ_NOT_FOUND + "  --->");
            log.error(ex);
            throw new CustomExceptionHandler(Constants.OBJ_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.OBJ_NOT_FOUND);
        }
    }

    @Override
    public List<GroupPermissionDTO> findAllByStatus(String status) {
        return null;
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void saveListPermissionsForGroupPermission(Long id, List<PermissionDTO> list) {
        log.info("<--- save list permission for group permission: start --->");
        if (gpPermissionRepository.findByGroupPermissionId(id).isEmpty()) {
            log.info("<--- new records GroupPermissionPermission --->");
            for (PermissionDTO e : list) {
                GroupPermissionPermissionEntity gppEntity = new GroupPermissionPermissionEntity(id, e.getPermissionId());
                try {
                    GroupPermissionPermissionEntity entity = gpPermissionRepository.save(gppEntity);
                } catch (Exception ex) {
                    log.error("<--- new records GroupPermissionPermission: " + Constants.SAVE_PERMISSIONS_ERROR + " --->");
                    throw new CustomExceptionHandler(Constants.SAVE_PERMISSIONS_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.SAVE_PERMISSIONS_ERROR);
                }
            }
        } else {
            log.info("<--- update records GroupPermissionPermission --->");
            deleteListPermissionsForGroupPermission(id);
            log.info("<--- new records GroupPermissionPermission --->");
            for (PermissionDTO e : list) {
                GroupPermissionPermissionEntity gppEntity = new GroupPermissionPermissionEntity(id, e.getPermissionId());
                try {
                    GroupPermissionPermissionEntity entity = gpPermissionRepository.save(gppEntity);
                } catch (Exception ex) {
                    log.error("<--- new records GroupPermissionPermission: " + Constants.SAVE_PERMISSIONS_ERROR + " --->");
                    throw new CustomExceptionHandler(Constants.SAVE_PERMISSIONS_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.SAVE_PERMISSIONS_ERROR);
                }
            }
        }
        log.info("<--- save list departments for group permission: success --->");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteListPermissionsForGroupPermission(Long id) {
        log.info("<--- delete records GroupPermissionPermission old - groupPermiisionId = " + id + " --->");
        try {
            gpPermissionRepository.deleteByGroupPermissionId(id);
            log.info("<--- delete records GroupPermissionPermission old - groupPermiisionId = " + id + " :success --->");
        } catch (Exception ex) {
            log.info("<--- delete records GroupPermissionPermission old - groupPermiisionId = " + id + " :error --->");
            throw new CustomExceptionHandler(Constants.DELETE_PERMISSIONS_ERROR, HttpStatus.BAD_REQUEST, ErrorCode.DELETE_PERMISSIONS_ERROR);
        }
    }

    private void addParentPermission(List<PermissionDTO> list, long groupPermissionId) {
        List<PermissionEntity> listParent = permissionRepository.findAllByLevel(1);
        for (PermissionEntity parent : listParent) {
            boolean checkAll = true;
            List<PermissionEntity> es = permissionRepository.findByParentIdIsActive(parent.getPermissionId());
            for (PermissionEntity e : es) {
                boolean check = false;
                for (PermissionDTO d : list) {
                    if (e.getPermissionId() == d.getPermissionId()) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    checkAll = false;
                    break;
                }
            }
            if (checkAll) {
                GroupPermissionPermissionEntity gppEntity = new GroupPermissionPermissionEntity(groupPermissionId, parent.getPermissionId());
                gpPermissionRepository.save(gppEntity);
            }
        }
    }


}

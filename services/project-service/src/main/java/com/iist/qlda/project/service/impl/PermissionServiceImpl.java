package com.iist.qlda.project.service.impl;

import com.iist.qlda.project.dto.GroupPermissionDTO;
import com.iist.qlda.project.dto.PermissionDTO;
import com.iist.qlda.project.dto.TreeDTO;
import com.iist.qlda.project.entity.GroupPermissionPermissionEntity;
import com.iist.qlda.project.entity.PermissionEntity;
import com.iist.qlda.project.repository.jparepository.GroupPermissionPermissionRepository;
import com.iist.qlda.project.repository.jparepository.GroupPermissionRepository;
import com.iist.qlda.project.repository.jparepository.PermissionRepository;
import com.iist.qlda.project.service.PermissionService;
import com.iist.qlda.project.service.mapper.GroupPermissionMapper;
import com.iist.qlda.project.service.mapper.PermissionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final Logger log = LogManager.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    @Autowired
    private GroupPermissionMapper groupPermissionMapper;

    @Autowired
    private GroupPermissionPermissionRepository gpPermissionRepository;

    @Override
    public List<PermissionDTO> findAllPermissionIsActive() {
        log.info("<--- serviceImpl - findAllIsActive: start --->");
        List<PermissionDTO> result = new ArrayList<>();
        try {
            List<PermissionEntity> pml = permissionRepository.findAllIsActive();
            log.info("<--- find all permission is active: ");
            log.info(pml);
            for (PermissionEntity permissionEntity : pml) {
                PermissionDTO dto = permissionMapper.toDto(permissionEntity);
                if (permissionEntity.getLevel() == 1) {
                    dto.setPermissionChilds(setPermissions(permissionEntity.getPermissionId()));
                    result.add(dto);
                }
            }
            log.info("<--- get list permission dto: ");
            log.info(result);
            log.info("<--- serviceImpl - findAllIsActive: end --->");
            return result;
        } catch (Exception ex) {
            log.error("<--- findAllIsActive: server error");
            log.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<GroupPermissionDTO> findAllPermissionByGoupPermission() {
        return null;
    }

    private List<PermissionDTO> setPermissions(long id) {
        List<PermissionDTO> result = new ArrayList<>();
        List<PermissionEntity> list = permissionRepository.findByParentIdIsActive(id);
        if (list.isEmpty()) {
            return result;
        }
        result = permissionMapper.toDto(list);
        result.forEach(e -> {
            e.setPermissionChilds(setPermissions(e.getPermissionId()));
        });
        return result;
    }

    @Override
    public List<TreeDTO> getTreePermission(Long groupPermissionId) {
        List<TreeDTO> trees = new ArrayList<>();
        List<PermissionEntity> pml = permissionRepository.findAllIsActive();
        if (null == groupPermissionId) {
            for (PermissionEntity permissionEntity : pml) {
                if (permissionEntity.getLevel() == 1) {
                    TreeDTO tree = new TreeDTO();
                    tree.setText(permissionEntity.getName());
                    tree.setValue(permissionEntity.getPermissionId());
                    tree.setChecked(false);
                    tree.setChildren(getTreesPermission(permissionEntity.getPermissionId()));
                    trees.add(tree);
                }
            }
        } else {
            for (PermissionEntity permissionEntity : pml) {
                if (permissionEntity.getLevel() == 1) {
                    TreeDTO tree = new TreeDTO();
                    tree.setText(permissionEntity.getName());
                    tree.setValue(permissionEntity.getPermissionId());
                    tree.setChecked(isCheckedPermission(groupPermissionId, permissionEntity.getPermissionId()));
                    tree.setChildren(getTreePermissionByGroupPermissionId(groupPermissionId, permissionEntity.getPermissionId()));
                    trees.add(tree);
                }
            }
        }
        return trees;
    }

    private List<TreeDTO> getTreesPermission(Long permissionId) {
        List<TreeDTO> trees = null;
        List<PermissionEntity> list = permissionRepository.findByParentIdIsActive(permissionId);
        if (list.isEmpty()) {
            return trees;
        }
        trees = new ArrayList<>();
        for (PermissionEntity e : list) {
            TreeDTO tree = new TreeDTO();
            tree.setText(e.getName());
            tree.setValue(e.getPermissionId());
            tree.setChecked(false);
            tree.setChildren(getTreesPermission(e.getPermissionId()));
            trees.add(tree);
        }
        return trees;
    }

    private List<TreeDTO> getTreePermissionByGroupPermissionId(long groupPermissionId, long permissionId) {
        List<TreeDTO> trees = null;
        List<PermissionEntity> list = permissionRepository.findByParentIdIsActive(permissionId);
        if (list.isEmpty()) {
            return trees;
        }
        trees = new ArrayList<>();
        for (PermissionEntity e : list) {
            TreeDTO tree = new TreeDTO();
            tree.setText(e.getName());
            tree.setValue(e.getPermissionId());
            tree.setChecked(isCheckedPermission(groupPermissionId, e.getPermissionId()));
            tree.setChildren(getTreePermissionByGroupPermissionId(groupPermissionId, e.getPermissionId()));
            trees.add(tree);
        }
        return trees;
    }

    private boolean isCheckedPermission(long groupPermissionId, long permissionId) {
        GroupPermissionPermissionEntity entity = null;
        try {
            entity = gpPermissionRepository.findByGroupPermissionIdAndPermissionId(groupPermissionId, permissionId);
            if (null != entity) {
                return true;
            }
        } catch (Exception ex) {
            log.error("<--- isCheckedPermission: error --->");
        }
        return false;
    }

}

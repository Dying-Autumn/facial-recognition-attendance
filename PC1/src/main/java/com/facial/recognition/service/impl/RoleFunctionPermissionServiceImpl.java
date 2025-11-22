package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.RoleFunctionPermission;
import com.facial.recognition.repository.RoleFunctionPermissionRepository;
import com.facial.recognition.service.RoleFunctionPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleFunctionPermissionServiceImpl implements RoleFunctionPermissionService {

    @Autowired
    private RoleFunctionPermissionRepository roleFunctionPermissionRepository;

    @Override
    public RoleFunctionPermission createPermission(RoleFunctionPermission permission) {
        permission.setCreatedTime(LocalDateTime.now());
        permission.setUpdatedTime(LocalDateTime.now());
        if (permission.getGranted() == null) {
            permission.setGranted(true);
        }
        return roleFunctionPermissionRepository.save(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleFunctionPermission> findByRoleIdAndFunctionId(Integer roleId, Integer functionId) {
        return roleFunctionPermissionRepository.findByRoleIdAndFunctionId(roleId, functionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findByRoleId(Integer roleId) {
        return roleFunctionPermissionRepository.findByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findByFunctionId(Integer functionId) {
        return roleFunctionPermissionRepository.findByFunctionId(functionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findByPermissionType(String permissionType) {
        return roleFunctionPermissionRepository.findByPermissionType(permissionType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findByGranted(Boolean granted) {
        return roleFunctionPermissionRepository.findByGranted(granted);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findByRoleIdAndGranted(Integer roleId, Boolean granted) {
        return roleFunctionPermissionRepository.findByRoleIdAndGranted(roleId, granted);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findByFunctionIdAndGranted(Integer functionId, Boolean granted) {
        return roleFunctionPermissionRepository.findByFunctionIdAndGranted(functionId, granted);
    }

    @Override
    public RoleFunctionPermission updatePermission(Integer roleId, Integer functionId, RoleFunctionPermission permission) {
        return roleFunctionPermissionRepository.findByRoleIdAndFunctionId(roleId, functionId)
                .map(existing -> {
                    existing.setPermissionType(permission.getPermissionType());
                    existing.setGranted(permission.getGranted());
                    existing.setUpdatedTime(LocalDateTime.now());
                    return roleFunctionPermissionRepository.save(existing);
                })
                .orElseGet(() -> {
                    permission.setRoleId(roleId);
                    permission.setFunctionId(functionId);
                    return createPermission(permission);
                });
    }

    @Override
    public RoleFunctionPermission updateGranted(Integer roleId, Integer functionId, Boolean granted) {
        return roleFunctionPermissionRepository.findByRoleIdAndFunctionId(roleId, functionId)
                .map(existing -> {
                    existing.setGranted(granted);
                    existing.setUpdatedTime(LocalDateTime.now());
                    return roleFunctionPermissionRepository.save(existing);
                })
                .orElseGet(() -> {
                    RoleFunctionPermission newPermission = new RoleFunctionPermission(roleId, functionId, "READ", granted);
                    return createPermission(newPermission);
                });
    }

    @Override
    public void deletePermission(Integer roleId, Integer functionId) {
        roleFunctionPermissionRepository.findByRoleIdAndFunctionId(roleId, functionId)
                .ifPresent(roleFunctionPermissionRepository::delete);
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        roleFunctionPermissionRepository.deleteByRoleId(roleId);
    }

    @Override
    public void deleteByFunctionId(Integer functionId) {
        roleFunctionPermissionRepository.deleteByFunctionId(functionId);
    }

    @Override
    public List<RoleFunctionPermission> batchCreatePermissions(List<RoleFunctionPermission> permissions) {
        permissions.forEach(permission -> {
            permission.setCreatedTime(LocalDateTime.now());
            permission.setUpdatedTime(LocalDateTime.now());
            if (permission.getGranted() == null) {
                permission.setGranted(true);
            }
        });
        return roleFunctionPermissionRepository.saveAll(permissions);
    }

    @Override
    public List<RoleFunctionPermission> batchUpdatePermissions(Integer roleId, List<RoleFunctionPermission> permissions) {
        // 先删除该角色的所有权限
        deleteByRoleId(roleId);
        // 然后批量创建新权限
        permissions.forEach(permission -> permission.setRoleId(roleId));
        return batchCreatePermissions(permissions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findUserPermissionsForFunction(Integer userId, Integer functionId) {
        return roleFunctionPermissionRepository.findUserPermissionsForFunction(userId, functionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleFunctionPermission> findGrantedPermissionsByRoleId(Integer roleId) {
        return roleFunctionPermissionRepository.findGrantedPermissionsByRoleId(roleId);
    }
}


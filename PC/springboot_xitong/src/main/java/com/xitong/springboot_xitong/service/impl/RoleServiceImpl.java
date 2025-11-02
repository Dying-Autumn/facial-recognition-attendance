package com.xitong.springboot_xitong.service.impl;

import com.xitong.springboot_xitong.pojo.Role;
import com.xitong.springboot_xitong.repository.RoleRepository;
import com.xitong.springboot_xitong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        role.setCreatedTime(LocalDateTime.now());
        role.setUpdatedTime(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Integer roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findByStatus(String status) {
        return roleRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findActiveRoles() {
        return roleRepository.findByStatusOrderByRoleNameAsc("ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findByRoleNameContaining(String roleName) {
        return roleRepository.findByRoleNameContaining(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findByDescriptionContaining(String description) {
        return roleRepository.findByDescriptionContaining(description);
    }

    @Override
    public Role updateRole(Integer roleId, Role role) {
        Optional<Role> existingRole = roleRepository.findById(roleId);
        if (existingRole.isPresent()) {
            Role updatedRole = existingRole.get();
            updatedRole.setRoleName(role.getRoleName());
            updatedRole.setDescription(role.getDescription());
            updatedRole.setStatus(role.getStatus());
            updatedRole.setUpdatedTime(LocalDateTime.now());
            return roleRepository.save(updatedRole);
        }
        throw new RuntimeException("Role not found with id: " + roleId);
    }

    @Override
    public Role updateStatus(Integer roleId, String status) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            role.get().setStatus(status);
            role.get().setUpdatedTime(LocalDateTime.now());
            return roleRepository.save(role.get());
        }
        throw new RuntimeException("Role not found with id: " + roleId);
    }

    @Override
    public void deleteRole(Integer roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findRoleByUserId(Integer userId) {
        return roleRepository.findRoleByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveRoles() {
        return roleRepository.countActiveRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).isPresent();
    }
}

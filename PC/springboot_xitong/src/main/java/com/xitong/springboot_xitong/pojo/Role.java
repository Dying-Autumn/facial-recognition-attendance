package com.xitong.springboot_xitong.pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleId;

    @Column(nullable = false, unique = true)
    private String roleName; // 角色名称

    private String description; // 角色描述

    @Column(name = "created_time")
    private java.time.LocalDateTime createdTime; // 创建时间

    @Column(name = "updated_time")
    private java.time.LocalDateTime updatedTime; // 更新时间

    private String status; // 状态：ACTIVE, INACTIVE

    public Role() {}

    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
        this.createdTime = java.time.LocalDateTime.now();
        this.updatedTime = java.time.LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public java.time.LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(java.time.LocalDateTime createdTime) { this.createdTime = createdTime; }

    public java.time.LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(java.time.LocalDateTime updatedTime) { this.updatedTime = updatedTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

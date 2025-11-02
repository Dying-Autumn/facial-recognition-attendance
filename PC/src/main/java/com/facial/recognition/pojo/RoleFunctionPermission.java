package com.facial.recognition.pojo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_function_permissions")
@IdClass(RoleFunctionPermissionId.class)
public class RoleFunctionPermission implements Serializable {
    
    @Id
    @Column(name = "RoleID", nullable = false)
    private Integer roleId; // 主键，外键

    @Id
    @Column(name = "FunctionID", nullable = false)
    private Integer functionId; // 主键，外键

    @Column(name = "permission_type")
    private String permissionType; // 权限类型：READ, WRITE, DELETE, EXECUTE

    @Column(name = "granted")
    private Boolean granted; // 是否授权

    @Column(name = "created_time")
    private LocalDateTime createdTime; // 创建时间

    @Column(name = "updated_time")
    private LocalDateTime updatedTime; // 更新时间

    public RoleFunctionPermission() {}

    public RoleFunctionPermission(Integer roleId, Integer functionId, String permissionType, Boolean granted) {
        this.roleId = roleId;
        this.functionId = functionId;
        this.permissionType = permissionType;
        this.granted = granted;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public Integer getFunctionId() { return functionId; }
    public void setFunctionId(Integer functionId) { this.functionId = functionId; }

    public String getPermissionType() { return permissionType; }
    public void setPermissionType(String permissionType) { this.permissionType = permissionType; }

    public Boolean getGranted() { return granted; }
    public void setGranted(Boolean granted) { this.granted = granted; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}

// 复合主键类
class RoleFunctionPermissionId implements Serializable {
    private Integer roleId;
    private Integer functionId;

    public RoleFunctionPermissionId() {}

    public RoleFunctionPermissionId(Integer roleId, Integer functionId) {
        this.roleId = roleId;
        this.functionId = functionId;
    }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public Integer getFunctionId() { return functionId; }
    public void setFunctionId(Integer functionId) { this.functionId = functionId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleFunctionPermissionId that = (RoleFunctionPermissionId) o;
        return roleId.equals(that.roleId) && functionId.equals(that.functionId);
    }

    @Override
    public int hashCode() {
        return roleId.hashCode() + functionId.hashCode();
    }
}


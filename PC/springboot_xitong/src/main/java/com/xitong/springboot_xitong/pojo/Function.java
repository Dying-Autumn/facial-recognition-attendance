package com.xitong.springboot_xitong.pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "function_entities")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FunctionID")
    private Integer functionId;

    @Column(nullable = false)
    private String functionName; // 功能名称

    private String description; // 功能描述

    @Column(name = "function_code", unique = true)
    private String functionCode; // 功能代码

    private String url; // 功能URL

    private String method; // HTTP方法

    @Column(name = "parent_id")
    private Integer parentId; // 父功能ID

    private Integer sortOrder; // 排序

    private String icon; // 图标

    @Column(name = "created_time")
    private java.time.LocalDateTime createdTime; // 创建时间

    @Column(name = "updated_time")
    private java.time.LocalDateTime updatedTime; // 更新时间

    private String status; // 状态：ACTIVE, INACTIVE

    public Function() {}

    public Function(String functionName, String functionCode, String url) {
        this.functionName = functionName;
        this.functionCode = functionCode;
        this.url = url;
        this.createdTime = java.time.LocalDateTime.now();
        this.updatedTime = java.time.LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Integer getFunctionId() { return functionId; }
    public void setFunctionId(Integer functionId) { this.functionId = functionId; }

    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFunctionCode() { return functionCode; }
    public void setFunctionCode(String functionCode) { this.functionCode = functionCode; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public java.time.LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(java.time.LocalDateTime createdTime) { this.createdTime = createdTime; }

    public java.time.LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(java.time.LocalDateTime updatedTime) { this.updatedTime = updatedTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

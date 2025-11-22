package com.facial.recognition.controller;

import com.facial.recognition.pojo.FunctionEntity;
import com.facial.recognition.service.FunctionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/functions")
@CrossOrigin(origins = "*")
public class FunctionEntityController {

    @Autowired
    private FunctionEntityService functionEntityService;

    // 创建功能
    @PostMapping
    public ResponseEntity<FunctionEntity> createFunction(@RequestBody FunctionEntity functionEntity) {
        try {
            FunctionEntity createdFunction = functionEntityService.createFunction(functionEntity);
            return ResponseEntity.ok(createdFunction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据ID获取功能
    @GetMapping("/{id}")
    public ResponseEntity<FunctionEntity> getFunctionById(@PathVariable Integer id) {
        Optional<FunctionEntity> function = functionEntityService.findById(id);
        return function.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // 根据功能路径获取功能
    @GetMapping("/path/{functionPath}")
    public ResponseEntity<FunctionEntity> getFunctionByPath(@PathVariable String functionPath) {
        Optional<FunctionEntity> function = functionEntityService.findByFunctionPath(functionPath);
        return function.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // 获取所有功能
    @GetMapping
    public ResponseEntity<List<FunctionEntity>> getAllFunctions() {
        List<FunctionEntity> functions = functionEntityService.findAll();
        return ResponseEntity.ok(functions);
    }

    // 根据状态获取功能
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FunctionEntity>> getFunctionsByStatus(@PathVariable String status) {
        List<FunctionEntity> functions = functionEntityService.findByStatus(status);
        return ResponseEntity.ok(functions);
    }

    // 获取活跃状态的功能
    @GetMapping("/active")
    public ResponseEntity<List<FunctionEntity>> getActiveFunctions() {
        List<FunctionEntity> functions = functionEntityService.findActiveFunctions();
        return ResponseEntity.ok(functions);
    }

    // 根据子系统获取功能
    @GetMapping("/subsystem/{subsystem}")
    public ResponseEntity<List<FunctionEntity>> getFunctionsBySubsystem(@PathVariable String subsystem) {
        List<FunctionEntity> functions = functionEntityService.findBySubsystem(subsystem);
        return ResponseEntity.ok(functions);
    }

    // 根据功能名称搜索
    @GetMapping("/search/name")
    public ResponseEntity<List<FunctionEntity>> searchFunctionsByName(@RequestParam String functionName) {
        List<FunctionEntity> functions = functionEntityService.findByFunctionNameContaining(functionName);
        return ResponseEntity.ok(functions);
    }

    // 根据功能路径搜索
    @GetMapping("/search/path")
    public ResponseEntity<List<FunctionEntity>> searchFunctionsByPath(@RequestParam String functionPath) {
        List<FunctionEntity> functions = functionEntityService.findByFunctionPathContaining(functionPath);
        return ResponseEntity.ok(functions);
    }

    // 更新功能信息
    @PutMapping("/{id}")
    public ResponseEntity<FunctionEntity> updateFunction(@PathVariable Integer id, @RequestBody FunctionEntity functionEntity) {
        try {
            FunctionEntity updatedFunction = functionEntityService.updateFunction(id, functionEntity);
            return ResponseEntity.ok(updatedFunction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新功能状态
    @PatchMapping("/{id}/status")
    public ResponseEntity<FunctionEntity> updateFunctionStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            FunctionEntity function = functionEntityService.updateStatus(id, status);
            return ResponseEntity.ok(function);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除功能
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFunction(@PathVariable Integer id) {
        try {
            functionEntityService.deleteFunction(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据用户ID获取用户有权限的功能
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FunctionEntity>> getFunctionsByUserId(@PathVariable Integer userId) {
        List<FunctionEntity> functions = functionEntityService.findFunctionsByUserId(userId);
        return ResponseEntity.ok(functions);
    }

    // 根据角色ID获取角色有权限的功能
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<FunctionEntity>> getFunctionsByRoleId(@PathVariable Integer roleId) {
        List<FunctionEntity> functions = functionEntityService.findFunctionsByRoleId(roleId);
        return ResponseEntity.ok(functions);
    }

    // 统计活跃功能数量
    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveFunctions() {
        Long count = functionEntityService.countActiveFunctions();
        return ResponseEntity.ok(count);
    }

    // 根据子系统统计功能数量
    @GetMapping("/count/subsystem/{subsystem}")
    public ResponseEntity<Long> countBySubsystem(@PathVariable String subsystem) {
        Long count = functionEntityService.countBySubsystem(subsystem);
        return ResponseEntity.ok(count);
    }

    // 检查功能路径是否存在
    @GetMapping("/exists/{functionPath}")
    public ResponseEntity<Boolean> checkFunctionPathExists(@PathVariable String functionPath) {
        boolean exists = functionEntityService.existsByFunctionPath(functionPath);
        return ResponseEntity.ok(exists);
    }
}


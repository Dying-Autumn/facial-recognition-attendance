package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.FunctionEntity;
import com.facial.recognition.repository.FunctionEntityRepository;
import com.facial.recognition.service.FunctionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FunctionEntityServiceImpl implements FunctionEntityService {

    @Autowired
    private FunctionEntityRepository functionEntityRepository;

    @Override
    public FunctionEntity createFunction(FunctionEntity functionEntity) {
        functionEntity.setCreatedTime(LocalDateTime.now());
        functionEntity.setUpdatedTime(LocalDateTime.now());
        if (functionEntity.getStatus() == null) {
            functionEntity.setStatus("ACTIVE");
        }
        return functionEntityRepository.save(functionEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FunctionEntity> findById(Integer functionId) {
        return functionEntityRepository.findById(functionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FunctionEntity> findByFunctionPath(String functionPath) {
        return functionEntityRepository.findByFunctionPath(functionPath);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findAll() {
        return functionEntityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findByStatus(String status) {
        return functionEntityRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findActiveFunctions() {
        return functionEntityRepository.findByStatus("ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findBySubsystem(String subsystem) {
        return functionEntityRepository.findBySubsystem(subsystem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findByFunctionNameContaining(String functionName) {
        return functionEntityRepository.findByFunctionNameContaining(functionName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findByFunctionPathContaining(String functionPath) {
        return functionEntityRepository.findByFunctionPathContaining(functionPath);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findBySubsystemAndStatus(String subsystem, String status) {
        return functionEntityRepository.findBySubsystemAndStatus(subsystem, status);
    }

    @Override
    public FunctionEntity updateFunction(Integer functionId, FunctionEntity functionEntity) {
        return functionEntityRepository.findById(functionId)
                .map(existing -> {
                    existing.setFunctionName(functionEntity.getFunctionName());
                    existing.setFunctionPath(functionEntity.getFunctionPath());
                    existing.setSubsystem(functionEntity.getSubsystem());
                    existing.setDescription(functionEntity.getDescription());
                    existing.setStatus(functionEntity.getStatus());
                    existing.setUpdatedTime(LocalDateTime.now());
                    return functionEntityRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("功能不存在: " + functionId));
    }

    @Override
    public FunctionEntity updateStatus(Integer functionId, String status) {
        return functionEntityRepository.findById(functionId)
                .map(existing -> {
                    existing.setStatus(status);
                    existing.setUpdatedTime(LocalDateTime.now());
                    return functionEntityRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("功能不存在: " + functionId));
    }

    @Override
    public void deleteFunction(Integer functionId) {
        functionEntityRepository.deleteById(functionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findFunctionsByUserId(Integer userId) {
        return functionEntityRepository.findFunctionsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FunctionEntity> findFunctionsByRoleId(Integer roleId) {
        return functionEntityRepository.findFunctionsByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveFunctions() {
        return functionEntityRepository.countByStatus("ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public Long countBySubsystem(String subsystem) {
        return functionEntityRepository.countBySubsystem(subsystem);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByFunctionPath(String functionPath) {
        return functionEntityRepository.findByFunctionPath(functionPath).isPresent();
    }
}


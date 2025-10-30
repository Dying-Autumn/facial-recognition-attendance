package com.xitong.springboot_xitong.service.impl;

import com.xitong.springboot_xitong.pojo.Teacher;
import com.xitong.springboot_xitong.repository.TeacherRepository;
import com.xitong.springboot_xitong.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) { this.teacherRepository = teacherRepository; }

    @Override
    @Transactional
    public Teacher createTeacher(Teacher t) { return teacherRepository.save(t); }

    @Override
    public Optional<Teacher> findByUserId(String userId) { return teacherRepository.findByUserId(userId); }

    @Override
    @Transactional
    public Teacher updateTeacher(String userId, Teacher update) {
        Teacher t = teacherRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("teacher not found"));
        t.setDepartment(update.getDepartment()); t.setTitle(update.getTitle());
        return teacherRepository.save(t);
    }

    @Override
    @Transactional
    public void deleteByUserId(String userId) { teacherRepository.findByUserId(userId).ifPresent(t -> teacherRepository.delete(t)); }

    @Override
    public List<Teacher> listAll() { return teacherRepository.findAll(); }
}

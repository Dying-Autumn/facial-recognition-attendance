package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.Teacher;
import com.facial.recognition.repository.TeacherRepository;
import com.facial.recognition.service.TeacherService;
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
    public Optional<Teacher> findByUserId(Integer userId) { return teacherRepository.findByUserId(userId); }

    @Override
    @Transactional
    public Teacher updateTeacher(Integer userId, Teacher update) {
        Teacher t = teacherRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("teacher not found"));
        t.setDepartment(update.getDepartment()); t.setJobTitle(update.getJobTitle());
        return teacherRepository.save(t);
    }

    @Override
    @Transactional
    public void deleteByUserId(Integer userId) { teacherRepository.findByUserId(userId).ifPresent(t -> teacherRepository.delete(t)); }

    @Override
    public List<Teacher> listAll() { return teacherRepository.findAll(); }
}

package com.facial.recognition.service;

import com.facial.recognition.pojo.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    Teacher createTeacher(Teacher t);
    Optional<Teacher> findByUserId(String userId);
    Teacher updateTeacher(String userId, Teacher update);
    void deleteByUserId(String userId);
    List<Teacher> listAll();
}

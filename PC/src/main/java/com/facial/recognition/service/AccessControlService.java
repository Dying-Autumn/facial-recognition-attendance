package com.facial.recognition.service;

/**
 * 统一的访问控制校验服务
 */
public interface AccessControlService {

    /**
     * 学生访问与自己相关的课程/课表时的权限校验
     */
    void assertCanAccessStudentCourse(Integer requesterUserId, Long targetStudentId);

    /**
     * 教师访问自己授课班级或课程时的权限校验
     */
    void assertCanAccessTeacherCourse(Integer requesterUserId, Long targetTeacherId);

    /**
     * 教师/管理员访问指定班级（用于课程、学生名单、考勤等）
     */
    void assertCanAccessCourseClass(Integer requesterUserId, Long courseClassId);
}


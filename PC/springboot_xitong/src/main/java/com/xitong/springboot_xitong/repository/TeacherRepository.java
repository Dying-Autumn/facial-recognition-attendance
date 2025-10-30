
package com.xitong.springboot_xitong.repository;
import com.xitong.springboot_xitong.pojo.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(String userId);
}

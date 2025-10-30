
package com.xitong.springboot_xitong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.xitong.springboot_xitong.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
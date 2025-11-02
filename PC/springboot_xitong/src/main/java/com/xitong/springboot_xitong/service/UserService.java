package com.xitong.springboot_xitong.service;

import com.xitong.springboot_xitong.pojo.User;
import com.xitong.springboot_xitong.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service           //spring的bean
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User add(User user){
        User userPojo=new User();
        BeanUtils.copyProperties(user,userPojo);
        return  userRepository.save(userPojo);    //调用数据访问类
    }

    }



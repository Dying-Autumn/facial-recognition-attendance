package com.xitong.springboot_xitong.controller;

import com.xitong.springboot_xitong.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController    //接口方法返回对象，转换成json文本
@RequestMapping("/user")    //localhost:8088,通过user访问
public class UserController {
@Autowired
    IUserService userService;
    //REST

}

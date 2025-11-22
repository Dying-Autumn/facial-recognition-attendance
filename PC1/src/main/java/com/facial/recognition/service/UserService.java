package com.facial.recognition.service;

import com.facial.recognition.pojo.User;
import com.facial.recognition.pojo.Student;
import com.facial.recognition.repository.UserRepository;
import com.facial.recognition.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public User add(User user) {
        User userPojo = new User();
        BeanUtils.copyProperties(user, userPojo);
        return userRepository.save(userPojo);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            return user;
        }
        // 尝试通过学号查找
        return studentRepository.findByStudentNumber(username)
                .map(Student::getUserId)
                .flatMap(userRepository::findById);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}



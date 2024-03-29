package com.example.userdao2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private UserDao userDao;

    @GetMapping("/user/{id}")
    public User get(@PathVariable Long id){
        return userDao.findById(id).get();
    }
}

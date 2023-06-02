package com.example.potalend;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;

    @GetMapping("/{id}")
    public User get(@PathVariable Long id){
        return userDao.findById(id).get();
    }

    /*@PostMapping("/upload")
    public String upload(@RequestParam)*/
}

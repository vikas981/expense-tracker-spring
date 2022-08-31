package com.vikash.expense.controller;

import com.vikash.expense.entity.User;
import com.vikash.expense.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<User> getUserByEmail(@RequestParam String name){
        return new ResponseEntity<>(userService.getUserByName(name),HttpStatus.OK);
    }
}

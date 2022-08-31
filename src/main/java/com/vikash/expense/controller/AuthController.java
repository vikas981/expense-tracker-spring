package com.vikash.expense.controller;

import com.vikash.expense.entity.TokenResponse;
import com.vikash.expense.entity.User;
import com.vikash.expense.entity.UserRequest;
import com.vikash.expense.service.JwtTokenUtil;
import com.vikash.expense.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserRequest userRequest){
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(),userRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken(authentication);

        return new ResponseEntity<>(TokenResponse.builder().token(token).build(),HttpStatus.CREATED);

    }

    @PostMapping(value = "/auth/register",consumes = "application/json")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        System.out.println(user);
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }
}

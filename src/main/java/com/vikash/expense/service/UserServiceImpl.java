package com.vikash.expense.service;

import com.vikash.expense.entity.Role;
import com.vikash.expense.entity.User;
import com.vikash.expense.exception.EmailAlreadyRegisteredException;
import com.vikash.expense.exception.ExpenseNotFoundException;
import com.vikash.expense.repository.RoleRepository;
import com.vikash.expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
           throw new EmailAlreadyRegisteredException(user.getEmail() +" is already registered");
        }

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setAge(user.getAge());
        newUser.setRoles(user.getRoles());
        return userRepository.save(newUser);
    }

    @Override
    public void assignUserRole(Long usedId, Long roleId) {

    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(userRepository.findByEmail(email));
        return  userRepository.findByEmail(email).orElseThrow(() -> new ExpenseNotFoundException("user not logged in"));
    }

    @Override
    public User getUserByName(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ExpenseNotFoundException("user not exists with email: "+email));
    }
}

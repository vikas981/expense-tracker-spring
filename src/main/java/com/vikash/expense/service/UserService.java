package com.vikash.expense.service;

import com.vikash.expense.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {

    User createUser(User user);
    void assignUserRole(Long usedId,Long roleId);

    User getLoggedInUser();


    User getUserByName(String name);
}

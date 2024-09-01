package com.sb.user.service.services;

import java.util.List;

import com.sb.user.service.entities.User;

public interface UserService {

	User saveUser(User user);

	List<User> getAllUsers();

	User getUser(String userId);
}

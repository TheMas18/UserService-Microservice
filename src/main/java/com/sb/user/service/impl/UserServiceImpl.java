package com.sb.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sb.user.service.entities.Rating;
import com.sb.user.service.entities.User;
import com.sb.user.service.exceptions.ResourceNotFoundException;
import com.sb.user.service.repositories.UserRepository;
import com.sb.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User saveUser(User user) {
		//It will create unique user id which will be in string format
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return this.userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return this.userRepository.findAll();
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		User user= this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Is Not Found on the server: " + userId));
	//get rating
		ArrayList<Rating> forObject = restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserId(), ArrayList.class);
		logger.info("[]",forObject);
		return user;
	}

}

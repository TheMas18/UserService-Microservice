package com.sb.user.service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.user.service.entities.User;
import com.sb.user.service.impl.UserServiceImpl;
import com.sb.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User user2 = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user2);
	}

	@GetMapping("/{userId}")
	@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
		User user = userService.getUser(userId);
		return ResponseEntity.ok(user);
	}

	// creating rating fall back method for circuit breaker
	public ResponseEntity<User> ratingFallback(String userId, Exception ex) {
		logger.info("Fallback is executed because service is down", ex.getMessage());
		User user = User.builder().email("workwithmas18@gmail.com").name("dummy")
				.about("User created dummy because service is down").userId("1423").build();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUser() {
		List<User> allUsers = userService.getAllUsers();
		return ResponseEntity.ok(allUsers);
	}
}

package com.sb.user.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sb.user.service.entities.Hotel;
import com.sb.user.service.entities.Rating;
import com.sb.user.service.entities.User;
import com.sb.user.service.exceptions.ResourceNotFoundException;
import com.sb.user.service.external.services.HotelService;
import com.sb.user.service.repositories.UserRepository;
import com.sb.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;
	
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
		Rating []ratingsUser = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
		logger.info("[]",ratingsUser);
		List<Rating> ratings = Arrays.stream(ratingsUser).toList();
		logger.info("[]",ratings);
		List<Rating> ratingList = ratings.stream().map(rating ->{
			//api call to hotel service to get the hotel
			// set  the hotel to  rating
			//return the rating
			//http://localhost:8082/hotels/dab8957d-6737-4ed6-b9d4-efc23528b343
			
			//this is without feign client 
			//ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);
			//Hotel hotel = forEntity.getBody();
			
			//with feign client we can get like this 
			Hotel hotel=hotelService.getHotel(rating.getHotelId());
			//logger.info("Response Status Code : ",forEntity.getStatusCode());
			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());
		user.setRatings(ratingList);
		return user;
	}

}

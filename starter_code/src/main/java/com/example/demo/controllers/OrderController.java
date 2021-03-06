package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger logger = LogManager.getLogger(OrderController.class);
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		ApplicationUser user = applicationUserRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		ApplicationUser user = applicationUserRepository.findByUsername(username);
		if(user == null) {
			logger.info("Order get request failed.");
			return ResponseEntity.notFound().build();
		}
		logger.info("Order get request succeed.");
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}

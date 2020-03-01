package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.*;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import org.junit.Before;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
public class OrderControllerTest {

    private OrderController orderController;
    private ApplicationUserRepository applicationUserRepository = mock(ApplicationUserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    List<UserOrder> list;
    @Before
    public void setup(){
        list = new ArrayList<>();
        orderController = new OrderController();
        list.add(getOrder());
        TestUtils.injectObject(orderController,"applicationUserRepository",applicationUserRepository);
        TestUtils.injectObject(orderController,"orderRepository",orderRepository);
        given(applicationUserRepository.findByUsername(any())).willReturn(getUser());
        given(orderRepository.save(any())).willReturn(getOrder());
        given(orderRepository.findByUser(getUser())).willReturn(list);
    }

    @Test
    public void submit_test(){
        assertEquals(200,orderController.submit(getUser().getUsername()).getStatusCodeValue());
        verify(applicationUserRepository,times(1)).findByUsername(getUser().getUsername());
    }
    @Test
    public void get_orders_for_users(){
        assertNotNull(orderController.getOrdersForUser("test"));
        assertEquals(200,orderController.getOrdersForUser(getUser().getUsername()).getStatusCodeValue());
    }
    private ApplicationUser getUser(){
        ApplicationUser user = new ApplicationUser();
        List<Item> list = new ArrayList<>();
        list.add(getItem());
        Cart cart = new Cart();
        cart.setItems(list);
        cart.setTotal(new BigDecimal(300));
        user.setCart(cart);
        user.setUsername("test");
        user.setPassword("testPassword");
        cart.setUser(user);
        return user;
    }
    private Item getItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("desk");
        item.setDescription("used by chidren");
        item.setPrice(new BigDecimal(300.00));
        return item;
    }
    private UserOrder getOrder(){
        return UserOrder.createFromCart(getUser().getCart());
    }
}

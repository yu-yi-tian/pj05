package com.example.demo.controllers;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserControllerTest {

    private UserController userController;
    private ApplicationUserRepository userRepository = mock(ApplicationUserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController,"applicationUserRepository",userRepository);
        TestUtils.injectObject(userController,"cartRepository",cartRepository);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder",encoder);

        ApplicationUser user = getUser();
        given(userRepository.findByUsername(any())).willReturn(user);
    }
    @Test
    public void create_user_test() throws Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<ApplicationUser> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        ApplicationUser user = response.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test",user.getUsername());
        assertEquals("thisIsHashed",user.getPassword());
    }

    @Test
    public void find_by_user_name_test(){
        ApplicationUser user = getUser();
        final ResponseEntity<ApplicationUser> responseEntity = userController.findByUserName(user.getUsername());
        assertEquals(responseEntity.getBody().getUsername(),user.getUsername());
        assertEquals(responseEntity.getBody().getPassword(),user.getPassword());
        assertEquals(200,responseEntity.getStatusCodeValue());
        verify(userRepository,times(1)).findByUsername(user.getUsername());
    }


    private ApplicationUser getUser(){
        ApplicationUser user = new ApplicationUser();
        user.setUsername("test");
        user.setPassword("testPassword");
        return user;
    }
}

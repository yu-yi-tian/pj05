package com.example.demo.repository;

import com.example.demo.model.persistence.ApplicationUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApplicationUserRepositoryTest {

    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void find_by_user_name_test(){
        //given
        ApplicationUser user = new ApplicationUser();
        user.setUsername("test");
        user.setPassword("1234567");
        testEntityManager.persist(user);
        testEntityManager.flush();

        //when
        assertEquals(applicationUserRepository.findByUsername(user.getUsername()).getPassword(),user.getPassword());

    }

}

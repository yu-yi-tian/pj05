package com.example.demo.controllers;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController,"itemRepository",itemRepository);

        Item item = getItem();
        List<Item> items = new ArrayList<>();
        items.add(item);
        given(itemRepository.findByName(any())).willReturn(items);
        given(itemRepository.findAll()).willReturn(items);
    }
    @Test
    public void get_items_by_name_test(){
        Item item = getItem();
        final ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(item.getName());
        assertEquals(responseEntity.getBody().get(0).getId(),item.getId());
        assertEquals(responseEntity.getBody().get(0).getName(),item.getName());
        verify(itemRepository,times(1)).findByName(item.getName());
    }
    @Test
    public void get_items_test(){
        Item item = getItem();
        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertEquals(responseEntity.getBody().get(0).getId(),item.getId());
        assertEquals(responseEntity.getBody().get(0).getName(),item.getName());
        verify(itemRepository,times(1)).findAll();

    }

    private Item getItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("desk");
        item.setDescription("used by chidren");
        return item;
    }
}

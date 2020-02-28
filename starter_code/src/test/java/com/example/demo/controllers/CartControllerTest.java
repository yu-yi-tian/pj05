package com.example.demo.controllers;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.ApplicationUser;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ApplicationUserRepository;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.*;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ApplicationUserRepository applicationUserRepository = mock(ApplicationUserRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObject(cartController,"applicationUserRepository",applicationUserRepository);
        TestUtils.injectObject(cartController,"cartRepository",cartRepository);
        TestUtils.injectObject(cartController,"itemRepository",itemRepository);

        given(applicationUserRepository.findByUsername(any())).willReturn(getUser());
        Optional<Item> item = Optional.of(getItem());
        given(itemRepository.findById(any())).willReturn(item);

    }
    @Test
    public void add_to_cart_test(){
        final ResponseEntity<Cart> responseEntity = cartController.addTocart(getRequest());
        assertEquals(responseEntity.getBody().getItems().get(0),getItem());
    }

    private ApplicationUser getUser(){
        ApplicationUser user = new ApplicationUser();
        Cart cart = new Cart();
        cart.setTotal(new BigDecimal(0));
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        return user;
    }

    private Item getItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("desk");
        item.setDescription("used by chidren");
        item.setPrice(new BigDecimal(200.00));
        return item;
    }

    private ModifyCartRequest getRequest(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1L);
        request.setUsername("test");
        return request;
    }

}

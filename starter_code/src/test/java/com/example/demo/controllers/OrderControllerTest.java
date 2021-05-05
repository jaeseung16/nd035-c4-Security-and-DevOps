package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }


    @Test
    public void testSubmit() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10.0));

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Cart cart = new Cart();
        cart.setItems(itemList);

        User user = new User();
        user.setUsername("test");
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(itemList.size(), order.getItems().size());
    }

    @Test
    public void testGetOrderForUser() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10.0));

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        User user = new User();
        user.setUsername("test");

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(itemList);

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);

        List<UserOrder> actualOrderList = response.getBody();
        assertNotNull(actualOrderList);
        assertEquals(1, actualOrderList.size());
    }
    
}



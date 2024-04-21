package com.portoflio.api.services;

import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.models.Order;

import java.util.List;

public interface OrderService {
    OrderDTO postOrder(String email, OrderCreateDTO postOrderRequest);
    OrderDTO postOrderGuest( OrderCreateDTO postOrderRequest);
    List<OrderDTO> findAll();
    //OrderDTO findById(Long id);
    Order findById(Long id);

}

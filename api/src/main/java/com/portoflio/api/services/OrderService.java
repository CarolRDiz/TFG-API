package com.portoflio.api.services;

import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.models.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDTO postOrder(String email, OrderCreateDTO postOrderRequest);
    OrderDTO postOrderGuest( OrderCreateDTO postOrderRequest);
    void update(Long id, Map<String, Object> fields);
    void updateOrdersStatus(List<Long> ids, Map<String, Object> fields);
    List<OrderDTO> findAll();
    //OrderDTO findById(Long id);
    Order findById(Long id);

}

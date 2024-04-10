package com.portoflio.api.services;

import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;

public interface OrderService {
    OrderDTO postOrder(OrderCreateDTO postOrderRequest);
}

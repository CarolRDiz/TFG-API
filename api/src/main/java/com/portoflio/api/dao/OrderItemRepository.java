package com.portoflio.api.dao;

import com.portoflio.api.models.Order;
import com.portoflio.api.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

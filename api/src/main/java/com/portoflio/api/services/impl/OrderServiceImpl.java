package com.portoflio.api.services.impl;

import com.portoflio.api.dao.OrderRepository;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Order;
import com.portoflio.api.models.OrderItem;
import com.portoflio.api.models.Product;
import com.portoflio.api.services.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    private ModelMapper mapper = new ModelMapper();
    TypeMap<Order, OrderDTO> propertyMapper = this.mapper.createTypeMap(Order.class, OrderDTO.class);

    @Override
    public OrderDTO postOrder(OrderCreateDTO postOrderRequest) {

        Order saveOrder = new Order();
        //saveOrder.setUser(user);
        saveOrder.setFirstName(postOrderRequest.getFirstName());
        saveOrder.setMobilePhone(postOrderRequest.getMobilePhone());
        saveOrder.setAddress(postOrderRequest.getAddress());
        saveOrder.setSecondAddress(postOrderRequest.getSecondAddress());
        saveOrder.setCity(postOrderRequest.getCity());
        saveOrder.setPostalCode(postOrderRequest.getPostalCode());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        saveOrder.setDate(date);
        saveOrder.setItems(new ArrayList<>());

        // POR CADA PRODUCTO SE CREA UN ORDERITEM
        postOrderRequest.getCartItems().forEach(cartItem -> {
            Optional<Product> oProduct = productRepository.findById(cartItem.getProduct_id());
            if (oProduct.isPresent()) {
                Product product = oProduct.get();
                product.setSellCount(product.getSellCount() + cartItem.getAmount());
                OrderItem orderItem = new OrderItem();
                orderItem.setAmount(cartItem.getAmount());
                //orderItem.setOrder(saveOrder);
                orderItem.setProduct(product);
                saveOrder.getItems().add(orderItem);
            } else {
                throw new NotFoundException("Product not found");
            }
        });
        saveOrder.setTotalPrice(postOrderRequest.getTotalPrice());
        //saveOrder.setTotalCargoPrice(cart.getTotalCargoPrice());
        //saveOrder.setDiscount(cart.getDiscount());
        saveOrder.setShipped(0);

        Order order = orderRepository.save(saveOrder);
        //cartService.emptyCart();
        OrderDTO dto = this.mapper.map(order, OrderDTO.class);
        return dto;
    }

}

package com.portoflio.api.services.impl;

import com.portoflio.api.dao.OrderItemRepository;
import com.portoflio.api.dao.OrderRepository;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.dto.OrderCreateDTO;
import com.portoflio.api.dto.OrderDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Order;
import com.portoflio.api.models.OrderItem;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.Users;
import com.portoflio.api.services.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    private ModelMapper mapper = new ModelMapper();
    TypeMap<Order, OrderDTO> propertyMapper = this.mapper.createTypeMap(Order.class, OrderDTO.class);

    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> dtos = orders
                .stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }
    /*
    @Override
    public OrderDTO findById(Long id) {
        Optional<Order> oOrder = orderRepository.findById(id);
        if(oOrder.isPresent()){
            OrderDTO dto = this.mapper.map(oOrder.get(), OrderDTO.class);
            return dto;
        }
        else {
            throw new NotFoundException("Order not found");
        }
    }
     */
    @Override
    public Order findById(Long id) {
        Optional<Order> oOrder = orderRepository.findById(id);
        if(oOrder.isPresent()){

            return oOrder.get();
        }
        else {
            throw new NotFoundException("Order not found");
        }
    }

    @Override
    public OrderDTO postOrder(String email, OrderCreateDTO postOrderRequest) {
        System.out.println("POST ORDER METHOD");
        System.out.println("new Order()");
        /*
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
        */



        Order saveOrder = this.mapper.map(postOrderRequest, Order.class);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        saveOrder.setDate(date);
        saveOrder.setItems(new ArrayList<>());
        saveOrder.setShipped(0);

        Optional<Users> oUser = usersRepository.findByEmail(email);
        if(oUser.isPresent()){
            saveOrder.setUser(oUser.get());
        }

        Order order = orderRepository.save(saveOrder);

        // POR CADA PRODUCTO SE CREA UN ORDERITEM
        postOrderRequest.getCartItems().forEach(cartItem -> {
            System.out.println("findById product");
            Optional<Product> oProduct = productRepository.findById(cartItem.getProduct_id());
            if (oProduct.isPresent()) {
                Product product = oProduct.get();
                product.setSellCount(product.getSellCount() + cartItem.getAmount());
                OrderItem orderItem = new OrderItem();
                orderItem.setAmount(cartItem.getAmount());
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem = orderItemRepository.save(orderItem);
                //order.getItems().add(orderItem);

            } else {
                throw new NotFoundException("Product not found");
            }
        });
        //cartService.emptyCart();
        OrderDTO dto = this.mapper.map(order, OrderDTO.class);
        return dto;
    }
    @Override
    public OrderDTO postOrderGuest( OrderCreateDTO postOrderRequest) {
        System.out.println("POST ORDER METHOD");
        System.out.println("new Order()");

        Order saveOrder = this.mapper.map(postOrderRequest, Order.class);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        saveOrder.setDate(date);
        saveOrder.setItems(new ArrayList<>());

        System.out.println("forEach");


        saveOrder.setShipped(0);

        Order order = orderRepository.save(saveOrder);

        // POR CADA PRODUCTO SE CREA UN ORDERITEM
        postOrderRequest.getCartItems().forEach(cartItem -> {
            System.out.println("findById product");
            Optional<Product> oProduct = productRepository.findById(cartItem.getProduct_id());
            if (oProduct.isPresent()) {
                Product product = oProduct.get();
                product.setSellCount(product.getSellCount() + cartItem.getAmount());
                OrderItem orderItem = new OrderItem();
                orderItem.setAmount(cartItem.getAmount());
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem = orderItemRepository.save(orderItem);
                //order.getItems().add(orderItem);

            } else {
                throw new NotFoundException("Product not found");
            }
        });
        //order = orderRepository.save(order);


        //cartService.emptyCart();
        OrderDTO dto = this.mapper.map(order, OrderDTO.class);
        return dto;
    }
}

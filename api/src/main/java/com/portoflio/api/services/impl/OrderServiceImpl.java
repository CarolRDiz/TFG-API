package com.portoflio.api.services.impl;

import com.portoflio.api.dao.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl {
    @Autowired
    OrderRepository orderRepository;

    @Override
    public OrderResponse postOrder(PostOrderRequest postOrderRequest) {
        // USUARIO
        User user = userService.getUser();
        // CARRITO DEL USUARIO
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList())) {
            throw new InvalidArgumentException("Cart is not valid");
        }

        if (cart.getCartItemList().stream().anyMatch(cartItem -> cartItem.getProductVariant().getStock() < cartItem.getAmount())) {
            throw new InvalidArgumentException("A product in your cart is out of stock.");
        }

        Order saveOrder = new Order();
        saveOrder.setUser(user);
        saveOrder.setShipName(postOrderRequest.getShipName());
        saveOrder.setPhone(postOrderRequest.getPhone());
        saveOrder.setShipAddress(postOrderRequest.getShipAddress());
        saveOrder.setBillingAddress(postOrderRequest.getBillingAddress());
        saveOrder.setCity(postOrderRequest.getCity());
        saveOrder.setCountry(postOrderRequest.getCountry());
        saveOrder.setState(postOrderRequest.getState());
        saveOrder.setZip(postOrderRequest.getZip());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        saveOrder.setDate(date);

        saveOrder.setOrderDetailList(new ArrayList<>());
        // POR CADA PRODUCTO SE CREA UN ORDERITEM
        cart.getCartItemList().forEach(cartItem -> {
            cartItem.getProductVariant().setSellCount(cartItem.getProductVariant().getSellCount() + cartItem.getAmount());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmount(cartItem.getAmount());
            orderDetail.setOrder(saveOrder);
            orderDetail.setProductVariant(cartItem.getProductVariant());
            saveOrder.getOrderDetailList().add(orderDetail);
        });

        saveOrder.setTotalPrice(cart.getTotalPrice());
        saveOrder.setTotalCargoPrice(cart.getTotalCargoPrice());
        saveOrder.setDiscount(cart.getDiscount());
        saveOrder.setShipped(0);


        Order order = orderRepository.save(saveOrder);
        cartService.emptyCart();
        return orderResponseConverter.apply(order);
    }
}

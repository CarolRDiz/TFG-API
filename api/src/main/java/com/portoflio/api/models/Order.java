package com.portoflio.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<OrderItem>();

    /*
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

     */

    // CUSTOMER DETAILS

    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String secondAddress;
    private String city;
    private String postalCode;
    private String mobilePhone;
    private Integer shipped = 0;
    private String totalPrice;

    private Date date;

    //    @Temporal(TemporalType.TIMESTAMP) - causes bug when saving...
    //private String created;




    /*
    //private String comment;

    @Column(name = "ship_name")
    private String shipName;

    @Column(name = "ship_address")
    private String shipAddress;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "zip")
    private String zip;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "total_cargo_price")
    private Float totalCargoPrice;

    @Column(name = "date")
    @Type(type = "timestamp")
    private Date date;

    @Column(name = "shipped")
    private Integer shipped;

    @Column(name = "cargo_firm")
    private String cargoFirm;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    private String type;

     @Column(name = "state")
    private String state;


     */


}

package com.ecommerce.user.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "address")
public class Address {
    @Id
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;


//    @OneToOne (mappedBy = "address")
//    private User user;
}

package com.presnakov.hotelBooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "room", schema = "public")
public class Room {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer occupancy;
    @Column(name = "class")
    private RoomClass roomClass;
    private String photo;
    @Column(name = "price_per_day")
    private Integer pricePerDay;
    private Hotel hotel;
}

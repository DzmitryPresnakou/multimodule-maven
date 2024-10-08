package com.presnakov.hotelbooking.util;

import com.presnakov.hotelbooking.entity.Hotel;
import com.presnakov.hotelbooking.entity.Order;
import com.presnakov.hotelbooking.entity.OrderStatusEnum;
import com.presnakov.hotelbooking.entity.PaymentStatusEnum;
import com.presnakov.hotelbooking.entity.RoleEnum;
import com.presnakov.hotelbooking.entity.Room;
import com.presnakov.hotelbooking.entity.RoomClassEnum;
import com.presnakov.hotelbooking.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

import java.time.LocalDate;

@UtilityClass
public class TestDataImporter {

    public static void importData(Session session) {

        Hotel hotel1 = saveHotel(session, "Plaza", "hotelphoto001.jpg");
        Hotel hotel2 = saveHotel(session, "Minsk", "hotelphoto002.jpg");

        Room room1 = saveRoom(session, RoomClassEnum.ECONOMY, 29, "roomphoto001.jpg", 2, hotel1);
        Room room2 = saveRoom(session, RoomClassEnum.COMFORT, 49, "roomphoto002.jpg", 3, hotel1);
        Room room3 = saveRoom(session, RoomClassEnum.BUSINESS, 69, "roomphoto003.jpg", 4, hotel1);
        Room room4 = saveRoom(session, RoomClassEnum.ECONOMY, 39, "roomphoto004.jpg", 2, hotel2);
        Room room5 = saveRoom(session, RoomClassEnum.COMFORT, 59, "roomphoto005.jpg", 3, hotel2);
        Room room6 = saveRoom(session, RoomClassEnum.BUSINESS, 79, "roomphoto006.jpg", 4, hotel2);

        User user1 = saveUser(session, "Vasya", "Vasilyev", "vasya@gmai.com",
                "+375291478523", "userphoto001.jpg", LocalDate.of(1995, 2, 5),
                2500, "12345", RoleEnum.USER);
        User user2 = saveUser(session, "Vanya", "Ivanov", "vanya@gmai.com",
                "+375291478523", "userphoto001.jpg", LocalDate.of(1997, 6, 11),
                3000, "56987", RoleEnum.USER);
        User user3 = saveUser(session, "Petya", "Petrov", "petya@gmai.com",
                "+375291478523", "userphoto001.jpg", LocalDate.of(2000, 11, 9),
                5000, "4563258", RoleEnum.USER);

        Order order1 = saveOrder(session, user1, room1, OrderStatusEnum.OPEN, PaymentStatusEnum.APPROVED,
                LocalDate.of(2024, 10, 15), LocalDate.of(2024, 10, 25));
        Order order2 = saveOrder(session, user2, room5, OrderStatusEnum.APPROVED, PaymentStatusEnum.APPROVED,
                LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 17));
        Order order3 = saveOrder(session, user3, room6, OrderStatusEnum.APPROVED, PaymentStatusEnum.APPROVED,
                LocalDate.of(2024, 10, 20), LocalDate.of(2024, 10, 30));
    }

    private static Hotel saveHotel(Session session, String name, String photo) {
        Hotel hotel = Hotel.builder()
                .photo(photo)
                .name(name)
                .build();
        session.persist(hotel);
        return hotel;
    }

    private static Room saveRoom(Session session,
                                 RoomClassEnum roomClass,
                                 Integer pricePerDay,
                                 String photo,
                                 Integer occupancy,
                                 Hotel hotel) {
        Room room = Room.builder()
                .roomClass(roomClass)
                .pricePerDay(pricePerDay)
                .photo(photo)
                .occupancy(occupancy)
                .hotel(hotel)
                .build();
        session.persist(room);
        return room;
    }

    private static User saveUser(Session session,
                                 String firstName,
                                 String lastName,
                                 String email,
                                 String phone,
                                 String photo,
                                 LocalDate birthDate,
                                 Integer money,
                                 String password,
                                 RoleEnum roleEnum) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .photo(photo)
                .birthDate(birthDate)
                .money(money)
                .password(password)
                .role(roleEnum)
                .build();
        session.persist(user);
        return user;
    }

    private static Order saveOrder(Session session,
                                   User user,
                                   Room room,
                                   OrderStatusEnum orderStatus,
                                   PaymentStatusEnum paymentStatus,
                                   LocalDate checkInDate,
                                   LocalDate checkOutDate) {
        Order order = Order.builder()
                .user(user)
                .room(room)
                .status(orderStatus)
                .paymentStatus(paymentStatus)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
        session.persist(order);
        return order;
    }
}

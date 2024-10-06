package com.presnakov.hotelbooking.entity;

import com.presnakov.hotelbooking.integration.EntityTestBase;
import com.presnakov.hotelbooking.util.TestDataImporter;
import lombok.Cleanup;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTestIT extends EntityTestBase {

    @Test
    void findOrderByUserEmail() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataImporter.importData(session);
        String userEmail = "vasya@gmai.com";

        List<Order> results = session.createQuery("select o from Order o " +
                                                  "join o.user u " +
                                                  "where u.email = :email", Order.class)
                .setParameter("email", userEmail)
                .list();

        assertThat(results).hasSize(1);
        session.getTransaction().commit();
    }

    @Test
    void findAllOrdersByHotelName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataImporter.importData(session);
        String hotelName = "Minsk";

        List<Order> results = session.createQuery("select o from Order o " +
                                                  "join o.room r " +
                                                  "join r.hotel h " +
                                                  "where h.name = :hotelName", Order.class)
                .setParameter("hotelName", hotelName)
                .list();

        assertThat(results).hasSize(2);
        session.getTransaction().commit();
    }

    @Test
    void findOrdersByCheckInDate() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataImporter.importData(session);
        LocalDate checkInDate = LocalDate.of(2024, 10, 20);

        List<Order> results = session.createQuery("select o from Order o " +
                                                  "where o.checkInDate = :checkInDate", Order.class)
                .setParameter("checkInDate", checkInDate)
                .list();

        assertThat(results).hasSize(1);
        session.getTransaction().commit();
    }

    @Test
    void createOrder() {
        Order order = addOrderToSession();
        session.flush();
        session.clear();

        Order actualResult = session.getReference(Order.class, order.getId());

        assertThat(actualResult.getId()).isEqualTo(order.getId());
    }

    @Test
    void updateOrder() {
        Order order = addOrderToSession();
        order.setStatus(OrderStatusEnum.REJECTED);
        order.setPaymentStatus(PaymentStatusEnum.DECLINED);
        order.setCheckInDate(LocalDate.of(2025, 11, 10));
        order.setCheckOutDate(LocalDate.of(2025, 11, 15));
        session.merge(order);
        session.flush();
        session.clear();

        Order actualResult = session.getReference(Order.class, order.getId());

        assertAll(
                () -> assertThat(actualResult.getStatus().equals(OrderStatusEnum.REJECTED)),
                () -> assertThat(actualResult.getPaymentStatus().equals(PaymentStatusEnum.DECLINED)),
                () -> assertThat(actualResult.getCheckInDate()).isEqualTo(LocalDate.of(2025, 11, 10)),
                () -> assertThat(actualResult.getCheckOutDate()).isEqualTo(LocalDate.of(2025, 11, 15))
        );
    }

    @Test
    void getOrderById() {
        Order order = addOrderToSession();
        session.flush();
        session.clear();

        Order actualResult = session.getReference(Order.class, order.getId());

        assertThat(actualResult.getId()).isEqualTo(order.getId());
    }

    @Test
    void deleteOrder() {
        Order order = addOrderToSession();
        Order actualResult = session.getReference(Order.class, order.getId());
        session.remove(actualResult);
        session.flush();
        session.clear();

        Optional<Order> deletedOrder = Optional.ofNullable(session.find(Order.class, order.getId()));

        assertThat(deletedOrder).isEmpty();
    }

    private static Hotel getHotel() {
        return Hotel.builder()
                .name("Minsk")
                .photo("hotelphoto007.jpg")
                .build();
    }

    private static Room getRoom() {
        return Room.builder()
                .occupancy(5)
                .roomClass(RoomClassEnum.ECONOMY)
                .photo("photoroom004.jpg")
                .pricePerDay(89)
                .hotel(getHotel())
                .build();
    }

    private static User getUser() {
        return User.builder()
                .firstName("Vanya")
                .lastName("Vanyev")
                .email("vanya66@gmail.com")
                .phone("+375291534863")
                .photo("userphoto006.jpg")
                .birthDate(LocalDate.of(1995, 10, 15))
                .money(2500)
                .password("12345")
                .role(RoleEnum.USER)
//                .isActive(true)
                .build();
    }

    private static Order getOrder() {
        return Order.builder()
                .user(getUser())
                .room(getRoom())
                .status(OrderStatusEnum.OPEN)
                .paymentStatus(PaymentStatusEnum.APPROVED)
                .checkInDate(LocalDate.of(2024, 10, 15))
                .checkOutDate(LocalDate.of(2024, 10, 25))
                .build();
    }

    private Order addOrderToSession() {
        Order order = getOrder();
        User user = order.getUser();
        Room room = order.getRoom();
        Hotel hotel = room.getHotel();

        session.persist(user);
        session.persist(room);
        session.persist(hotel);
        session.persist(order);
        return order;
    }
}

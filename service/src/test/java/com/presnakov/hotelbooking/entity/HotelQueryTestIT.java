package com.presnakov.hotelbooking.entity;

import com.presnakov.hotelbooking.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class HotelQueryTestIT {

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @AfterAll
    public void finish() {
        sessionFactory.close();
    }

    @Test
    void findAll() {

        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        initData(session);

        List<Hotel> results = session.createQuery("select h from Hotel h", Hotel.class)
                .list();

        assertThat(results).hasSize(2);

        List<String> fullNames = results.stream().map(Hotel::getName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Plaza", "Minsk");

        session.getTransaction().commit();
    }

    private static void initData(Session session) {
        Hotel hotel1 = saveHotel(session, "Plaza", "hotelphoto001.jpg");
        Hotel hotel2 = saveHotel(session, "Minsk", "hotelphoto002.jpg");

        Room room1 = saveRoom(session, RoomClassEnum.ECONOMY, 29, "roomphoto001.jpg", 2, hotel1);
        Room room2 = saveRoom(session, RoomClassEnum.COMFORT, 49, "roomphoto002.jpg", 3, hotel1);
        Room room3 = saveRoom(session, RoomClassEnum.BUSINESS, 69, "roomphoto003.jpg", 4, hotel1);
        Room room4 = saveRoom(session, RoomClassEnum.ECONOMY, 39, "roomphoto004.jpg", 2, hotel2);
        Room room5 = saveRoom(session, RoomClassEnum.COMFORT, 59, "roomphoto005.jpg", 3, hotel2);
        Room room6 = saveRoom(session, RoomClassEnum.BUSINESS, 79, "roomphoto006.jpg", 4, hotel2);
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

}

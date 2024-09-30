package com.presnakov.hotelbooking.integration.entity;

import com.presnakov.hotelbooking.entity.Hotel;
import com.presnakov.hotelbooking.entity.Room;
import com.presnakov.hotelbooking.entity.RoomClassEnum;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class HotelTestIT {

    @Test
    void upsert() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        @Cleanup SessionFactory sessionFactory = configuration.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Hotel hotel = getHotel();

        session.save(hotel);
        session.getTransaction().commit();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());

        assertThat(actualResult.getId()).isEqualTo(hotel.getId());
    }

    @Test
    void deleteRoomFromHotel() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        @Cleanup SessionFactory sessionFactory = configuration.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Hotel hotel = getHotel();
        Room room = getRoom();

        hotel.addRoom(room);

        session.save(hotel);
        session.getTransaction().commit();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());

        actualResult.getRooms().removeIf(actualRoom -> actualRoom.getId().equals(room.getId()));

        assertThat(actualResult.getRooms().removeIf(actualRoom -> actualRoom.getId().equals(room.getId())));
    }

    @Test
    void getHotelById() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        @Cleanup SessionFactory sessionFactory = configuration.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Hotel hotel = getHotel();

        session.save(hotel);
        session.getTransaction().commit();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());

        assertThat(actualResult.getId()).isEqualTo(hotel.getId());
    }

    @Test
    void deleteHotel() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        @Cleanup SessionFactory sessionFactory = configuration.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Hotel hotel = getHotel();

        session.save(hotel);
        session.getTransaction().commit();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());
        session.delete(actualResult);

        Optional<Hotel> deletedHotel = Optional.ofNullable(session.find(Hotel.class, hotel.getId()));
        assertThat(deletedHotel).isEmpty();
    }

    @Test
    void addRoomToNewHotel() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        @Cleanup SessionFactory sessionFactory = configuration.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Hotel hotel = getHotel();
        Room room = getRoom();
        hotel.addRoom(room);

        session.save(hotel);

        Hotel actualHotel = session.getReference(Hotel.class, hotel.getId());
        Room actualRoom = session.getReference(Room.class, room.getId());

        assertThat(actualHotel.getId()).isEqualTo(hotel.getId());
        assertThat(actualRoom.getId()).isEqualTo(room.getId());
    }

    private static Room getRoom() {
        return Room.builder()
                .roomClass(RoomClassEnum.ECONOMY)
                .pricePerDay(49)
                .photo("photo0005.jpg")
                .occupancy(3)
                .build();
    }

    private static Hotel getHotel() {
        return Hotel.builder()
                .photo("hotelphoto005.jpg")
                .name("Bobruisk5")
                .build();
    }
}

package com.presnakov.hotelbooking.entity;

import com.presnakov.hotelbooking.integration.EntityTestBase;
import com.presnakov.hotelbooking.util.TestDataImporter;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class HotelTestIT extends EntityTestBase {

    @Test
    void findAllHql() {
        TestDataImporter.importData(session);

        List<Hotel> results = session.createQuery("select h from Hotel h", Hotel.class)
                .list();
        List<String> hotelNames = results.stream()
                .map(Hotel::getName)
                .collect(toList());

        assertThat(results).hasSize(2);
        assertThat(hotelNames).containsExactlyInAnyOrder("Plaza", "Minsk");
    }

    @Test
    void findByNameHql() {
        TestDataImporter.importData(session);

        String hotelName = "Plaza";
        Optional<Hotel> actualResult = session.createQuery("select h from Hotel h " +
                                                           "where h.name = :name", Hotel.class)
                .setParameter("name", hotelName)
                .uniqueResultOptional();
        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult.get().getName()).isEqualTo(hotelName);
    }

    @Test
    void findAllCriteria() {
        TestDataImporter.importData(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Hotel.class);

        var hotel = criteria.from(Hotel.class);
        criteria.select(hotel);
        List<Hotel> results = session.createQuery(criteria)
                .list();
        List<String> hotelNames = results.stream()
                .map(Hotel::getName)
                .collect(toList());

        assertThat(results).hasSize(2);
        assertThat(hotelNames).containsExactlyInAnyOrder("Plaza", "Minsk");
    }

    @Test
    void findByNameCriteria() {
        TestDataImporter.importData(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Hotel.class);
        String hotelName = "Plaza";
        var hotel = criteria.from(Hotel.class);

        criteria.select(hotel).where(
                cb.equal(hotel.get("name"), hotelName));
        Optional<Hotel> actualResult = session.createQuery(criteria)
                .uniqueResultOptional();

        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult.get().getName()).isEqualTo(hotelName);
    }

    @Test
    void findAllQueryDsl() {
        TestDataImporter.importData(session);
        List<Hotel> results = new JPAQuery<Hotel>(session)
                .select(QHotel.hotel)
                .from(QHotel.hotel)
                .fetch();

        List<String> hotelNames = results.stream()
                .map(Hotel::getName)
                .collect(toList());

        assertThat(results).hasSize(2);
        assertThat(hotelNames).containsExactlyInAnyOrder("Plaza", "Minsk");
    }

    @Test
    void findByNameQueryDsl() {
        TestDataImporter.importData(session);
        String hotelName = "Plaza";

        Hotel actualResult = new JPAQuery<Hotel>(session)
                .select(QHotel.hotel)
                .from(QHotel.hotel)
                .where(QHotel.hotel.name.eq(hotelName))
                .fetchOne();

        assertThat(actualResult.getName()).isEqualTo(hotelName);
    }

    @Test
    void createHotel() {
        Hotel hotel = getHotel();
        session.persist(hotel);
        session.flush();
        session.clear();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());

        assertThat(actualResult.getId()).isEqualTo(hotel.getId());
    }

    @Test
    void updateHotel() {
        Hotel hotel = getHotel();
        session.persist(hotel);
        hotel.setName("Europe");
        hotel.setPhoto("hotelphoto12345.jpg");
        session.merge(hotel);
        session.flush();
        session.clear();

        Hotel actualResult = session.get(Hotel.class, hotel.getId());

        assertAll(
                () -> assertThat(actualResult.getName()).isEqualTo("Europe"),
                () -> assertThat(actualResult.getPhoto()).isEqualTo("hotelphoto12345.jpg")
        );
    }

    @Test
    void getHotelById() {
        Hotel hotel = getHotel();
        session.persist(hotel);
        session.flush();
        session.clear();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());

        assertThat(actualResult.getId()).isEqualTo(hotel.getId());
    }

    @Test
    void deleteHotel() {
        Hotel hotel = getHotel();
        session.persist(hotel);
        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());
        session.remove(actualResult);
        session.flush();
        session.clear();

        Optional<Hotel> deletedHotel = Optional.ofNullable(session.find(Hotel.class, hotel.getId()));

        assertThat(deletedHotel).isEmpty();
    }

    @Test
    void addRoomToNewHotel() {
        Hotel hotel = getHotel();
        Room room = getRoom();
        hotel.addRoom(room);
        session.persist(hotel);
        session.flush();
        session.clear();

        Hotel actualHotel = session.getReference(Hotel.class, hotel.getId());
        Room actualRoom = session.getReference(Room.class, room.getId());

        assertAll(
                () -> assertThat(actualHotel.getId()).isEqualTo(hotel.getId()),
                () -> assertThat(actualRoom.getId()).isEqualTo(room.getId())
        );
    }

    @Test
    void deleteRoomFromHotel() {
        Hotel hotel = getHotel();
        Room room = getRoom();
        hotel.addRoom(room);
        session.persist(hotel);
        session.flush();
        session.clear();

        Hotel actualResult = session.getReference(Hotel.class, hotel.getId());
        actualResult.getRooms().removeIf(actualRoom -> actualRoom.getId().equals(room.getId()));

        assertThat(actualResult.getRooms().removeIf(actualRoom -> actualRoom.getId().equals(room.getId())));
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
                .name("Bobruisk")
                .build();
    }
}

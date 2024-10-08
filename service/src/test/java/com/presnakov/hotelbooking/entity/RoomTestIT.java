package com.presnakov.hotelbooking.entity;

import com.presnakov.hotelbooking.integration.EntityTestBase;
import com.presnakov.hotelbooking.util.TestDataImporter;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.query.criteria.JpaJoin;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RoomTestIT extends EntityTestBase {

    //test with HQL
    @Test
    void findAllByHotelNameClassOccupancyPrice() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataImporter.importData(session);
        String hotelName = "Plaza";
        Integer occupancy = 2;
        Integer pricePerDay = 29;
        RoomClassEnum comfortClass = RoomClassEnum.ECONOMY;

        List<Room> results = session.createQuery("select r from Hotel h " +
                                                 "join h.rooms r " +
                                                 "where h.name = :hotelName " +
                                                 "and r.roomClass = :comfortClass " +
                                                 "and r.occupancy = :occupancy " +
                                                 "and r.pricePerDay = :pricePerDay", Room.class)
                .setParameter("hotelName", hotelName)
                .setParameter("comfortClass", comfortClass)
                .setParameter("occupancy", occupancy)
                .setParameter("pricePerDay", pricePerDay)
                .list();

        assertThat(results).hasSize(1);
        session.getTransaction().commit();
    }

    //test with Criteria
    @Test
    void findAllByHotelNameClassOccupancyPriceCriteria() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        TestDataImporter.importData(session);
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Room.class);
        var hotel = criteria.from(Hotel.class);
        JpaJoin<Hotel, Room> rooms = hotel.join("rooms");
        String hotelName = "Plaza";
        Integer occupancy = 2;
        Integer pricePerDay = 29;
        RoomClassEnum roomClass = RoomClassEnum.ECONOMY;

        criteria.select(rooms).where(
                cb.equal(hotel.get("name"), hotelName),
                cb.equal(rooms.get("occupancy"), occupancy),
                cb.equal(rooms.get("pricePerDay"), pricePerDay),
                cb.equal(rooms.get("roomClass"), roomClass)
        );
        Optional<Room> actualResult = session.createQuery(criteria)
                .uniqueResultOptional();

        assertAll(
                () -> assertThat(actualResult.isPresent()).isTrue(),
                () -> assertThat(actualResult.get().getHotel().getName().equals(hotelName)),
                () -> assertThat(actualResult.get().getOccupancy().equals(occupancy)),
                () -> assertThat(actualResult.get().getPricePerDay().equals(pricePerDay)),
                () -> assertThat(actualResult.get().getRoomClass().equals(roomClass))
        );
        session.getTransaction().commit();
    }

    @Test
    void createRoom() {
        Room room = getRoom();
        session.persist(room);
        session.flush();
        session.clear();

        Room actualResult = session.getReference(Room.class, room.getId());

        assertThat(actualResult.getId()).isEqualTo(room.getId());
    }

    @Test
    void updateRoom() {
        Room room = getRoom();
        session.persist(room);
        room.setRoomClass(RoomClassEnum.COMFORT);
        room.setOccupancy(4);
        room.setPricePerDay(69);
        room.setPhoto("roomphoto007.jpg");
        session.merge(room);
        session.flush();
        session.clear();

        Room actualResult = session.getReference(Room.class, room.getId());

        assertAll(
                () -> assertThat(actualResult.getRoomClass().equals(RoomClassEnum.COMFORT)),
                () -> assertThat(actualResult.getOccupancy()).isEqualTo(4),
                () -> assertThat(actualResult.getPricePerDay().equals(69)),
                () -> assertThat(actualResult.getPhoto()).isEqualTo("roomphoto007.jpg")
        );
    }

    @Test
    void getRoomById() {
        Room room = getRoom();
        session.persist(room);
        session.flush();
        session.clear();

        Room actualResult = session.getReference(Room.class, room.getId());

        assertThat(actualResult.getId()).isEqualTo(room.getId());
    }

    @Test
    void deleteRoom() {
        Room room = getRoom();
        session.persist(room);
        Room actualResult = session.getReference(Room.class, room.getId());
        session.remove(actualResult);
        session.flush();
        session.clear();

        Optional<Room> deletedRoom = Optional.ofNullable(session.find(Room.class, room.getId()));

        assertThat(deletedRoom).isEmpty();
    }

    private static Room getRoom() {
        return Room.builder()
                .roomClass(RoomClassEnum.ECONOMY)
                .pricePerDay(49)
                .photo("photo0005.jpg")
                .occupancy(3)
                .build();
    }
}

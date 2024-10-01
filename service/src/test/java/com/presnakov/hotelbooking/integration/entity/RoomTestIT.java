package com.presnakov.hotelbooking.integration.entity;

import com.presnakov.hotelbooking.entity.Room;
import com.presnakov.hotelbooking.entity.RoomClassEnum;
import com.presnakov.hotelbooking.integration.integration.EntityTestBase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RoomTestIT extends EntityTestBase {

    @Test
    void createRoom() {
        Room room = getRoom();

        session.persist(room);
        session.flush();
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
        Room actualResult = session.getReference(Room.class, room.getId());

        assertThat(actualResult.getId()).isEqualTo(room.getId());
    }

    @Test
    void deleteRoom() {
        Room room = getRoom();

        session.persist(room);
        Room actualResult = session.getReference(Room.class, room.getId());
        session.remove(actualResult);
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

package com.presnakov.hotelbooking.integration.entity;

import com.presnakov.hotelbooking.entity.Hotel;
import com.presnakov.hotelbooking.entity.Room;
import com.presnakov.hotelbooking.entity.RoomClassEnum;
import com.presnakov.hotelbooking.integration.integration.EntityTestBase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class HotelTestIT extends EntityTestBase {

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

package com.presnakov.hotelbooking.util;

import com.presnakov.hotelbooking.entity.Hotel;
import com.presnakov.hotelbooking.entity.Room;
import com.presnakov.hotelbooking.entity.RoomClassEnum;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UtilityClass
public class TestDataImporter {

    public static void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();

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

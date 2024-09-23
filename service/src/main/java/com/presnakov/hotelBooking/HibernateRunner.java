package com.presnakov.hotelBooking;

import com.presnakov.hotelBooking.entity.Hotel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;


public class HibernateRunner {

    public static void main(String[] args)  throws SQLException {
        Configuration configuration = new Configuration();

        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Hotel hotel = Hotel.builder()
                    .name("Hilton")
                    .photo("hotelphoto01.jpg")
                    .build();

            session.save(hotel);

            session.getTransaction().commit();
        }
    }
}
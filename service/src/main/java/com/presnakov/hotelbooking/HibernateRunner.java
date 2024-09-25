package com.presnakov.hotelbooking;

import com.presnakov.hotelbooking.entity.Hotel;
import com.presnakov.hotelbooking.entity.RoleEnum;
import com.presnakov.hotelbooking.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.time.LocalDate;


public class HibernateRunner {

    public static void main(String[] args)  throws SQLException {
        Configuration configuration = new Configuration();

        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Hotel hotel = Hotel.builder()
                    .name("Hilton2")
                    .photo("hotelphoto02.jpg")
                    .build();

            User user = User.builder()
                    .firstName("Vasya")
                    .lastName("Vasilyev")
                    .email("vasya25@gmail.com")
                    .phone("+375291534863")
                    .photo("userphoto001.jpg")
                    .birthDate(LocalDate.of(1995, 10, 15))
                    .money(2500)
                    .password("12345")
                    .role(RoleEnum.USER)
                    .isActive(true)
                    .build();

            session.saveOrUpdate(hotel);
            session.saveOrUpdate(user);

            session.getTransaction().commit();
        }
    }
}
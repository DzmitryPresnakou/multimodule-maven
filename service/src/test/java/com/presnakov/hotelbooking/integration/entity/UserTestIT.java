package com.presnakov.hotelbooking.integration.entity;

import com.presnakov.hotelbooking.entity.RoleEnum;
import com.presnakov.hotelbooking.entity.User;
import com.presnakov.hotelbooking.integration.integration.EntityTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserTestIT extends EntityTestBase {

    @Test
    void createUser() {
        User user = getUser();
        session.persist(user);
        session.flush();
        session.clear();

        User actualResult = session.getReference(User.class, user.getId());

        assertThat(actualResult.getId()).isEqualTo(user.getId());
    }

    @Test
    void updateUser() {
        User user = getUser();
        session.persist(user);
        user.setFirstName("Kolya");
        user.setLastName("Petrov");
        user.setPhone("+375296583652");
        user.setPhoto("userphoto0012.jpg");
        user.setBirthDate(LocalDate.of(1990, 11, 8));
        user.setMoney(5000);
        user.setPassword("963258");
        user.setRole(RoleEnum.ADMIN);
        user.setIsActive(false);
        session.merge(user);
        session.flush();
        session.clear();

        User actualResult = session.getReference(User.class, user.getId());

        assertAll(
                () -> assertThat(actualResult.getFirstName().equals("Kolya")),
                () -> assertThat(actualResult.getLastName().equals("Petrov")),
                () -> assertThat(actualResult.getPhone().equals("375296583652")),
                () -> assertThat(actualResult.getPhoto().equals("userphoto0012.jpg")),
                () -> assertThat(actualResult.getBirthDate().equals(LocalDate.of(1990, 11, 8))),
                () -> assertThat(actualResult.getMoney().equals(5000)),
                () -> assertThat(actualResult.getPassword().equals("963258")),
                () -> assertThat(actualResult.getRole().equals(RoleEnum.ADMIN)),
                () -> assertThat(actualResult.getIsActive().equals(false))
        );
    }

    @Test
    void getUserById() {
        User user = getUser();
        session.persist(user);
        session.flush();
        session.clear();

        User actualResult = session.getReference(User.class, user.getId());

        assertThat(actualResult.getId()).isEqualTo(user.getId());
    }

    @Test
    void deleteUser() {
        User user = getUser();
        session.persist(user);
        User actualResult = session.getReference(User.class, user.getId());
        session.remove(actualResult);
        session.flush();
        session.clear();

        Optional<User> deletedRoom = Optional.ofNullable(session.find(User.class, user.getId()));

        assertThat(deletedRoom).isEmpty();
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
                .build();
    }
}

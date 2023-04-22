package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.DbUserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(scripts = {"classpath:add_test_objects.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = {"classpath:remove_test_objects.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})

public class UserDaoTest {

    private final DbUserStorage storage;

    @Test
    void addFriend() {
        User wrongUser = User.builder()
                .email("mail@mail.com")
                .login("testUser2")
                .name(null)
                .birthday(LocalDate.of(1985, Month.AUGUST, 21))
                .build();
        storage.createUser(wrongUser);
        storage.addFriend(wrongUser, storage.getUserById(0).orElseThrow());
        List<User> friends = storage.getFriendsList(storage.getUserById(0).orElseThrow());
        Assertions.assertEquals(friends.size(), 0);
    }

    @Test
    void removeFriend() {
        User wrongUser = User.builder()
                .email("mail@mail.com")
                .login("testUser2")
                .name(null)
                .birthday(LocalDate.of(1985, Month.AUGUST, 21))
                .build();
        storage.createUser(wrongUser);

        storage.addFriend(wrongUser, storage.getUserById(0).orElseThrow());
        storage.addFriend(storage.getUserById(0).orElseThrow(), wrongUser);
        List<User> friends = storage.getFriendsList(storage.getUserById(0).orElseThrow());
        Assertions.assertEquals(friends.size(), 1);

        storage.removeFriend(storage.getUserById(0).orElseThrow(), wrongUser);
        List<User> friends2 = storage.getFriendsList(storage.getUserById(0).orElseThrow());
        Assertions.assertEquals(friends2.size(), 0);

    }

    @Test
    void getFriendsList() {
        User wrongUser = User.builder()
                .email("mail@mail.com")
                .login("testUser2")
                .name(null)
                .birthday(LocalDate.of(1985, Month.AUGUST, 21))
                .build();
        storage.createUser(wrongUser);

        storage.addFriend(wrongUser, storage.getUserById(0).orElseThrow());
        storage.addFriend(storage.getUserById(0).orElseThrow(), wrongUser);
        List<User> friends = storage.getFriendsList(storage.getUserById(0).orElseThrow());
        Assertions.assertEquals(friends.size(), 1);
        Assertions.assertEquals(friends.get(0), wrongUser);
    }

    @Test
    void getAllUsers() {
        List<User> users = storage.getAllUsers();
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0), User.builder()
                .id(0)
                .email("mail@mail.com")
                .login("user1")
                .name("name")
                .birthday(LocalDate.of(1985, Month.AUGUST, 21))
                .build());
    }

    @Test
    void getUserById() {
        User user = storage.getUserById(0).orElseThrow();
        assertThat(Optional.of(user))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 0)
                                .hasFieldOrPropertyWithValue("email", "mail@mail.com")
                                .hasFieldOrPropertyWithValue("login", "user1")
                                .hasFieldOrPropertyWithValue("name", "name")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1985, Month.AUGUST, 21)
                                ));

    }

    @Test
    void createUser() {
        User wrongUser = User.builder()
                .email("mail@mail.com")
                .login("testUser2")
                .name("testUser2")
                .birthday(LocalDate.of(1985, Month.AUGUST, 21))
                .build();
        User user = storage.createUser(wrongUser);
        assertThat(Optional.ofNullable(user))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it)
                                .hasFieldOrPropertyWithValue("email", "mail@mail.com")
                                .hasFieldOrPropertyWithValue("login", "testUser2")
                                .hasFieldOrPropertyWithValue("name", "testUser2")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1985, Month.AUGUST, 21))
                );
    }

    @Test
    void updateUser() {
        User updatedUser = User.builder()
                .id(0)
                .email("updated@mail.com")
                .login("updatedLogin")
                .name("updatedLogin")
                .birthday(LocalDate.of(1986, Month.AUGUST, 21))
                .build();

        storage.updateUser(updatedUser);

        User testUser = storage.getUserById(0).orElseThrow();

        assertThat(Optional.of(testUser))
                .isPresent()
                .hasValueSatisfying(it ->
                        assertThat(it).hasFieldOrPropertyWithValue("id", 0)
                                .hasFieldOrPropertyWithValue("email", "updated@mail.com")
                                .hasFieldOrPropertyWithValue("login", "updatedLogin")
                                .hasFieldOrPropertyWithValue("name", "updatedLogin")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1986, Month.AUGUST, 21))
                );
    }


}

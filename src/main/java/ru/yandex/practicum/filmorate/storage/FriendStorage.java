package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(User user, User otherUser);

    void removeFriend(User user, User otherUser);

    List<User> getFriendsList(User user);

    List<User> getMutualFriends(User user, User friend);
}

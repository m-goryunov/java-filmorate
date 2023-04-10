package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]*$")
    private String login;
    private String name;
    @PastOrPresent
    @NotNull
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();
    private boolean isFriendRequestAccepted;

    public void addFriend(User user) {
        friends.add(user.getId());
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
    }

}

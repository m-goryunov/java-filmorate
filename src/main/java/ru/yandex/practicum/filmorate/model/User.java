package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

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
}

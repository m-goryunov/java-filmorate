package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @Id
    private Integer id;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]*$")
    private String login;
    @PastOrPresent
    @NotNull
    private LocalDate birthday;
    private String name;
}

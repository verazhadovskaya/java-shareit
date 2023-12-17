package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    @NotBlank
    @NotEmpty
    @Email
    private String email;
}

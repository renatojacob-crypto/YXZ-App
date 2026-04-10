package com.enzo.yxzapp.dto.user;

import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank String senha,
        @NotNull Role role,
        CorAdministradora corAdministradora
) {}
package com.enzo.yxzapp.dto.auth;

import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.Role;

public record LoginResponse(
        String token,
        Long userId,
        String nome,
        Role role,
        CorAdministradora corAdministradora
) {}
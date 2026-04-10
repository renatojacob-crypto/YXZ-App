package com.enzo.yxzapp.dto.user;

import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.Role;

public record UpdateUserRequest(
        String nome,
        Role role,
        CorAdministradora corAdministradora,
        Boolean ativo
) {}
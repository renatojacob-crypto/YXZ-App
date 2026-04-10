package com.enzo.yxzapp.dto.user;

import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.Role;
import com.enzo.yxzapp.model.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String nome,
        String email,
        Role role,
        CorAdministradora corAdministradora,
        boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static UserResponse fromEntity(User u) {
        return new UserResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getRole(),
                u.getCorAdministradora(),
                u.isAtivo(),
                u.getCriadoEm(),
                u.getAtualizadoEm()
        );
    }
}
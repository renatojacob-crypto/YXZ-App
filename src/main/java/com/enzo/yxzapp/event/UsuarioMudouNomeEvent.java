package com.enzo.yxzapp.event;

public record UsuarioMudouNomeEvent(
        Long usuarioId,
        String novoNome
) {
}

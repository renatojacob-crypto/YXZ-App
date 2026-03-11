package com.enzo.yxzapp.event;

public record ArquivoDriveDTO(
        String nomeOriginal,
        String contentType,
        byte[] conteudo
){}
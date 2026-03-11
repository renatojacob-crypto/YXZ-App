package com.enzo.yxzapp.event;

import java.util.List;

public record FotosOficinaEnviadasEvent(
        Long oficinaId,
        String nomePasta,
        List<ArquivoDriveDTO> fotos
){}

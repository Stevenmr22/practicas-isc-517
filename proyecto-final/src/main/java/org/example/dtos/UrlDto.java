package org.example.dtos;

import org.example.entidades.Acceso;

import java.util.Date;
import java.util.List;

public record UrlDto(String id,
                     String urlOriginal,
                     String titulo,
                     String usernameCreador,
                     Date fechaCreacion,
                     List<Acceso> accesos
) { }

package org.example.encapsulaciones;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {
    private int id;
    private String mensaje;
    private String usuarioSender;
    private LocalDateTime fecha;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Mensaje(int id, String mensaje, String usuarioSender, LocalDateTime fecha) {
        this.id = id;
        this.mensaje = mensaje;
        this.usuarioSender = usuarioSender;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsuarioSender() {
        return usuarioSender;
    }

    public void setUsuarioSender(String usuarioSender) {
        this.usuarioSender = usuarioSender;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        String fecha = this.fecha.format(formatter);
        return "<li class=\"list-group-item\">\n" +
                "<div class=\"d-flex justify-content-between\">\n" +
                "<h6 class=\"mb-1 text-primary\">"+usuarioSender+"</h6>\n" +
                "<small>"+fecha+"</small>\n" +
                "</div>\n" +
                "<p class=\"mb-1\">"+mensaje+"</p>\n" +
                "</li>";
    }
}

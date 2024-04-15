package org.example.encapsulaciones;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private int id;
    private String usuario;
    private String ultimoMensaje;
    private final List<Session> usuariosConectados = new ArrayList<>();
    private final List<Mensaje> mensajes = new ArrayList<>();

    public Chat(int id, String usuario, String ultimoMensaje) {
        this.id = id;
        this.usuario = usuario;
        this.ultimoMensaje = ultimoMensaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Session> getUsuariosConectados() {
        return usuariosConectados;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public String getUsuario() {
        return usuario;
    }
    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
}

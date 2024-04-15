package org.example.servicios;

import org.example.encapsulaciones.Chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiciosChat {
    private static ServiciosChat instancia;
    private final Map<Integer, Chat> chats = new ConcurrentHashMap<>();

    private ServiciosChat() { }

    public static ServiciosChat getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosChat();
        }
        return instancia;
    }

    public Map<Integer, Chat> getChats() {
        return chats;
    }
}

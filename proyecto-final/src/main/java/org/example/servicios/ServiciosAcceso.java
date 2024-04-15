package org.example.servicios;

import org.example.entidades.Acceso;
import org.example.entidades.Url;

public class ServiciosAcceso extends GestionDB<Acceso> {
    private static ServiciosAcceso instancia;
    private ServiciosAcceso() { super(Acceso.class); }

    public static ServiciosAcceso getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosAcceso();
        }
        return instancia;
    }
}

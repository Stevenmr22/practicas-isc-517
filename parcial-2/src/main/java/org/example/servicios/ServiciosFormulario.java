package org.example.servicios;

import org.example.encapsulaciones.Formulario;
import org.example.utilitarios.GestionDb;

public class ServiciosFormulario extends GestionDb<Formulario> {
    private static ServiciosFormulario instancia;
    public ServiciosFormulario() { super(Formulario.class); }

    public static ServiciosFormulario getInstancia() {
        if(instancia==null) {
            instancia = new ServiciosFormulario();
        }
        return instancia;
    }
}

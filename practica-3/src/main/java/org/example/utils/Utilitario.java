package org.example.utils;

import org.example.encapsulaciones.Etiqueta;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.ServiciosEtiqueta;
import org.example.servicios.ServiciosUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utilitario {

    //Método para agregar el user en la navbar
    public static Map<String, Object> modeloBase(String username, ServiciosUsuario serviciosUsuario){
        Map<String, Object> model = new HashMap<>();
        Usuario user = serviciosUsuario.getUsuarioPorUsername(username);
        if(user == null) return model;

        model.put("username", user.getNombre());
        if(user.isAutor()) model.put("autor", true);
        if(user.isAdministrator()) model.put("admin", true);
        return model;
    }

    public static boolean parseCheckbox(String checkbox){
        if(checkbox != null) return checkbox.equals("on");
        return false;
    }

    public static boolean falseIfNull(Boolean bool){
        if(bool == null) return false;
        return bool;
    }
}

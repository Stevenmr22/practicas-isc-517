package org.example.utilitarios;

import io.jsonwebtoken.Claims;
import org.example.controladores.UsuarioControlador;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.ServiciosUsuario;

import java.util.HashMap;
import java.util.Map;

public class Utilitario {
    public static Map<String, Object> modeloBase(Claims claimsToken){
        Map<String, Object> model = new HashMap<>();
        if(claimsToken == null) return model;

        model.put("username", claimsToken.get("username"));
        model.put("admin", claimsToken.get("admin"));
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

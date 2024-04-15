package org.example.servicios;

import org.example.encapsulaciones.Usuario;
import org.example.utilitarios.GestionDb;

import java.util.List;

public class ServiciosUsuario extends GestionDb<Usuario> {
    private static ServiciosUsuario instancia;

    private ServiciosUsuario(){
        super(Usuario.class);
        if(find("admin") == null)
            crear(new Usuario("admin", "admin", "Administrador", true));
    }
    public static ServiciosUsuario getInstancia(){
        if(instancia==null){
            instancia = new ServiciosUsuario();
        }
        return instancia;
    }

    public void agregarUsuario(Usuario usuario) {
        crear(usuario);
    }

    public Usuario validarUsuario(String username, String password){
        Usuario usuario = find(username);
        if(usuario != null && usuario.getPassword().equals(password))
            return usuario;
        return null;
    }
    public List<Usuario> obtenerUsuarios(){
        return findAll();
    }

    public boolean usernameExiste(String username) {
        Usuario usuario = find(username);
        return usuario != null;
    }

    public Usuario getUsuarioPorUsername(String username){
        try{
            return find(username);
        }catch (Exception e){
            return null;
        }
    }

}

package org.example.servicios;

import org.example.encapsulaciones.Usuario;

import java.util.ArrayList;

public class ServiciosUsuario {
    private static ServiciosUsuario instancia;
    private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    /**
     * Crea el usuario administrador al iniciar el programa.
     * USERNAME: admin, PASSWORD: admin
     */
    private ServiciosUsuario(){
        Usuario admin = new Usuario("admin", "Administrador", "admin",
                true, true);
        agregarUsuario(admin);
    }

    public static ServiciosUsuario getInstancia(){
        if(instancia==null){
            instancia = new ServiciosUsuario();
        }
        return instancia;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario validarUsuario(String username, String password){
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario getUsuarioPorUsername(String username){
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username))
                return usuario;
        }
        return null;
    }

    public boolean usernameExiste(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username))
                return true;
        }
        return false;
    }
}

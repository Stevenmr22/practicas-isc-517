package controladores;

import org.example.encapsulaciones.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoginController {
    private static LoginController instancia;
    private List<Usuario> listaUsuarios = new ArrayList<>();

    private LoginController(){
        listaUsuarios.add(new Usuario("Admin", "Gustavo", "1234", true, true));
    }
    public static LoginController getInstancia(){
        if(instancia==null){
            instancia = new LoginController();
        }
        return instancia;
    }

    public Usuario autheticarUsuario(String usuario, String password){
        //simulando la busqueda en la base de datos.
        return listaUsuarios.stream().filter(u -> u.getUsername().equals(usuario) && u.getPassword().equals(password)).findFirst().orElse(null);
    }
    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }

    public Usuario getUsuarioPorUsername(String username){
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }
}


package org.example.servicios;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import org.example.entidades.Usuario;

public class ServiciosUsuario extends GestionDB<Usuario> {

    private static ServiciosUsuario instancia;
    private ServiciosUsuario() {
        super(Usuario.class);

        if(find().count() == 0)
            crear(new Usuario("admin", "Administrador", "admin", true));
    }

    public static ServiciosUsuario getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosUsuario();
        }
        return instancia;
    }

    public Usuario autenticarUsuario(String username, String password) {
        Query<Usuario> query = find().filter(Filters.and(Filters.eq("username", username), Filters.eq("password", password)));
        if(query.count() == 0) { return null; }
        return query.first();
    }
    public boolean existeUsuario(String username) {
        Query<Usuario> query = find().filter(Filters.eq("username", username));
        return query.count() > 0;
    }
    public Usuario getUsuarioPorUsername(String username) {
        Query<Usuario> query = find().filter(Filters.eq("username", username));
        return query.first();
    }
}

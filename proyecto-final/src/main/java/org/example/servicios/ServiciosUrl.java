package org.example.servicios;

import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.filters.Filters;
import org.example.entidades.Url;

import java.util.List;

public class ServiciosUrl extends GestionDB<Url> {
    private static ServiciosUrl instancia;

    private ServiciosUrl() {
        super(Url.class);
    }
    public static ServiciosUrl getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosUrl();
        }
        return instancia;
    }

    public List<Url> getUrlByUsername(String username){
        return find()
                    .filter(Filters.eq("usernameCreador", username))
                    .iterator(new FindOptions().sort(Sort.descending("fechaCreacion")))
                    .toList();
    }
}

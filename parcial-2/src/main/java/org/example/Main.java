package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.jsonwebtoken.Claims;
import org.example.controladores.FormularioControlador;
import org.example.controladores.UsuarioControlador;
import org.example.servicios.ServiciosBootstrap;
import org.example.servicios.ServiciosUsuario;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ServiciosBootstrap.getInstancia().startDb();
        ServiciosUsuario.getInstancia();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/publico";
                staticFiles.location = Location.CLASSPATH;
            });
        }
        ).start(7000);

        app.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            Claims claimsToken = UsuarioControlador.obtenerClaims(ctx);
            if(claimsToken != null){
                model.put("username", claimsToken.get("username"));
                model.put("nombre", claimsToken.get("nombre"));
                model.put("admin", claimsToken.get("admin"));
            }
            ctx.render("/vistas/index.html", model);
        });

        app.get("/error", ctx -> ctx.render("/publico/offline.html"));

        new UsuarioControlador(app).aplicarRutas();
        new FormularioControlador(app).aplicarRutas();
    }
}
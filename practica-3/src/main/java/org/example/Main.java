package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.example.controladores.ArticuloControlador;
import org.example.controladores.UsuarioControlador;
import org.example.servicios.ServiciosEtiqueta;
import org.example.servicios.ServiciosUsuario;
import org.example.servicios.ServiciosArticulo;

import java.util.Map;

import static org.example.utils.Utilitario.modeloBase;

public class Main {
    public static void main(String[] args) {
        ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFilesConfig -> {
                staticFilesConfig.directory = "/publico";
                staticFilesConfig.location = Location.CLASSPATH;
            });
        }).start(7000);

        app.get("/", ctx -> {
            ServiciosArticulo serviciosArticulo = ServiciosArticulo.getInstancia();
            Map<String, Object> model = modeloBase(ctx.sessionAttribute("username"), serviciosUsuario);
            model.put("articulos", serviciosArticulo.getArticulos());
            model.put("etiquetas", ServiciosEtiqueta.getInstancia().getEtiquetas());
            ctx.render("/vistas/index.html", model);
        });

        new UsuarioControlador(app).aplicarRutas();
        new ArticuloControlador(app).aplicarRutas();
    }

}
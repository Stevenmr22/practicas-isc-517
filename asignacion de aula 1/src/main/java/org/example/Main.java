package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import org.example.encapsulation.Usuario;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<String, Usuario> usuarios = new HashMap<>();

    public static void main(String[] args) {

        usuarios.put("admin", new Usuario("admin", "admin"));

        Javalin app = Javalin.create(config ->{
            //configurando los documentos estáticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;
            });
        }).start(7000);

        app.before("/", ctx -> {
            if (ctx.sessionAttribute("usuario") == null){
                ctx.redirect("/login");
            }
        });

        app.get("/" , ctx -> {
            ctx.render("/views/index.html");
        });

        app.get("/login", ctx -> {
            if (ctx.sessionAttribute("usuario") != null) {
                ctx.redirect("/");
            } else {
                ctx.render("/views/login.html");
            }
        });

        app.post("/login", ctx -> {
            String usuario = ctx.formParam("usuario");
            String contrasena = ctx.formParam("contrasena");
            Usuario user = usuarios.get(usuario);
            if(user != null && user.autenticar(usuario, contrasena)){
                ctx.sessionAttribute("usuario", usuario);
                ctx.redirect("/");
            }else{
                ctx.redirect("/login");
            }
        });

    }
}

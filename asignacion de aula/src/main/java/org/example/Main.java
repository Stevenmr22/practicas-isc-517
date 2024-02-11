package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.example.encapsulation.Usuario;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<String, Usuario> usuarios = new HashMap<>();

    public static void main(String[] args) {

        usuarios.put("admin", new Usuario("admin", "admin"));

    Javalin app = Javalin.create(config ->{
        //configurando los documentos estaticos.
        config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.hostedPath = "/";
            staticFileConfig.directory = "/public";
            staticFileConfig.location = Location.CLASSPATH;
            staticFileConfig.precompress=false;
            staticFileConfig.aliasCheck=null;
        });
    }).start(7000);

    app.before("/", ctx -> {
        if (ctx.cookie("usuario") == null){
            ctx.redirect("/login");
        }
    });

    app.get("/" , ctx -> {
        ctx.render("/public/index.html");
    });

    app.get("/login", ctx -> {
        ctx.render("/public/login.html");
    });

    app.post("/login", ctx -> {
        String usuario = ctx.formParam("usuario");
        String contrasena = ctx.formParam("contrasena");
        Usuario user = new Usuario("admin", "admin");
        if(user.autenticar(usuario, contrasena)){
            ctx.cookie("usuario", usuario, 120);
            ctx.redirect("/");
        }else{
            ctx.redirect("/login");
        }
    });

    app.after("/", ctx -> {
        ctx.removeCookie("usuario");
    });
    }

}

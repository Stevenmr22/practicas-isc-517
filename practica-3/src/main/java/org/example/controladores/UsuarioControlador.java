package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.ServiciosUsuario;

import java.util.HashMap;
import java.util.Map;

import static org.example.utils.Utilitario.modeloBase;
import static org.example.utils.Utilitario.parseCheckbox;

public class UsuarioControlador extends BaseControlador{

    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();
    public UsuarioControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.get("/login", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("loginError", Boolean.parseBoolean(ctx.queryParam("loginError")));
            ctx.render("/vistas/login.html", model);
        });

        app.before("/verificacion", ctx -> {
            Usuario user = serviciosUsuario.validarUsuario(ctx.formParam("username"), ctx.formParam("password"));
            if(user == null)
                ctx.redirect("/login?loginError=true");
        });
        app.post("/verificacion", ctx -> {
            Usuario user = serviciosUsuario.validarUsuario(ctx.formParam("username"), ctx.formParam("password"));
            ctx.sessionAttribute("username", user.getUsername());
            ctx.sessionAttribute("password", user.getPassword());
            ctx.sessionAttribute("admin", user.isAdministrator());
            ctx.sessionAttribute("autor", user.isAutor());
            ctx.redirect("/");
        });

        app.get("cerrar-sesion", ctx -> {
            ctx.req().getSession().invalidate();
            ctx.redirect("/");
        });

        app.before("/crear-usuario", ctx ->{
            boolean admin = Boolean.parseBoolean(ctx.sessionAttribute("admin").toString());
            Usuario user = ServiciosUsuario.getInstancia().getUsuarioPorUsername(ctx.sessionAttribute("username"));
            if(!admin || user == null)
                ctx.redirect("/login");
        });
        app.get("crear-usuario", ctx ->{
            boolean errorEmpty = Boolean.parseBoolean(ctx.queryParam("errorEmpty"));
            boolean errorUsername = Boolean.parseBoolean(ctx.queryParam("errorUsername"));
            boolean completed = Boolean.parseBoolean(ctx.queryParam("completed"));

            Map<String, Object> model = modeloBase(ctx.sessionAttribute("username"), serviciosUsuario);
            model.put("errorEmpty", errorEmpty);
            model.put("errorUsername", errorUsername);
            model.put("completed", completed);
            ctx.render("/vistas/crear-usuario.html", model);
        });

        app.before("/procesar-registro", ctx ->{
            boolean admin = Boolean.parseBoolean(ctx.sessionAttribute("admin").toString());
            Usuario user = ServiciosUsuario.getInstancia().getUsuarioPorUsername(ctx.sessionAttribute("username"));
            if(!admin || user == null)
                ctx.redirect("/login");

            if(ctx.formParam("username").isBlank() || ctx.formParam("name").isBlank() ||
                    ctx.formParam("password").isBlank())
                ctx.redirect("/crear-usuario?errorEmpty=true");

            if(serviciosUsuario.usernameExiste(ctx.formParam("username")))
                ctx.redirect("/crear-usuario?errorUsername=true");
        });
        app.post("/procesar-registro", ctx ->{
            Usuario usuario = new Usuario(ctx.formParam("username"), ctx.formParam("name"), ctx.formParam("password"),
                    parseCheckbox(ctx.formParam("admin")), parseCheckbox(ctx.formParam("autor")));
            serviciosUsuario.agregarUsuario(usuario);
            ctx.redirect("/crear-usuario?completed=true");
        });
    }
}

package org.example.controladores;

import dev.morphia.query.filters.Filters;
import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import org.example.entidades.Url;
import org.example.servicios.ServiciosUrl;
import org.example.servicios.ServiciosUsuario;
import org.example.utilitarios.BaseControlador;
import org.example.entidades.Usuario;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.after;
import static org.example.utilitarios.Utilitario.modeloBase;

public class UsuarioControlador extends BaseControlador {
    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();
    ServiciosUrl serviciosUrl = ServiciosUrl.getInstancia();
    public UsuarioControlador(Javalin App) {super(App); }

    @Override
    public void aplicarRutas() {
        app.get("/login", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            if(ctx.queryParam("loginError") != null)
                modelo.put("error", true);
            ctx.render("/vistas/login.html", modelo);
        });

        app.post("/verificacion", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            Usuario usuario = serviciosUsuario.autenticarUsuario(username, password);
            if (usuario != null && usuario.isActivo()) {
                usuario.setPassword(null);
                ctx.sessionAttribute("username", usuario.getUsername());
                ctx.sessionAttribute("usuario", usuario);
                ctx.redirect("/");
            } else {
                ctx.redirect("/login?loginError=true");
            }
        });

        app.get("/logout", ctx -> {
            ctx.req().getSession().invalidate();
            ctx.redirect("/");
        });

        app.get("/registro", ctx -> {
            ctx.render("/vistas/registro.html");
        });

        app.post("/registro", ctx -> {
            String username = ctx.formParam("username");
            String nombre = ctx.formParam("nombre");
            String password = ctx.formParam("password");
            if (serviciosUsuario.existeUsuario(username)) {
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("errorUsername", "El nombre de usuario ya existe.");
                ctx.render("/vistas/registro.html", modelo);
            } else {
                Usuario usuario = new Usuario(username, nombre, password, false);
                serviciosUsuario.crear(usuario);

                // Para asignar username a las urls creadas por el usuario antes de registrarse
                ArrayList<String> sessionUrls = ctx.sessionAttribute("urls");

                if(sessionUrls == null){
                    ctx.redirect("/login");
                    return;
                }

                serviciosUrl.find().filter(Filters.in("id", sessionUrls))
                        .forEach(url -> {
                            url.setUsernameCreador(usuario.getUsername());
                            serviciosUrl.merge(url);
                        });
                ctx.sessionAttribute("urls", null);

                ctx.redirect("/login");
            }
        });

        app.get("/listar-usuario", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario == null || !usuario.isAdmin())
                ctx.redirect("/");

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("usuario", usuario);
            modelo.put("usuarios", serviciosUsuario.find());
            ctx.render("/vistas/listar-usuario.html", modelo);
        });
        app.routes(() -> {
            path("/usuario/", () -> {
                before("{nombre}", ctx -> {
                    String nombre = (ctx.pathParam("nombre"));
                    if (serviciosUsuario.getUsuarioPorUsername(nombre) == null)
                        ctx.redirect("/");
                });

                get("{nombre}", ctx -> {
                    Map<String, Object> model = modeloBase(ctx.sessionAttribute("username"), serviciosUsuario);
                    String nombre = (ctx.pathParam("nombre"));
                    Usuario usuario = serviciosUsuario.getUsuarioPorUsername(nombre);
                    model.put("usuario", usuario);
                    model.put("admin", usuario.isAdmin());
                    model.put("activo", usuario.isActivo());
                    ctx.render("/vistas/ver-usuario.html", model);
                });
                app.post("/hacer-admin", ctx -> {
                    Usuario usuarioActual = ctx.sessionAttribute("usuario");
                    if (usuarioActual != null && usuarioActual.getUsername().equals("admin")) {
                        String username = ctx.formParam("username");
                        Usuario usuario = serviciosUsuario.getUsuarioPorUsername(username);
                        if (usuario != null) {
                            usuario.setAdmin(true);
                            serviciosUsuario.crear(usuario);
                            ctx.redirect("/listar-usuario");
                        }
                    }
                });

                app.post("/quitar-admin", ctx -> {
                    Usuario usuarioActual = ctx.sessionAttribute("usuario");
                    if (usuarioActual != null && usuarioActual.getUsername().equals("admin")) {
                        String username = ctx.formParam("username");
                        Usuario usuario = serviciosUsuario.getUsuarioPorUsername(username);
                        if (usuario != null) {
                            usuario.setAdmin(false);
                            serviciosUsuario.crear(usuario);
                            ctx.redirect("/listar-usuario");
                        }
                    }
                });
            });
        });
    }
}
package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Articulo;
import org.example.encapsulaciones.Comentario;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.ServiciosArticulo;
import org.example.servicios.ServiciosEtiqueta;
import org.example.servicios.ServiciosUsuario;

import java.util.Date;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.example.servicios.ServiciosEtiqueta.separarPorComas;
import static org.example.utils.Utilitario.*;

public class ArticuloControlador extends BaseControlador {

    ServiciosArticulo serviciosArticulo = ServiciosArticulo.getInstancia();
    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

    public ArticuloControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {
            path("/articulo/", () -> {

                before("{id}", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    if(serviciosArticulo.getArticuloPorId(id) == null)
                        ctx.redirect("/");
                });
                get("{id}", ctx -> {
                    Map<String, Object> model = modeloBase(ctx.sessionAttribute("username"), serviciosUsuario);
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Articulo articulo = serviciosArticulo.getArticuloPorId(id);
                    model.put("articulo", articulo);
                    model.put("fecha", serviciosArticulo.getFechaFormateada(articulo));
                    model.put("admin", falseIfNull(ctx.sessionAttribute("admin")));
                    ctx.render("/vistas/ver-articulo.html", model);
                });

                before("{id}/agregar-comentario", ctx -> {
                    if(ServiciosUsuario.getInstancia().getUsuarioPorUsername(ctx.sessionAttribute("username")) == null)
                        ctx.redirect("/login");
                });
                post("{id}/agregar-comentario", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Articulo articulo = serviciosArticulo.getArticuloPorId(id);
                    Comentario comentario = new Comentario(
                            articulo.getListaComentarios().size(),
                            ctx.formParam("comentarioInput"),
                            serviciosUsuario.getUsuarioPorUsername(ctx.sessionAttribute("username")),
                            articulo);
                    articulo.agregarComentario(comentario);
                    ctx.redirect("/articulo/"+id);
                });

                before("{id}/eliminar", ctx -> {
                    boolean admin = Boolean.parseBoolean(ctx.sessionAttribute("admin").toString());
                    Articulo articulo = serviciosArticulo.getArticuloPorId(Long.parseLong(ctx.pathParam("id")));
                    if(!admin && ctx.sessionAttribute("username") != articulo.getAutor().getUsername())
                        ctx.redirect("/");
                });
                delete("{id}/eliminar", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    serviciosArticulo.eliminarArticulo(id);
                });
                after("{id}/eliminar", ctx -> {
                    ctx.redirect("/");
                });

                before("{id}/editar", ctx -> {
                    boolean admin = Boolean.parseBoolean(ctx.sessionAttribute("admin").toString());
                    Articulo articulo = serviciosArticulo.getArticuloPorId(Long.parseLong(ctx.pathParam("id")));
                    if(!admin && ctx.sessionAttribute("username") != articulo.getAutor().getUsername())
                        ctx.redirect("/");
                });
                get("{id}/editar", ctx -> {
                    Map<String, Object> model = modeloBase(ctx.sessionAttribute("username"), serviciosUsuario);
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Articulo articulo = serviciosArticulo.getArticuloPorId(id);
                    model.put("articulo", articulo);
                    model.put("etiquetas", ServiciosEtiqueta.unirPorComas(articulo.getEtiquetas()));
                    model.put("arrayEtiquetas", ServiciosEtiqueta.getInstancia().getEtiquetas());
                    ctx.render("/vistas/editar-articulo.html", model);
                });

                before("{id}/editar-registro", ctx -> {
                    boolean admin = Boolean.parseBoolean(ctx.sessionAttribute("admin").toString());
                    Articulo articulo = serviciosArticulo.getArticuloPorId(Long.parseLong(ctx.pathParam("id")));
                    if(!admin && ctx.sessionAttribute("username") != articulo.getAutor().getUsername())
                        ctx.redirect("/");
                });
                put("{id}/editar-registro", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Articulo articulo = serviciosArticulo.getArticuloPorId(id);
                    articulo.setTitulo(ctx.formParam("titulo"));
                    articulo.setCuerpo(ctx.formParam("cuerpo"));
                    articulo.setEtiquetas(separarPorComas(ctx.formParam("etiquetas")));
                });
                after("{id}/editar-registro", ctx -> {
                    ctx.redirect("/articulo/"+ctx.pathParam("id"));
                });
            });

            before("/crear-articulo", ctx -> {
                Usuario user = ServiciosUsuario.getInstancia().getUsuarioPorUsername(ctx.sessionAttribute("username"));
                if(user == null || !user.isAutor())
                    ctx.redirect("/login");
            });
            app.get("/crear-articulo", ctx -> {
                Map<String, Object> model = modeloBase(ctx.sessionAttribute("username"), serviciosUsuario);
                model.put("errorEmpty", ctx.queryParam("errorEmpty"));
                model.put("arrayEtiquetas", ServiciosEtiqueta.getInstancia().getEtiquetas());
                ctx.render("/vistas/crear-articulo.html", model);
            });

            before("/publicar-articulo", ctx -> {
                Usuario user = ServiciosUsuario.getInstancia().getUsuarioPorUsername(ctx.sessionAttribute("username"));
                if(user == null || !user.isAutor())
                    ctx.redirect("/login");

                if(ctx.formParam("titulo").isBlank() || ctx.formParam("cuerpo").isBlank())
                    ctx.redirect("/crear-articulo?errorEmpty=true");
            });
            app.post("/publicar-articulo", ctx -> {
                Usuario autor = serviciosUsuario.getUsuarioPorUsername(ctx.sessionAttribute("username"));
                Articulo articulo = new Articulo(
                        serviciosArticulo.generarId(),
                        ctx.formParam("titulo"),
                        ctx.formParam("cuerpo"),
                        autor,
                        new Date(),
                        separarPorComas(ctx.formParam("etiquetas")));
                serviciosArticulo.agregarArticulo(articulo);
                ctx.redirect("/");
            });
        });

    }
}

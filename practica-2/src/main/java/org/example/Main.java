package org.example;

import controladores.ArticuloController;
import controladores.EtiquetaController;
import controladores.LoginController;
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.encapsulaciones.Articulo;
import org.example.encapsulaciones.Comentario;
import org.example.encapsulaciones.Etiqueta;
import org.example.encapsulaciones.Usuario;
import io.javalin.http.staticfiles.Location;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public Main() {
        JavalinRenderer.register(new JavalinThymeleaf(), ".html");
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;
            });

        }).start(7000);
        app.get("/login", ctx -> {ctx.render("/thymeleaf/login.html");});
        app.get("/creararticulo", ctx -> {
            EtiquetaController etiquetaController = EtiquetaController.getInstancia();
            List<Etiqueta> listaEtiquetas = etiquetaController.getListaEtiquetas();
            ctx.attribute("listaEtiquetas", listaEtiquetas);
            ctx.attribute("etiquetas", listaEtiquetas);
            ctx.render("/thymeleaf/creararticulo.html");
        });


        app.post("/creararticulo", ctx -> {
            String titulo = ctx.formParam("titulo");
            String cuerpo = ctx.formParam("cuerpo");
            String[] etiquetas = ctx.formParams("etiquetas").toArray(new String[0]);
            // Obtener el usuario de la sesión
            Usuario autor = ctx.sessionAttribute("usuario");

            if (autor != null) {
                Articulo articulo = new Articulo(titulo, cuerpo, autor, new Date());
                articulo.setEtiquetas(EtiquetaController.getInstancia().agregarEtiqueta(etiquetas));

                ArticuloController articuloController = ArticuloController.getInstancia();
                articuloController.crearArticulo(articulo, autor);
                ctx.redirect("/index");
            } else {
                ctx.redirect("/login");
            }
        });

        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            LoginController loginController = LoginController.getInstancia();
            Usuario usuario = loginController.autheticarUsuario(username, password);

            if (usuario != null) {
                ctx.sessionAttribute("usuario", usuario); // Aquí se establece el objeto Usuario completo en la sesión
                ctx.redirect("/index");
            } else {
                ctx.redirect("/login");
            }
        });

        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/logout", ctx -> {
            ctx.req().getSession().invalidate();
            ctx.redirect("/index");
        });

        app.get("/index", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            // Verifica si el usuario está en la sesión
            if (usuario != null) {
                ctx.attribute("usuario", usuario);

                // Obtener la etiqueta seleccionada, si hay una
                String etiquetaSeleccionada = ctx.queryParam("etiqueta");
                ctx.attribute("etiquetaSeleccionada", etiquetaSeleccionada);

                // Obtener la lista de artículos
                ArticuloController articuloController = ArticuloController.getInstancia();
                List<Articulo> listaArticulos;

                if (etiquetaSeleccionada != null && !etiquetaSeleccionada.isEmpty()) {
                    // Si hay una etiqueta seleccionada, filtrar los artículos por esa etiqueta
                    listaArticulos = articuloController.getArticulosPorEtiqueta(etiquetaSeleccionada);
                } else {
                    // Si no hay etiqueta seleccionada, obtener todos los artículos
                    listaArticulos = articuloController.getListaArticulos();
                }

                ctx.attribute("articulos", listaArticulos);

            }
            EtiquetaController etiquetaController = EtiquetaController.getInstancia();
            List<Etiqueta> listaEtiquetas = etiquetaController.getListaEtiquetas();
            ctx.attribute("etiquetas", listaEtiquetas);
            ctx.render("/thymeleaf/index.html");
        });

        app.get("/articulo/{id}", ctx -> {
            String articuloId = ctx.pathParam("id");
            ArticuloController articuloController = ArticuloController.getInstancia();
            Articulo articulo = articuloController.buscarArticuloPorId(UUID.fromString(articuloId));
            if (articulo != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("articulo", articulo);
                ctx.render("/thymeleaf/articulo.html", model);
            } else {
                ctx.html("Artículo no encontrado");
            }
        });
        app.post("/articulo/comentario", ctx -> {
            String articuloId = ctx.formParam("articuloId");
            String contenido = ctx.formParam("contenido");
            ArticuloController articuloController = ArticuloController.getInstancia();
            Articulo articulo = articuloController.buscarArticuloPorId(UUID.fromString(articuloId));
            if (articulo != null) {
                // Obtén el usuario directamente de la sesión
                Usuario autor = ctx.sessionAttribute("usuario");
                if (autor != null) {
                    Comentario comentario = new Comentario(UUID.randomUUID(), contenido, autor, articulo);
                    articulo.agregarComentario(comentario);
                    ctx.redirect("/articulo/" + articuloId);
                } else {
                    ctx.redirect("/login"); // Redirige a la página de inicio de sesión si no hay usuario en la sesión
                }
            } else {
                ctx.html("Artículo no encontrado");
            }
        });

        app.get("/crearusuario", ctx -> {
            ctx.render("/thymeleaf/crearUsuario.html");
        });
        app.post("/crearusuario", ctx -> {
            // Obtén el nombre de usuario del formulario
            String username = ctx.formParam("username");

            // Verifica si ya existe un usuario con el mismo nombre de usuario
            LoginController loginController = LoginController.getInstancia();
            List<Usuario> listaUsuarios = loginController.getListaUsuarios();
            boolean usuarioExistente = listaUsuarios.stream().anyMatch(usuario -> usuario.getUsername().equals(username));

            if (usuarioExistente) {
                // Si ya existe un usuario con el mismo nombre de usuario, muestra un mensaje de error
                ctx.redirect("/crearusuario");
            } else {
                // Si no existe un usuario con el mismo nombre de usuario, procede a crear el nuevo usuario

                // Obtén los demás parámetros del formulario
                String nombre = ctx.formParam("nombre");
                String password = ctx.formParam("password");
                boolean admin = ctx.formParam("administrator") != null;
                boolean autor = ctx.formParam("autor") != null;

                // Crea un nuevo objeto Usuario con los parámetros recibidos
                Usuario nuevoUsuario = new Usuario(username, nombre, password, admin, autor);

                // Agrega el nuevo usuario a la lista de usuarios
                listaUsuarios.add(nuevoUsuario);

                // Redirige a la página de inicio de sesión
                ctx.redirect("/index");
            }
        });


        app.delete("/articulo/{id}", ctx -> {
            String articuloId = ctx.pathParam("id");

            // Obtén el usuario directamente de la sesión
            Usuario usuario = ctx.sessionAttribute("usuario");

            ArticuloController articuloController = ArticuloController.getInstancia();
            Articulo articulo = articuloController.buscarArticuloPorId(UUID.fromString(articuloId));

            if (usuario != null && (usuario.isAdministrator() || articulo.getAutor().getUsername().equals(usuario.getUsername()))) {
                articuloController.eliminarArticulo(UUID.fromString(articuloId));
                ctx.status(204);
            } else {
                ctx.status(403);
            }
        });


        app.get("/articulo/comentario/{id}", ctx -> {
            String articuloId = ctx.pathParam("id");
            ArticuloController articuloController = ArticuloController.getInstancia();
            Articulo articulo = articuloController.buscarArticuloPorId(UUID.fromString(articuloId));
            if (articulo != null) {
                ctx.attribute("comentarios", articulo.getListaComentarios());
                ctx.render("/thymeleaf/comentarios.html");
            } else {
                ctx.html("Artículo no encontrado");
            }
        });

        app.delete("/articulo/{articuloId}/comentario/{comentarioId}", ctx -> {
            String comentarioId = ctx.pathParam("comentarioId");
            String articuloId = ctx.pathParam("articuloId");

            // Obtén el usuario directamente de la sesión
            Usuario usuario = ctx.sessionAttribute("usuario");

            ArticuloController articuloController = ArticuloController.getInstancia();
            Comentario comentario = articuloController.buscarComentarioPorId(UUID.fromString(comentarioId));

            if (usuario != null && (usuario.isAdministrator() || comentario.getAutor().getUsername().equals(usuario.getUsername()))) {
                articuloController.eliminarComentario(UUID.fromString(articuloId), UUID.fromString(comentarioId));
                ctx.status(204);
            } else {
                ctx.status(403);
            }
        });

        app.get("/listarusuarios", ctx -> {
            // Obtener la lista de usuarios
            List<Usuario> listaUsuarios = LoginController.getInstancia().getListaUsuarios();

            // Agregar la lista de usuarios al contexto de renderizado
            ctx.attribute("usuarios", listaUsuarios);

            // Renderizar la página HTML para mostrar la lista de usuarios
            ctx.render("/thymeleaf/listarUsuarios.html");
        });

        app.get("/editararticulo/{id}", ctx -> {
            String articuloId = ctx.pathParam("id");
            ArticuloController articuloController = ArticuloController.getInstancia();
            Articulo articulo = articuloController.buscarArticuloPorId(UUID.fromString(articuloId));
            EtiquetaController etiquetaController = EtiquetaController.getInstancia();
            List<Etiqueta> listaEtiquetas = etiquetaController.getListaEtiquetas();
            ctx.attribute("listaEtiquetas", listaEtiquetas);
            List<Etiqueta> etiquetasSeleccionadas = articulo.getEtiquetas();
            Set<Etiqueta> setExistentes = new HashSet<>(listaEtiquetas);
            Set<Etiqueta> setElegidas = new HashSet<>(etiquetasSeleccionadas);
            Set<Etiqueta> diferencia = new HashSet<>(setExistentes);
            diferencia.removeAll(setElegidas);
            ctx.attribute("etiquetas", diferencia);

            if (articulo != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("articulo", articulo);
                ctx.render("/thymeleaf/editararticulo.html", model);
            } else {
                ctx.html("Artículo no encontrado");
            }
        });

        app.put("/editararticulo/{id}", ctx -> {
            String articuloId = ctx.pathParam("id");
            String titulo = ctx.formParam("titulo");
            String cuerpo = ctx.formParam("cuerpo");
            String[] etiquetas = ctx.formParams("etiquetas").toArray(new String[0]);
            ArticuloController articuloController = ArticuloController.getInstancia();
            Articulo articulo = articuloController.buscarArticuloPorId(UUID.fromString(articuloId));
            if (articulo != null) {
                articulo.setTitulo(titulo);
                articulo.setCuerpo(cuerpo);
                articulo.setEtiquetas(EtiquetaController.getInstancia().agregarEtiqueta(etiquetas));
                ctx.redirect("/articulo/" + articuloId);
            } else {
                ctx.html("Artículo no encontrado");
            }
        });

    }
}
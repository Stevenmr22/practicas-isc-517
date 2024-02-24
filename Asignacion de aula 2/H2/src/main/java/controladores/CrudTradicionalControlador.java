package controladores;

import encapsulaciones.Estudiante;
import servicios.FakeServices;
import util.BaseControlador;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Representa las rutas para manejar las operaciones de petición - respuesta.
 */
public class CrudTradicionalControlador extends BaseControlador {

    FakeServices fakeServices = FakeServices.getInstancia();

    public CrudTradicionalControlador(Javalin app) {
        super(app);
    }

    /**
     * Las clases que implementan el sistema de plantilla están agregadas en PlantillasControlador.
     * http://localhost:7000/crud-simple/listar
     */
    @Override
    public void aplicarRutas() {
        app.routes(()->{

            /**
             * Ejemplo de como agrupar los endpoint utilizados.
             */
            path("/path/", () -> {
                before(ctx -> {
                    System.out.println("Entrando a la ruta path...");
                });
                get("/", ctx -> {
                    ctx.result("Ruta path /");
                });

                get("/compras", ctx -> {
                    ctx.result("Ruta /path/compras");
                });

                get("/otro", ctx -> {
                    ctx.result("Ruta /path/otro");
                });
            });
        });
        app.routes(() -> {
            path("/crud-simple/", () -> {


                get("/", ctx -> {
                    ctx.redirect("/crud-simple/listar");
                });

                get("/listar", ctx -> {
                    List<Estudiante> lista = fakeServices.listarEstudiante();
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Listado de Estudiante");
                    modelo.put("lista", lista);
                    ctx.render("/templates/crud-tradicional/listar.html", modelo);
                });

                get("/crear", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Creación Estudiante");
                    modelo.put("accion", "/crud-simple/crear");
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                post("/crear", ctx -> {
                    int matricula = ctx.formParamAsClass("matricula", Integer.class).get();
                    String nombre = ctx.formParam("nombre");
                    String carrera = ctx.formParam("carrera");
                    //
                    Estudiante tmp = new Estudiante(matricula, nombre, carrera);
                    fakeServices.insertarEstudiante(tmp);
                    ctx.redirect("/crud-simple/");
                });

                get("/visualizar/{matricula}", ctx -> {
                    Estudiante estudiante = fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get());
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Visaulizar Estudiante "+estudiante.getMatricula());
                    modelo.put("estudiante", estudiante);
                    modelo.put("visualizar", true);
                    modelo.put("accion", "/crud-simple/");
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                get("/editar/{matricula}", ctx -> {
                    Estudiante estudiante = fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get());
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Editar Estudiante "+estudiante.getMatricula());
                    modelo.put("estudiante", estudiante);
                    modelo.put("accion", "/crud-simple/editar");
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                post("/editar", ctx -> {
                    int matricula = ctx.formParamAsClass("matricula", Integer.class).get();
                    String nombre = ctx.formParam("nombre");
                    String carrera = ctx.formParam("carrera");
                    Estudiante tmp = new Estudiante(matricula, nombre, carrera);
                    fakeServices.actualizarEstudiante(tmp);
                    ctx.redirect("/crud-simple/");
                });

                get("/eliminar/{matricula}", ctx -> {
                    fakeServices.borrarEstudiante(ctx.pathParamAsClass("matricula", Integer.class).get());
                    ctx.redirect("/crud-simple/");
                });

            });
        });
    }
}

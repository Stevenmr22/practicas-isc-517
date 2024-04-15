package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.example.controladores.AcortadorControlador;
import org.example.controladores.ApiControlador;
import org.example.controladores.UsuarioControlador;
import org.example.entidades.Usuario;
import org.example.grpc.UrlServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        Javalin app = Javalin.create(config -> {
                    config.staticFiles.add(staticFiles -> {
                        staticFiles.hostedPath = "/";
                        staticFiles.directory = "/publico";
                        staticFiles.location = Location.CLASSPATH;
                    });
                }
        ).start(8000);

        app.get("/", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            Map<String, Object> modelo = new HashMap<>();

            ctx.cookie("cash", "0");

            modelo.put("usuario", usuario);
            if(ctx.queryParam("error") != null)
                modelo.put("error", true);

            ctx.render("/vistas/index.html", modelo);
        });

        new UsuarioControlador(app).aplicarRutas();
        new AcortadorControlador(app).aplicarRutas();
        new ApiControlador(app).aplicarRutas();

        int port = 50051;

        //Inicializando el servidor
        Server server = ServerBuilder.forPort(port)
                .addService(new UrlServiceImpl())// indicando el servicio registrado.
                .build()
                .start();
        System.out.println("Servidor gRPC iniciando y escuchando en " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Cerrando servidor por la JVM ");
            if (server != null) {
                server.shutdown();
            }
            System.err.println("Servidor abajo!...");
        }));
        server.awaitTermination();
    }
}
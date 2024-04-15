package org.example.controladores;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.jsonwebtoken.Claims;
import org.example.encapsulaciones.Formulario;
import org.example.encapsulaciones.NivelEscolar;
import org.example.servicios.ServiciosFormulario;
import org.example.servicios.ServiciosUsuario;
import org.example.utilitarios.BaseControlador;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.example.controladores.UsuarioControlador.obtenerClaims;
import static org.example.utilitarios.Utilitario.modeloBase;

public class FormularioControlador extends BaseControlador {
    public FormularioControlador(Javalin app) {
        super(app);
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void aplicarRutas() {
        app.get("/capturar-respuesta", ctx -> {
            Claims claims = obtenerClaims(ctx);

            if(claims == null){
                ctx.redirect("/login");
                return;
            }

            Map<String, Object> model = modeloBase(claims);
            model.put("niveles", NivelEscolar.values());
            ctx.render("/vistas/registro-formulario.html", model);
        });

        app.get("/listar-formulario", ctx -> {
            Claims claims = obtenerClaims(ctx);

            if(claims == null){
                ctx.redirect("/login");
                return;
            }

            Map<String, Object> model = modeloBase(claims);
            List<Formulario> formularios = ServiciosFormulario.getInstancia().findAll();
            model.put("formularios", formularios);
            ctx.render("/vistas/listar-formulario.html", model);
        });

        app.ws("/ws", ws-> {
            // Si llega un mensaje al websocket, se van guardando los objetos
            ws.onMessage(ctx -> {
                String json = ctx.message();
                JsonNode jsonObject = objectMapper.readTree(json);

                NivelEscolar nivelEscolar = Arrays.stream(NivelEscolar.values())
                        .filter(nivel -> nivel.toString().equals(jsonObject.get("nivel_escolar").asText()))
                        .findFirst()
                        .orElse(null);

                Formulario formulario = new Formulario(
                        jsonObject.get("nombre").asText(),
                        jsonObject.get("sector").asText(),
                        nivelEscolar,
                        ServiciosUsuario.getInstancia().find(jsonObject.get("username").asText()),
                        jsonObject.get("latitud").asDouble(),
                        jsonObject.get("longitud").asDouble()
                );
                ServiciosFormulario.getInstancia().crear(formulario);
            });
        });
    }
}

package org.example.controladores;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.DeleteOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.filters.Filters;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.entidades.Acceso;
import org.example.entidades.Url;
import org.example.entidades.Usuario;
import org.example.servicios.ServiciosAcceso;
import org.example.servicios.ServiciosUrl;
import org.example.utilitarios.BaseControlador;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.example.utilitarios.Utilitario.generateShortId;
import static org.example.utilitarios.Utilitario.urlIsValid;

public class AcortadorControlador extends BaseControlador {

    private final ServiciosUrl serviciosUrl = ServiciosUrl.getInstancia();
    private final ServiciosAcceso serviciosAcceso = ServiciosAcceso.getInstancia();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AcortadorControlador(Javalin app) { super(app); }

    @Override
    public void aplicarRutas() {

        app.before("/acortar", ctx ->{
            if(!urlIsValid(ctx.formParam("url"))){
                ctx.redirect("/?error=true");
            }
        });

        app.post("/acortar", ctx -> {
            String id = generateShortId();
            while(serviciosUrl.find().filter(Filters.eq("id", id)).first() != null) {
                id = generateShortId();
            }

            Usuario usuario = ctx.sessionAttribute("usuario");
            serviciosUrl.crear(new Url(ctx.formParam("url"), id, usuario != null ? usuario.getUsername() : "Invitado", ctx.formParam("titulo")));
            setUrlToSessionIfUserNull(ctx, usuario, id);

            ctx.redirect("/enlaces");
        });

        app.get("/enlaces", ctx -> {
            Map<String, Object> model = new HashMap<>();
            Usuario usuario = ctx.sessionAttribute("usuario");

            ctx.cookie("cash", "0");

            model.put("usuario", usuario);
            model.put("urls", getUrls(ctx, usuario));
            ctx.render("/vistas/listar-urls.html", model);
        });

        app.get("/gestion-urls", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario == null || !usuario.isAdmin()) {
                ctx.redirect("/");
                return;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("urls", serviciosUrl.find().iterator(new FindOptions().sort(Sort.descending("fechaCreacion"))).toList());
            model.put("isAdmin", usuario.isAdmin());
            model.put("usuario", usuario);

            if(Objects.equals(ctx.queryParam("deleted"), "true"))
                model.put("deleted", true);
            else if(Objects.equals(ctx.queryParam("deleted"), "false"))
                model.put("deleted", false);

            ctx.render("/vistas/listar-urls.html", model);
        });

        app.delete("/eliminar-url/{id}", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario == null || !usuario.isAdmin()) {
                ctx.redirect("/");
                return;
            }

            // Borrando accesos a la URL y la URL en sí.
            serviciosAcceso.find()
                    .filter(Filters.eq("idUrlAcortada", ctx.pathParam("id")))
                    .delete(new DeleteOptions().multi(true));
            serviciosUrl.find()
                    .filter(Filters.eq("id", ctx.pathParam("id")))
                    .delete();
        });

        app.get("/eliminar-url/{id}", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario == null || !usuario.isAdmin()) {
                ctx.redirect("/");
                return;
            }

            Url url = serviciosUrl.find().filter(Filters.eq("id", ctx.pathParam("id"))).first();
            ctx.redirect("/gestion-urls?deleted=" + (url == null));
        });

        app.get("/r/{id}", ctx -> {
            Url url = serviciosUrl.find().filter(Filters.eq("id", ctx.pathParam("id"))).first();
            if(url == null){
                ctx.status(404);
                ctx.render("/vendors/error-404.html");
                return;
            }

            // Capturar información de acceso.
            Parser userAgentParser = new Parser();
            Client c = userAgentParser.parse(ctx.userAgent());
            String dominioCliente = new URI(url.getUrlOriginal()).getHost();
            serviciosAcceso.crear(new Acceso(c.userAgent.family, ctx.ip(), c.os.family, dominioCliente, url.getId()));
            ctx.redirect(url.getUrlOriginal());
        });

        app.get("/ver-url/{id}", ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario == null) {
                ctx.redirect("/");
                return;
            }
            Url url = serviciosUrl.find().filter(Filters.eq("id", ctx.pathParam("id"))).first();
            if(url == null){
                ctx.status(404);
                ctx.render("/vendors/error-404.html");
                return;
            }

            List<Acceso> accesos = serviciosAcceso.find().filter(Filters.eq("idUrlAcortada", url.getId())).stream().toList();

            Map<LocalDateTime, Long> accessCounts = accesos.stream()
                    .collect(Collectors.groupingBy(
                            acceso -> acceso.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().truncatedTo(ChronoUnit.HOURS),
                            Collectors.counting()
                    ));

            Map<String, Long> sistemaOperativo = accesos.stream()
                    .collect(Collectors.groupingBy(Acceso::getSistemaOperativo, Collectors.counting()));

            Map<String, Long> browser = accesos.stream()
                    .collect(Collectors.groupingBy(Acceso::getNavegador, Collectors.counting()));

            List<String> accessHours = new ArrayList<>();
            List<Long> accessCountsList = new ArrayList<>();
            for (Map.Entry<LocalDateTime, Long> entry : accessCounts.entrySet()) {
                accessHours.add(entry.getKey().toString());
                accessCountsList.add(entry.getValue());
            }

            Map<String, Object> model = new HashMap<>();
            model.put("url", url);
            model.put("accesos", accesos);
            model.put("accessHours", accessHours);
            model.put("accessCounts", accessCountsList);
            model.put("usuario", usuario);
            model.put("sistemaOperativo", sistemaOperativo);
            model.put("browser", browser);
            ctx.render("/vistas/ver-url.html", model);
        });

        app.post("/api-previewer", ctx -> {
            try {
                String url = (String) objectMapper.readValue(ctx.body(), new TypeReference<Map<String, Object>>() {}).get("q");
                ctx.result(urlPreview(url));
            } catch (Exception e){
                ctx.result("{\"error\": \"Error al obtener la vista previa.\"}");
            }
        });
    }

    private void setUrlToSessionIfUserNull(Context ctx, Usuario usuario, String id){
        if(ctx.sessionAttribute("urls") == null && usuario == null)
            ctx.sessionAttribute("urls", new ArrayList<String>());

        if(usuario == null) {
            ArrayList<String> sessionUrls = ctx.sessionAttribute("urls");
            sessionUrls.add(id);
        }
    }

    private List<Url> getUrls(Context ctx, Usuario usuario){
        ArrayList<String> sessionUrls = ctx.sessionAttribute("urls");

        if(usuario == null && sessionUrls != null)
            return serviciosUrl.find()
                    .filter(Filters.in("id", sessionUrls))
                    .iterator(new FindOptions().sort(Sort.descending("fechaCreacion")))
                    .toList();

        else if(usuario != null)
            return serviciosUrl.find()
                    .filter(Filters.eq("usernameCreador", usuario.getUsername()))
                    .iterator(new FindOptions().sort(Sort.descending("fechaCreacion")))
                    .toList();

        return new ArrayList<>();
    }

    public static String urlPreview(String url){
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI("https://api.linkpreview.net").toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-Linkpreview-Api-Key", "4c33d8416a240690ecba0af1be2680c6");
            conn.setDoOutput(true);

            try(OutputStream os = conn.getOutputStream()) {
                os.write(("{\"q\": \"" + url + "\"}").getBytes(StandardCharsets.UTF_8));
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining());
            } catch (Exception e) {
                return "{\"error\": \"Error al obtener la vista previa.\"}";
            }
        } catch (Exception e) {
            return "{\"error\": \"Error al obtener la vista previa.\"}";
        }
    }


}

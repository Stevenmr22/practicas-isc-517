package org.example.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.query.filters.Filters;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.dtos.LoginResponse;
import org.example.dtos.UrlDto;
import org.example.dtos.UsuarioDto;
import org.example.entidades.Acceso;
import org.example.entidades.Url;
import org.example.entidades.Usuario;
import org.example.servicios.ServiciosAcceso;
import org.example.servicios.ServiciosUrl;
import org.example.servicios.ServiciosUsuario;
import org.example.utilitarios.BaseControlador;
import org.example.utilitarios.Utilitario;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.example.utilitarios.DatosEstaticos.LLAVE_SECRETA;
import static org.example.utilitarios.Utilitario.*;

public class ApiControlador extends BaseControlador {

    private final ServiciosUrl serviciosUrl = ServiciosUrl.getInstancia();
    private final ServiciosAcceso serviciosAcceso = ServiciosAcceso.getInstancia();
    private final ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {
            path("/api", () -> {

                post("/generarApiKey", ctx -> {
                    Usuario usuario;
                    try {
                        UsuarioDto usuarioDto = objectMapper.readValue(ctx.body(), UsuarioDto.class);
                        usuario = serviciosUsuario.autenticarUsuario(usuarioDto.username(), usuarioDto.password());
                    }catch(Exception ex){
                        ctx.status(400).result("Request mal formada");
                        ctx.contentType("application/json");
                        return;
                    }

                    if(usuario == null){
                        ctx.status(401).result("Username o password incorrectos");
                        ctx.contentType("application/json");
                        return;
                    }
                    
                    ctx.json(generarJWT(usuario.getUsername()));
                });

                post("/acortar", ctx -> {
                    String id = generateShortId();
                    while(serviciosUrl.find().filter(Filters.eq("id", id)).first() != null) {
                        id = generateShortId();
                    }

                    String username = obtenerUsername(ctx);
                    if(username.isBlank()) {
                        ctx.status(401).result("El token no es aceptable");
                        ctx.contentType("application/json");
                        return;
                    }

                    try {
                        UrlDto urlDto = objectMapper.readValue(ctx.body(), UrlDto.class);
                        if(!urlIsValid(urlDto.urlOriginal())){
                            ctx.status(400).result("URL no aceptada");
                            ctx.contentType("application/json");
                            return;
                        }
                        Url url = new Url(urlDto.urlOriginal(), id, username, urlDto.titulo());
                        serviciosUrl.crear(url);
                        ctx.json(responseCrearUrl(url));
                    } catch(Exception ex){
                        ctx.status(400).result("Request mal formada");
                        ctx.contentType("application/json");
                    }
                });

                path("/urls", () -> {
                    get("/usuarios/{username}", ctx -> {
                        if(obtenerUsername(ctx).isBlank()) {
                            ctx.status(401).result("El token no es aceptable");
                            ctx.contentType("application/json");
                        }

                        List<Url> urlsUsuario = serviciosUrl.getUrlByUsername(ctx.pathParam("username"));
                        List<String> idsUrls = urlsUsuario.stream().map(Url::getId).toList();
                        List<Acceso> accesos = serviciosAcceso.find().filter(Filters.in("idUrlAcortada", idsUrls)).stream().toList();

                        List<Acceso> estadisticas;
                        List<UrlDto> dtos = new ArrayList<>();
                        for (Url url : urlsUsuario) {
                            estadisticas = accesos.stream().filter(acceso -> acceso.getIdUrlAcortada().equals(url.getId())).toList();
                            dtos.add(new UrlDto(url.getId(), url.getUrlOriginal(), url.getTitulo(), url.getUsernameCreador(), url.getFechaCreacion(), estadisticas));
                        }
                        ctx.json(dtos);
                    });
                });

            });
        });
    }

    private static LoginResponse generarJWT(String username){
        SecretKey secretKey = Keys.hmacShaKeyFor(LLAVE_SECRETA.getValor().getBytes());

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(60);
        Date fechaExpiracion = Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-4)));

        String jwt = Jwts.builder()
                         .setIssuer("SKY-SHORTENER")
                         .setSubject("JWT")
                         .setExpiration(fechaExpiracion)
                         .claim("username", username)
                         .signWith(secretKey)
                         .compact();

        return new LoginResponse(jwt, fechaExpiracion.getTime());
    }

    private static String obtenerUsername(Context ctx){
        if(ctx.method() == HandlerType.OPTIONS){
            return "";
        }

        String header = "Authorization";
        String prefijo = "Bearer";

        String headerAuth = ctx.header(header);
        System.out.println(headerAuth);
        if(headerAuth == null || !headerAuth.startsWith(prefijo))
            return "";

        String tramaJwt = headerAuth.replace(prefijo,"").trim();
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(LLAVE_SECRETA.getValor().getBytes()))
                    .build()
                    .parseSignedClaims(tramaJwt)
                    .getPayload();

            return claims.get("username").toString();
        } catch(Exception ex){
            return "";
        }
    }

    private Map<String, Object> responseCrearUrl(Url url){
        String imageUrl = extractImageUrl(url.getUrlOriginal());
        String base64Image = urlImageToBase64(imageUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("urlOriginal", url.getUrlOriginal());
        response.put("urlAcortada", "sh.stevenapp.me/r/"+url.getId());
        response.put("fechaCreacion", url.getFechaCreacion());
        response.put("estadisticas", new ArrayList<>());
        response.put("base64Image", base64Image);
        return response;
    }
}

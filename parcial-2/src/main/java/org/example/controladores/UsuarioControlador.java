package org.example.controladores;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HandlerType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.ServiciosUsuario;
import org.example.utilitarios.BaseControlador;
import org.example.utilitarios.Utilitario;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.utilitarios.Utilitario.*;

public class UsuarioControlador extends BaseControlador {

    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();
    public final static String SECRET_KEY = "key_parcial2_secreta1234?caracteres_secretos";
    public UsuarioControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.get("/login", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            if(ctx.queryParam("loginError") != null)
                modelo.put("error", true);
            ctx.render("/vistas/login.html",modelo);
        });

        app.before("/verificacion", ctx -> {
            Usuario user = serviciosUsuario.validarUsuario(ctx.formParam("username"), ctx.formParam("password"));
            if(user == null)
                ctx.redirect("/login?loginError=true");
        });

        app.post("/verificacion", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            Usuario usuario = serviciosUsuario.validarUsuario(username, password);
            if(usuario == null) {
                ctx.redirect("/crearLocalStorage");
                return;
            }

            ctx.cookie("Authorization", generarJsonWebToken(usuario).token, 86400);
            ctx.redirect("/");
        });

        app.get("/crearLocalStorage", ctx -> {
            ctx.render("/vistas/crearLocalStorage.html");
        });

        app.get("/logout", ctx -> {
            ctx.removeCookie("Authorization");
            ctx.redirect("/");
        });

        app.before("/crear-usuario", ctx -> {
            Claims claims = obtenerClaims(ctx);
            if(claims == null || !Utilitario.falseIfNull((Boolean) claims.get("admin"))){
                ctx.redirect("/login");
            }
        });

        app.get("/crear-usuario", ctx -> {
            Claims claims = obtenerClaims(ctx);
            if(claims == null || !Utilitario.falseIfNull((Boolean) claims.get("admin"))){
                ctx.redirect("/login");
                return;
            }
            Map<String, Object> modelo = Utilitario.modeloBase(claims);
            if(ctx.queryParam("errorUsername") != null)
                modelo.put("error", true);
            ctx.render("/vistas/crear-usuario.html", modelo);
        });

        app.before("/procesar-registro", ctx ->{
            if(serviciosUsuario.usernameExiste(ctx.formParam("username")))
                ctx.redirect("/crear-usuario?errorUsername=true");
        });

        app.post("/procesar-registro", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            String nombre = ctx.formParam("nombre");
            String admin = ctx.formParam("admin");
            boolean isAdmin = parseCheckbox(admin);

            if (username == null || password == null || nombre == null) {
                ctx.redirect("/crear-usuario?error=true&message=Campos%20obligatorios%20vacíos");
                return;
            }

            if (serviciosUsuario.usernameExiste(username)) {
                ctx.redirect("/crear-usuario?error=true&message=Nombre%20de%20usuario%20ya%20existente");
                return;
            }

            Usuario nuevoUsuario = new Usuario(username, password, nombre, isAdmin);
            serviciosUsuario.agregarUsuario(nuevoUsuario);

            ctx.redirect("/crear-usuario?success=true&message=Usuario%20creado%20exitosamente");
        });

        app.before("/listar-usuario", ctx -> {
            Claims claims = obtenerClaims(ctx);
            if(claims == null || !Utilitario.falseIfNull((Boolean) claims.get("admin"))){
                ctx.redirect("/login");
            }
        });

        app.get("/listar-usuario", ctx -> {
            List<Usuario> usuarios = serviciosUsuario.obtenerUsuarios();
            Map<String, Object> modelo = Utilitario.modeloBase(obtenerClaims(ctx));
            modelo.put("usuarios", usuarios);
            ctx.render("/vistas/listar-usuario.html", modelo);
        });
    }

    public static Claims obtenerClaims(Context ctx){
        String tramaJwt = ctx.cookie("Authorization");
        if(tramaJwt == null)
            return null;

        try{
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(tramaJwt)
                    .getPayload();
        }catch (Exception e){
            return null;
        }
    }

    public boolean isJwtValid(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static LoginResponse generarJsonWebToken(Usuario usuario){
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1440);
        Date fechaExpiracion = Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-4)));

        String jwt = Jwts.builder()
                .setIssuer("OFICINA-PLANEAMIENTO")
                .setSubject("JWT")
                .setExpiration(fechaExpiracion)
                .claim("username", usuario.getUsername())
                .claim("nombre", usuario.getNombre())
                .claim("admin", usuario.isAdmin())
                .signWith(secretKey)
                .compact();

        return new LoginResponse(jwt, fechaExpiracion.getTime());
    }
    public record LoginResponse(String token,long expiresIn){}

}

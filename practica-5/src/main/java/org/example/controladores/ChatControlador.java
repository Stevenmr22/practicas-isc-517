package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Chat;
import org.example.encapsulaciones.Mensaje;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.ServiciosChat;
import org.example.servicios.ServiciosUsuario;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.example.utils.Utilitario.modeloBase;

public class ChatControlador extends BaseControlador {
    ServiciosChat serviciosChat = ServiciosChat.getInstancia();
    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();

    public ChatControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {
            path("/chat", () -> {
                get("/", ctx -> {
                    Usuario usuario = UsuarioControlador.autenticarConCookie(ctx);
                    if (usuario == null || (!usuario.isAdministrator() && !usuario.isAutor())) {
                        ctx.redirect("/");
                        return;
                    }
                    Map<String, Object> model = modeloBase(usuario != null ? usuario.getUsername() : null, ServiciosUsuario.getInstancia());
                    model.put("chats", serviciosChat.getChats());
                    ctx.render("/vistas/listar-chat.html", model);
                });

                get("/{id}", ctx -> {
                    Usuario usuario = UsuarioControlador.autenticarConCookie(ctx);
                    Map<String, Object> model = modeloBase(usuario != null ? usuario.getUsername() : null, serviciosUsuario);
                    Chat chat = serviciosChat.getChats().get(Integer.parseInt(ctx.pathParam("id")));
                    String chatId = ctx.cookie("chatId");
                    if(chat != null) {
                        if (!(usuario != null && (usuario.isAdministrator() || usuario.isAutor())) && !String.valueOf(chat.getId()).equals(chatId)) {
                            ctx.redirect("/error");
                            return;
                        }
                        model.put("mensajes", chat.getMensajes().stream().map(Mensaje::toString).collect(Collectors.joining("\n")));
                        String username = (usuario != null && (usuario.isAdministrator() || usuario.isAutor())) ? usuario.getUsername() : chat.getUsuario();
                        ctx.cookie("usuario", username.replaceAll("\\s", ""));
                        model.put("chatUser", (usuario != null && (usuario.isAdministrator() || usuario.isAutor())) ? chat.getUsuario() : "encargado");
                    }
                    ctx.render("/vistas/panel-chat.html", model);
                });
            });
        });

        app.post("/iniciarChat", ctx -> {
            int chatId = serviciosChat.getChats().size()+1;
            Chat chat = new Chat(chatId, ctx.formParam("usuario"), ". . .");
            serviciosChat.getChats().put(chatId, chat);
            ctx.cookie("chatId", String.valueOf(chatId), 300);
            ctx.redirect("/chat/"+chatId);
        });

        app.ws("/chat/{id}", ws -> {
            //Si un usuario se conecta a un chat, se crea el chat en caso de que no exista
            ws.onConnect(ctx -> {
                Chat chat = serviciosChat.getChats().get(Integer.parseInt(ctx.pathParam("id")));
                if(chat == null){
                    ctx.session.close();
                    return;
                }
                chat.getUsuariosConectados().add(ctx.session);
            });

            //Si un usuario envía un mensaje a un chat, se envía el mensaje a todos los usuarios conectados al chat
            ws.onMessage(ctx -> {
                Chat chat = serviciosChat.getChats().get(Integer.parseInt(ctx.pathParam("id")));
                Mensaje msg = new Mensaje(chat.getMensajes().size()+1,
                                            ctx.message(), ctx.cookie("usuario"),
                                            LocalDateTime.now());
                chat.setUltimoMensaje(ctx.message());
                chat.getMensajes().add(msg);
                chat.getUsuariosConectados().forEach(session -> {
                    try {
                        session.getRemote().sendString(msg.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });

            //Si un usuario se desconecta de un chat, se elimina de la lista de usuarios conectados
            ws.onClose(ctx -> {
                Chat chat = serviciosChat.getChats().get(Integer.parseInt(ctx.pathParam("id")));
                if(chat != null)
                    chat.getUsuariosConectados().remove(ctx.session);
            });
        });
    }
}

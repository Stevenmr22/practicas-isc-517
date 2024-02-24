package org.example;

import controladores.CrudTradicionalControlador;
import io.javalin.Javalin;
import servicios.BootStrapServices;

public class Main {
    public static void main(String[] args) {
        BootStrapServices.getInstancia().startDb();

        Javalin app = Javalin.create().start(7000);
        app.get("/", ctx -> ctx.redirect("/crud-simple/listar"));
        try {
            new CrudTradicionalControlador(app).aplicarRutas();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
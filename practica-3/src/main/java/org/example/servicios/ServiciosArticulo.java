package org.example.servicios;

import org.example.encapsulaciones.Articulo;
import org.example.encapsulaciones.Etiqueta;
import org.example.encapsulaciones.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ServiciosArticulo {

    private static ServiciosArticulo instancia;
    private ArrayList<Articulo> articulos = new ArrayList<Articulo>();
    private long contadorArticulos = 1;

    public static ServiciosArticulo getInstancia(){
        if(instancia==null){
            instancia = new ServiciosArticulo();
        }
        return instancia;
    }

    public void agregarArticulo(Articulo articulo) { articulos.add(articulo); }

    public long generarId() {
        return contadorArticulos++;
    }

    public ArrayList<Articulo> getArticulos() {
        return articulos;
    }

    public Articulo getArticuloPorId(long id){
        for (Articulo articulo : articulos) {
            if (articulo.getId() == id)
                return articulo;
        }
        return null;
    }

    public String getFechaFormateada(Articulo articulo){
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        return formato.format(articulo.getFecha());
    }

    public void eliminarArticulo(long id) {
        for (Articulo articulo : articulos) {
            if (articulo.getId() == id) {
                articulos.remove(articulo);
                break;
            }
        }
    }
}

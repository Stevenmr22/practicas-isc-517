package org.example.servicios;

import org.example.encapsulaciones.Articulo;
import org.example.encapsulaciones.Etiqueta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ServiciosEtiqueta {
    private static ServiciosEtiqueta instancia;
    private static ArrayList<Etiqueta> etiquetas = new ArrayList<Etiqueta>();

    public static ServiciosEtiqueta getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosEtiqueta();
        }
        return instancia;
    }

    private static long generarId(){
        return etiquetas.size() + 1;
    }

    public static void agregarEtiqueta(Etiqueta etiqueta) {
        etiquetas.add(etiqueta);
    }

    public ArrayList<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public static ArrayList<Etiqueta> separarPorComas(String etiquetas){
        Set<Etiqueta> etiquetasSeparadas = new HashSet<>();
        String[] etiquetasArray = etiquetas.split(",");
        for (String etiqueta : etiquetasArray) {
            Etiqueta e = new Etiqueta(generarId(), etiqueta.trim());
            if(!e.getEtiqueta().isBlank()) {
                e = etiquetaExistente(e);
                etiquetasSeparadas.add(e);
            }
        }
        return new ArrayList<>(etiquetasSeparadas);
    }

    public static String unirPorComas(ArrayList<Etiqueta> listaEtiquetas){
        StringBuilder etiquetas = new StringBuilder();
        for (Etiqueta etiqueta : listaEtiquetas) {
            etiquetas.append(etiqueta.getEtiqueta()).append(", ");
        }

        if(etiquetas.isEmpty()) return "";
        return etiquetas.substring(0, etiquetas.length() - 2);
    }

    private static Etiqueta etiquetaExistente(Etiqueta e){
        for (Etiqueta etiqueta : etiquetas) {
            if (etiqueta.getEtiqueta().equalsIgnoreCase(e.getEtiqueta()))
                return etiqueta; //Si la etiqueta ya existe, se retorna la etiqueta existente.
        }
        agregarEtiqueta(e); //Si no existe, se agrega a la lista de etiquetas y se retorna la etiqueta original.
        return e;
    }

    public static ArrayList<Articulo> obtenerArticulosPorEtiqueta(String etiqueta){
        ArrayList<Articulo> articulos = new ArrayList<>();
        for (Articulo articulo : ServiciosArticulo.getInstancia().getArticulos()) {
            for (Etiqueta e : articulo.getEtiquetas()) {
                if(e.getEtiqueta().equalsIgnoreCase(etiqueta)){
                    articulos.add(articulo);
                    break;
                }
            }
        }
        return articulos;
    }

}

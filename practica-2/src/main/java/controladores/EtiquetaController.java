package controladores;

import org.example.encapsulaciones.Etiqueta;

import java.util.ArrayList;
import java.util.List;

public class EtiquetaController {
    private static EtiquetaController instancia;
    private List<Etiqueta> listaEtiquetas = new ArrayList<>();

    private EtiquetaController() {
        listaEtiquetas.add(new Etiqueta(1L, "Deportes"));
        listaEtiquetas.add(new Etiqueta(2L, "Cine"));
        listaEtiquetas.add(new Etiqueta(3L, "Tecnologia"));
        listaEtiquetas.add(new Etiqueta(4L, "Musica"));
    }

    public static EtiquetaController getInstancia(){
        if(instancia==null){
            instancia = new EtiquetaController();
        }
        return instancia;
    }

    public List<Etiqueta> getListaEtiquetas(){
        return listaEtiquetas;
    }

    public Etiqueta buscarEtiquetaPorID(long id) {
        return listaEtiquetas.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public Etiqueta buscarEtiquetaPorNombre (String etiqueta) {
        for (Etiqueta e : listaEtiquetas) {
            if (e.getEtiqueta().equals(etiqueta)) {
                return e;
            }
        }
        return null;
    }
     public Etiqueta agregar(Etiqueta etiqueta) {
        listaEtiquetas.add(etiqueta);
        return etiqueta;
    }
    public List <Etiqueta> agregarEtiqueta(String[] etiquetas) {
        List <Etiqueta> lista = new ArrayList<>();
        for (String e : etiquetas) {
            if (this.buscarEtiquetaPorNombre(e) == null) {
                agregar(new Etiqueta(listaEtiquetas.size() + 1, e));
            }
            lista.add(this.buscarEtiquetaPorNombre(e));
        }
        return lista;
    }
}


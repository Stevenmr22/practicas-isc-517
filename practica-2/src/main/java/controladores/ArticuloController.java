package controladores;

import org.example.encapsulaciones.Articulo;
import org.example.encapsulaciones.Etiqueta;
import org.example.encapsulaciones.Usuario;
import org.example.encapsulaciones.Comentario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArticuloController {

    private ArrayList<Articulo> listaArticulos;
    private ArrayList<Etiqueta> listaEtiquetas;
    private static ArticuloController instancia;

    private ArticuloController() {
        listaArticulos = new ArrayList<>();
        listaEtiquetas = new ArrayList<Etiqueta>();
    }

    public static ArticuloController getInstancia() {
        if (instancia == null) {
            instancia = new ArticuloController();
        }
        return instancia;
    }

    public void crearArticulo(Articulo articulo, Usuario autor) {
        articulo.setAutor(autor);
        listaArticulos.add(articulo);
    }

    public void agregarEtiquetaALista(Etiqueta etiqueta) {
        listaEtiquetas.add(etiqueta);
    }

    public ArrayList<Articulo> getListaArticulos() {
        return listaArticulos;
    }

    public Articulo buscarArticuloPorId(UUID id) {
        for (Articulo articulo : listaArticulos) {
            if (articulo.getId().equals(id)) {
                return articulo;
            }
        }
        return null;
    }

    public void eliminarArticulo(UUID id) {
        listaArticulos.removeIf(articulo -> articulo.getId().equals(id));
    }

    public void eliminarComentario(UUID articuloId, UUID comentarioId) {
        Articulo articulo = buscarArticuloPorId(articuloId);
        if (articulo != null) {
            articulo.eliminarComentario(comentarioId);
        }
    }
    public Comentario buscarComentarioPorId(UUID comentarioId) {
        // Iterar sobre la lista de artículos y buscar el comentario por su ID
        for (Articulo articulo : listaArticulos) {
            for (Comentario comentario : articulo.getListaComentarios()) {
                if (comentario.getId().equals(comentarioId)) {
                    return comentario;
                }
            }
        }
        return null; // Si no se encuentra el comentario
    }
    public List<Articulo> getArticulosPorEtiqueta(String etiqueta) {
        List<Articulo> articulosPorEtiqueta = new ArrayList<>();
        for (Articulo articulo : listaArticulos) {
            for (Etiqueta et : articulo.getEtiquetas()) {
                if (et.getEtiqueta().equals(etiqueta)) {
                    articulosPorEtiqueta.add(articulo);
                    break; // Salir del bucle interior si se encuentra una coincidencia
                }
            }
        }
        return articulosPorEtiqueta;
    }
}

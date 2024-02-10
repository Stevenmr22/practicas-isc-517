package org.example.encapsulaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Articulo {
    private UUID id;
    private String titulo;
    private String cuerpo;
    private Usuario autor;
    private Date fecha;
    private List<Comentario> listaComentarios;
    private List<Etiqueta> listaEtiquetas;

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = new ArrayList<>();
        this.listaEtiquetas = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void agregarComentario(Comentario comentario) {
        this.listaComentarios.add(comentario);
    }

    public List<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void agregarEtiqueta(Etiqueta etiqueta) {
        this.listaEtiquetas.add(etiqueta);
    }

    public String obtenerResumen() {
        String resumen = cuerpo.length() > 70 ? cuerpo.substring(0, 70) + "..." : cuerpo;
        return resumen;
    }

    public List<Etiqueta> getEtiquetas() {
        return listaEtiquetas;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.listaEtiquetas = etiquetas;
    }

    public void eliminarComentario(UUID comentarioId) {
        listaComentarios.removeIf (comentario -> comentario.getId().equals(comentarioId));
    }


}

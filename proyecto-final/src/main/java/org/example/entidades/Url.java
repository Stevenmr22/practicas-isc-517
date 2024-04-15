package org.example.entidades;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity("urls")
public class Url {
    @Id
    private String id;
    private String urlOriginal;
    private String titulo;
    private String usernameCreador;
    @Indexed
    private Date fechaCreacion = new Date();

    public Url() { }
    public Url(String urlOriginal, String id, String usernameCreador, String titulo) {
        this.urlOriginal = urlOriginal;
        this.id = id;
        this.usernameCreador = usernameCreador;
        this.titulo = titulo;
    }

    public String getId() { return id; }
    public String getUrlOriginal() { return urlOriginal; }
    public void setUrlOriginal(String urlOriginal) { this.urlOriginal = urlOriginal; }
    public String getUsernameCreador() { return usernameCreador; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public void setUsernameCreador(String username) { this.usernameCreador = username; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

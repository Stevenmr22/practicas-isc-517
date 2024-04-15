package org.example.encapsulaciones;

import jakarta.persistence.*;

@Entity
public class Formulario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String sector;
    @Enumerated
    private NivelEscolar nivelEscolar;
    @ManyToOne
    private Usuario usuarioRegistrante;
    private double latitud;
    private double longitud;


    public Formulario() {}
    public Formulario(String nombre, String sector, NivelEscolar nivelEscolar, Usuario usuarioRegistrante, double latitud, double longitud) {
        this.nombre = nombre;
        this.sector = sector;
        this.nivelEscolar = nivelEscolar;
        this.usuarioRegistrante = usuarioRegistrante;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    public NivelEscolar getNivelEscolar() { return nivelEscolar; }
    public void setNivelEscolar(NivelEscolar nivelEscolar) { this.nivelEscolar = nivelEscolar; }
    public Usuario getUsuarioRegistrante() { return usuarioRegistrante; }
    public void setUsuarioRegistrante(Usuario usuarioRegistrante) { this.usuarioRegistrante = usuarioRegistrante; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}

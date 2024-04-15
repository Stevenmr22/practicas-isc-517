package org.example.entidades;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.util.Date;
@Entity("accesos")
public class Acceso {
    @Id
    private ObjectId id;
    @Indexed
    private String idUrlAcortada;
    private final Date fecha = new Date();
    private String navegador;
    private String ip;
    private String sistemaOperativo;
    private String dominioCliente;

    public Acceso() { }
    public Acceso(String navegador, String ip, String sistemaOperativo, String dominioCliente, String idUrlAcortada) {
        this.navegador = navegador;
        this.ip = ip;
        this.sistemaOperativo = sistemaOperativo;
        this.dominioCliente = dominioCliente;
        this.idUrlAcortada = idUrlAcortada;
    }

    public ObjectId getId() { return id; }
    public Date getFecha() { return fecha; }
    public String getNavegador() { return navegador; }
    public void setNavegador(String navegador) { this.navegador = navegador; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getSistemaOperativo() { return sistemaOperativo; }
    public void setSistemaOperativo(String sistemaOperativo) { this.sistemaOperativo = sistemaOperativo; }
    public String getDominioCliente() { return dominioCliente; }
    public void setDominioCliente(String dominioCliente) { this.dominioCliente = dominioCliente; }
    public String getIdUrlAcortada() { return idUrlAcortada; }

}

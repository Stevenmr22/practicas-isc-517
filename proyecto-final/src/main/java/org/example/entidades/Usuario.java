package org.example.entidades;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("Usuario")
public class Usuario {
    @Id
    private String username;
    private String nombre;
    private String password;
    private boolean admin;
    private boolean activo;

    public Usuario() { }
    public Usuario(String username, String nombre, String password, boolean admin) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.admin = admin;
        this.activo = true;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}

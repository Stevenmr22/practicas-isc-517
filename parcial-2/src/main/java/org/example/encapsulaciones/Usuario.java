package org.example.encapsulaciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private String username;
    private String password;
    private String nombre;
    private boolean admin;


    public Usuario() {}
    public Usuario(String username, String password, String nombre, boolean admin) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.admin = admin;
    }


    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}

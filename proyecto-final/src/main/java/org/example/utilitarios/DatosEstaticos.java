package org.example.utilitarios;

public enum DatosEstaticos {
    USUARIO("USUARIO"),
    URL_MONGO("URL_MONGO"),
    DB_NOMBRE("DB_NOMBRE"),
    LLAVE_SECRETA("llave_secreta_para_generar_jwt_proyectofinal1234567");

    private String valor;
    DatosEstaticos(String valor){ this.valor =  valor; }

    public String getValor() { return valor; }
}

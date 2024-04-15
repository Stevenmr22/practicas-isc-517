package org.example.encapsulaciones;

public enum NivelEscolar {
    BASICO{
        public String toString(){
            return "Basico";
        }
    }, MEDIO{
        public String toString(){
            return "Medio";
        }
    }, UNIVERSITARIO{
        public String toString(){
            return "Grado Universitario";
        }
    }, POSTGRADO{
        public String toString(){
            return "Postgrado";
        }
    }, DOCTORADO{
        public String toString(){
            return "Doctorado";
        }
    }
}

package servicios;

import encapsulaciones.Estudiante;
import util.NoExisteEstudianteException;
import util.ConexionJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class FakeServices {

    private static FakeServices instancia;

    private FakeServices(){
    }

    public static FakeServices getInstancia(){
        if(instancia==null){
            instancia = new FakeServices();
        }
        return instancia;
    }

    public List<Estudiante> listarEstudiante(){
        List <Estudiante> listaEstudiante = new ArrayList<>();
        Estudiante estudiante = new Estudiante();

        String sqlquery = "SELECT * FROM ESTUDIANTE";
        try {
            Statement st = ConexionJDBC.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sqlquery);
            while (rs.next()) {
                estudiante.setMatricula(rs.getInt("Matricula"));
                estudiante.setNombre(rs.getString("Nombre"));
                estudiante.setCarrera(rs.getString("Carrera"));
                listaEstudiante.add(estudiante);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return listaEstudiante;
    }

    public boolean verificarEstudiantePorMatricula(int matricula){
        boolean estudiante = false;
        String sqlquery = "SELECT * FROM ESTUDIANTE e WHERE e.Matricula = ?";

        try {
            PreparedStatement ps = ConexionJDBC.getConnection().prepareStatement(sqlquery);
            ps.setInt(1, matricula);
            ResultSet rs = ps.executeQuery();
            estudiante = (rs.next());
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudiante;
    }

    public void insertarEstudiante (Estudiante estudiante){

        if (verificarEstudiantePorMatricula(estudiante.getMatricula())){
            System.out.println("Estudiante insertado correctamente...");
            return;
        }

        String sqlquery = "INSERT INTO ESTUDIANTE (Matricula, Nombre, Carrera) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = ConexionJDBC.getConnection().prepareStatement(sqlquery);
            ps.setInt(1, estudiante.getMatricula());
            ps.setString(2, estudiante.getNombre());
            ps.setString(3, estudiante.getCarrera());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrarEstudiante (int matricula){
        String sqlquery = "DELETE FROM ESTUDIANTE e WHERE e.Matricula = ?";

        try {
            PreparedStatement ps = ConexionJDBC.getConnection().prepareStatement(sqlquery);
            ps.setInt(1, matricula);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstudiante (Estudiante estudiante){
        if(!verificarEstudiantePorMatricula(estudiante.getMatricula())){
            throw new NoExisteEstudianteException("Este estudiante no existe: "+estudiante.getMatricula());
        }
        String sqlquery = "UPDATE ESTUDIANTE e SET e.Matricula = ?, e.Nombre = ?, e.Carrera = ? WHERE e.Matricula = ?";

        try {
            PreparedStatement ps = ConexionJDBC.getConnection().prepareStatement(sqlquery);
            ps.setInt(1, estudiante.getMatricula());
            ps.setString(2, estudiante.getNombre());
            ps.setString(3, estudiante.getCarrera());
            ps.setInt(4, estudiante.getMatricula());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Estudiante getEstudiantePorMatricula(int matricula){
        Estudiante estudiante = null;
        String sqlquery = "SELECT * FROM ESTUDIANTE e WHERE e.Matricula = ?";

        try {
            PreparedStatement ps = ConexionJDBC.getConnection().prepareStatement(sqlquery);
            ps.setInt(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                estudiante = new Estudiante();
                estudiante.setMatricula(rs.getInt("Matricula"));
                estudiante.setNombre(rs.getString("Nombre"));
                estudiante.setCarrera(rs.getString("Carrera"));
                rs.close();
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudiante;
    }

}
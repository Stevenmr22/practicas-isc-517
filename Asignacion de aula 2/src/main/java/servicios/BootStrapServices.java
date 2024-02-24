package servicios;

import org.h2.tools.Server;
import util.ConexionJDBC;

import java.sql.SQLException;
import java.sql.Statement;

public class BootStrapServices {

    private static BootStrapServices instancia;

    private BootStrapServices(){

    }

    public static BootStrapServices getInstancia(){
        if(instancia == null){
            instancia=new BootStrapServices();
        }
        return instancia;
    }

    public void startDb() {
        try {
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon",
                    "-ifNotExists").start();
            String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
            crearTabla();
            System.out.println("Status Web: "+status);
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }

    private void crearTabla(){
        String sql = "CREATE TABLE IF NOT EXISTS " + "ESTUDIANTE (Matricula INT PRIMARY KEY, Nombre VARCHAR(255), Carrera VARCHAR(255))";
        try{
            Statement statement = ConexionJDBC.getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}

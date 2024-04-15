package org.example.servicios;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.example.utilitarios.DatosEstaticos;

public class GestionDB<T> {
    private Class<T> claseEntidad;
    private MongoClient cliente = MongoDbConexion.getMongoClient();
    public GestionDB(Class<T> claseEntidad) { this.claseEntidad = claseEntidad; }

    protected Datastore getConexionMorphia() {
        return Morphia.createDatastore(cliente, MongoDbConexion.getNombreBaseDatos());
    }

    public void crear(T entidad){
        Datastore datastore = getConexionMorphia();
        try {
            datastore.save(entidad);
        }catch (Exception  e){
            e.printStackTrace();
        }
    }

    public Query<T> find(){
        Datastore datastore = getConexionMorphia();
        Query<T> query = datastore.find(claseEntidad);
        return query;
    }

    public T findByID(String id){
        Datastore datastore = getConexionMorphia();
        Query<T> query = datastore.find(claseEntidad).filter(Filters.eq("_id", new ObjectId(id)));
        return  query.first();
    }

    public void deleteById(String id){
        Datastore datastore = getConexionMorphia();
        datastore.find(claseEntidad).filter(Filters.eq("_id", new ObjectId(id))).delete();
    }

    public void merge(T entidad){
        Datastore datastore = getConexionMorphia();
        try{
            datastore.merge(entidad);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class MongoDbConexion{
        private static MongoClient mongoClient;
        private static String nombreBaseDatos;

        public static MongoClient getMongoClient() {
            if(mongoClient == null){
                ProcessBuilder processBuilder = new ProcessBuilder();
                String URL_MONGODB = processBuilder.environment().get(DatosEstaticos.URL_MONGO.getValor());
                nombreBaseDatos = processBuilder.environment().get(DatosEstaticos.DB_NOMBRE.getValor());
                mongoClient = MongoClients.create(URL_MONGODB);
            }
            return mongoClient;
        }

        public static String getNombreBaseDatos(){
            return nombreBaseDatos;
        }
    }
}

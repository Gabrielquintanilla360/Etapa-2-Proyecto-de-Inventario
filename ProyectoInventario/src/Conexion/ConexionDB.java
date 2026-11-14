
package Conexion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ConexionDB {
    private static final String URI = "mongodb://localhost:27017";
    private static final String NOMBRE_BD = "inventario_db";

    public static MongoDatabase obtenerBaseDatos() {
        MongoClient cliente = MongoClients.create(URI);
        return cliente.getDatabase(NOMBRE_BD);
    }

    public static MongoCollection<Document> obtenerColeccionUsuarios() {
        return obtenerBaseDatos().getCollection("usuarios");
    }
}
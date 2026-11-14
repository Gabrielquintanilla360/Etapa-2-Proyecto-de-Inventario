
package Dao;


import Conexion.ConexionDB;
import Modelo.Producto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean registrarProducto(Producto p) {
        try {
            MongoDatabase db = ConexionDB.obtenerBaseDatos();
            MongoCollection<Document> coleccion = db.getCollection("productos");

            Document existente = coleccion.find(eq("codigo", p.getCodigo())).first();
            if (existente != null) {
                return false; 
            }

            Document documento = new Document("codigo", p.getCodigo())
                    .append("nombre", p.getNombre())
                    .append("descripcion", p.getDescripcion())
                    .append("categoria", p.getCategoria())
                    .append("precioUnitario", p.getPrecioUnitario())
                    .append("cantidadDisponible", p.getCantidadDisponible());

            coleccion.insertOne(documento);
            return true;
        } catch (Exception e) {
            System.out.println("Error al guardar en MongoDB: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> obtenerListaProductos() {
        List<Producto> lista = new ArrayList<>();
        try {
            MongoDatabase db = ConexionDB.obtenerBaseDatos();
            MongoCollection<Document> coleccion = db.getCollection("productos");
            MongoCursor<Document> cursor = coleccion.find().iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Producto p = new Producto(
                        doc.getString("codigo"),
                        doc.getString("nombre"),
                        doc.getString("descripcion"),
                        doc.getString("categoria"),
                        doc.getDouble("precioUnitario"),
                        doc.getInteger("cantidadDisponible")
                );
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error al consultar productos: " + e.getMessage());
        }
        return lista;
    }

    // Modificar los datos de un producto existente
    public boolean modificarProducto(Producto p) {
        try {
            MongoDatabase db = ConexionDB.obtenerBaseDatos();
            MongoCollection<Document> coleccion = db.getCollection("productos");

            var resultado = coleccion.updateOne(
                    eq("codigo", p.getCodigo()),
                    new Document("$set", new Document("nombre", p.getNombre())
                            .append("descripcion", p.getDescripcion())
                            .append("categoria", p.getCategoria())
                            .append("precioUnitario", p.getPrecioUnitario())
                            .append("cantidadDisponible", p.getCantidadDisponible()))
            );
            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // RF-07: Control específico de entradas y salidas de stock
    public boolean actualizarStock(String codigo, int nuevaCantidad) {
        try {
            MongoDatabase db = ConexionDB.obtenerBaseDatos();
            MongoCollection<Document> coleccion = db.getCollection("productos");

            var resultado = coleccion.updateOne(
                    eq("codigo", codigo),
                    set("cantidadDisponible", nuevaCantidad)
            );
            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean eliminarProducto(String codigo) {
        try {
            MongoDatabase db = ConexionDB.obtenerBaseDatos();
            MongoCollection<Document> coleccion = db.getCollection("productos");
            var resultado = coleccion.deleteOne(eq("codigo", codigo));
            return resultado.getDeletedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
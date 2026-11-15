
package Dao;

import Conexion.ConexionDB;
import Modelo.Producto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lte;

/** Consultas para reportes de existencias y valoración del inventario. */
public class ReportesDAO {

    private MongoCollection<Document> coleccionProductos() {
        MongoDatabase db = ConexionDB.obtenerBaseDatos();
        return db.getCollection("productos");
    }

    public List<Producto> obtenerProductosAgotados() {
        return convertirLista(
                coleccionProductos().find(lte("cantidadDisponible", 0))
        );
    }

    public List<Producto> obtenerProductosExistenciaBaja(int limite) {
        if (limite < 1) {
            throw new IllegalArgumentException(
                    "El límite debe ser mayor que cero."
            );
        }

        return convertirLista(
                coleccionProductos().find(
                        and(
                                gt("cantidadDisponible", 0),
                                lte("cantidadDisponible", limite)
                        )
                )
        );
    }

    public BigDecimal calcularValorTotalInventario() {
        BigDecimal total = BigDecimal.ZERO;

        for (Document doc : coleccionProductos().find()) {
            Object precioObj = doc.get("precioUnitario");
            Object cantidadObj = doc.get("cantidadDisponible");

            BigDecimal precio = precioObj instanceof Number
                    ? BigDecimal.valueOf(
                            ((Number) precioObj).doubleValue())
                    : BigDecimal.ZERO;

            int cantidad = cantidadObj instanceof Number
                    ? ((Number) cantidadObj).intValue()
                    : 0;

            total = total.add(
                    precio.multiply(BigDecimal.valueOf(cantidad))
            );
        }

        return total;
    }

    private List<Producto> convertirLista(
            Iterable<Document> documentos) {

        List<Producto> lista = new ArrayList<>();

        for (Document doc : documentos) {
            Object precio = doc.get("precioUnitario");
            Object cantidad = doc.get("cantidadDisponible");

            lista.add(new Producto(
                    doc.getString("codigo"),
                    doc.getString("nombre"),
                    doc.getString("descripcion"),
                    doc.getString("categoria"),
                    precio instanceof Number
                            ? ((Number) precio).doubleValue() : 0.0,
                    cantidad instanceof Number
                            ? ((Number) cantidad).intValue() : 0
            ));
        }

        return lista;
    }
}

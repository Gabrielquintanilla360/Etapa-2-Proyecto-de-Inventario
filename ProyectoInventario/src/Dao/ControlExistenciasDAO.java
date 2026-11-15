
package Dao;

import Conexion.ConexionDB;
import Modelo.Movimiento;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.inc;

/** Registra movimientos y actualiza existencias en MongoDB. */
public class ControlExistenciasDAO {

    private MongoCollection<Document> coleccionProductos() {
        MongoDatabase db = ConexionDB.obtenerBaseDatos();
        return db.getCollection("productos");
    }

    private MongoCollection<Document> coleccionMovimientos() {
        MongoDatabase db = ConexionDB.obtenerBaseDatos();
        return db.getCollection("movimientos");
    }

    private int entero(Document doc, String campo) {
        Object valor = doc.get(campo);
        return valor instanceof Number
                ? ((Number) valor).intValue() : 0;
    }

    public Movimiento registrarEntrada(String codigo, int cantidad) {
        validarDatos(codigo, cantidad);

        Document anterior = coleccionProductos().findOneAndUpdate(
                eq("codigo", codigo.trim()),
                inc("cantidadDisponible", cantidad),
                new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.BEFORE)
        );

        if (anterior == null) {
            throw new IllegalArgumentException(
                    "No existe un producto con ese código.");
        }

        int existenciaAnterior = entero(anterior, "cantidadDisponible");
        int existenciaNueva = existenciaAnterior + cantidad;

        Movimiento movimiento = new Movimiento(
                codigo.trim(),
                anterior.getString("nombre"),
                "ENTRADA",
                cantidad,
                existenciaAnterior,
                existenciaNueva,
                new Date()
        );

        coleccionMovimientos().insertOne(documentoMovimiento(movimiento));
        return movimiento;
    }

    public Movimiento registrarSalida(String codigo, int cantidad) {
        validarDatos(codigo, cantidad);

        Bson filtro = and(
                eq("codigo", codigo.trim()),
                gte("cantidadDisponible", cantidad)
        );

        Document anterior = coleccionProductos().findOneAndUpdate(
                filtro,
                inc("cantidadDisponible", -cantidad),
                new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.BEFORE)
        );

        if (anterior == null) {
            Document producto = coleccionProductos()
                    .find(eq("codigo", codigo.trim())).first();

            if (producto == null) {
                throw new IllegalArgumentException(
                        "No existe un producto con ese código.");
            }

            int disponible = entero(producto, "cantidadDisponible");

            throw new IllegalArgumentException(
                    "Existencias insuficientes. Disponibles: "
                    + disponible + "; solicitadas: " + cantidad + ".");
        }

        int existenciaAnterior = entero(anterior, "cantidadDisponible");
        int existenciaNueva = existenciaAnterior - cantidad;

        Movimiento movimiento = new Movimiento(
                codigo.trim(),
                anterior.getString("nombre"),
                "SALIDA",
                cantidad,
                existenciaAnterior,
                existenciaNueva,
                new Date()
        );

        coleccionMovimientos().insertOne(documentoMovimiento(movimiento));
        return movimiento;
    }

    public List<Movimiento> obtenerMovimientos() {
        List<Movimiento> lista = new ArrayList<>();

        FindIterable<Document> documentos =
                coleccionMovimientos().find().sort(descending("fecha"));

        for (Document doc : documentos) {
            lista.add(convertirMovimiento(doc));
        }

        return lista;
    }

    public List<Movimiento> obtenerMovimientosPorCodigo(String codigo) {
        List<Movimiento> lista = new ArrayList<>();

        FindIterable<Document> documentos =
                coleccionMovimientos()
                        .find(eq("codigoProducto", codigo.trim()))
                        .sort(descending("fecha"));

        for (Document doc : documentos) {
            lista.add(convertirMovimiento(doc));
        }

        return lista;
    }

    private void validarDatos(String codigo, int cantidad) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe indicar el código del producto.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }
    }

    private Document documentoMovimiento(Movimiento m) {
        return new Document("codigoProducto", m.getCodigoProducto())
                .append("nombreProducto", m.getNombreProducto())
                .append("tipoMovimiento", m.getTipoMovimiento())
                .append("cantidad", m.getCantidad())
                .append("existenciaAnterior", m.getExistenciaAnterior())
                .append("existenciaNueva", m.getExistenciaNueva())
                .append("fecha", m.getFecha());
    }

    private Movimiento convertirMovimiento(Document doc) {
        Date fecha = doc.getDate("fecha");

        return new Movimiento(
                doc.getString("codigoProducto"),
                doc.getString("nombreProducto"),
                doc.getString("tipoMovimiento"),
                entero(doc, "cantidad"),
                entero(doc, "existenciaAnterior"),
                entero(doc, "existenciaNueva"),
                fecha
        );
    }
}

package Modelo;

import java.util.Date;

/** Representa una entrada o salida del inventario. */
public class Movimiento {

    private final String codigoProducto;
    private final String nombreProducto;
    private final String tipoMovimiento;
    private final int cantidad;
    private final int existenciaAnterior;
    private final int existenciaNueva;
    private final Date fecha;

    public Movimiento(String codigoProducto, String nombreProducto,
                      String tipoMovimiento, int cantidad,
                      int existenciaAnterior, int existenciaNueva, Date fecha) {

        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.existenciaAnterior = existenciaAnterior;
        this.existenciaNueva = existenciaNueva;
        this.fecha = fecha == null ? new Date() : new Date(fecha.getTime());
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getExistenciaAnterior() {
        return existenciaAnterior;
    }

    public int getExistenciaNueva() {
        return existenciaNueva;
    }

    public Date getFecha() {
        return new Date(fecha.getTime());
    }
}
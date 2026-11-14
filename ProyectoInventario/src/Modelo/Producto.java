
package Modelo;

public class Producto {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private double precioUnitario;
    private int cantidadDisponible;

    // Constructor completo
    public Producto(String codigo, String nombre, String descripcion, String categoria, double precioUnitario, int cantidadDisponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precioUnitario = precioUnitario;
        this.cantidadDisponible = cantidadDisponible;
    }

    // Getters y Setters (Necesarios para que ProductoDAO y las vistas lean y escriban los datos)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
}
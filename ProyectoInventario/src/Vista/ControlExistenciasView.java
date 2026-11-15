
package Vista;

import Dao.ControlExistenciasDAO;
import Dao.ProductoDAO;
import Dao.ReportesDAO;
import Modelo.Movimiento;
import Modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ControlExistenciasView extends JFrame {

    private final ControlExistenciasDAO movimientosDAO =
            new ControlExistenciasDAO();

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ReportesDAO reportesDAO = new ReportesDAO();

    private final JTextField txtCodigo = new JTextField(12);
    private final JTextField txtCantidad = new JTextField(7);
    private final JTextField txtLimite = new JTextField("5", 4);

    private final JLabel lblResultado =
            new JLabel("Ingrese el código y la cantidad del movimiento.");

    private final DefaultTableModel modeloExistencias =
            new DefaultTableModel(
                    new Object[]{"Código", "Producto", "Categoría",
                        "Precio unitario", "Existencia", "Valor"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final DefaultTableModel modeloMovimientos =
            new DefaultTableModel(
                    new Object[]{"Fecha", "Código", "Producto",
                        "Movimiento", "Cantidad", "Antes", "Después"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final DefaultTableModel modeloReporte =
            new DefaultTableModel(
                    new Object[]{"Código", "Producto", "Categoría",
                        "Precio", "Existencia", "Valor"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable tablaExistencias = new JTable(modeloExistencias);
    private final JTable tablaMovimientos = new JTable(modeloMovimientos);
    private final JTable tablaReporte = new JTable(modeloReporte);

    private final JLabel lblTotal =
            new JLabel("Valor total del inventario: --");

    public ControlExistenciasView() {

        setTitle("Control de Existencias y Reportes");
        setSize(1000, 680);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelMovimiento =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panelMovimiento.setBorder(
                BorderFactory.createTitledBorder(
                        "Registrar entrada o salida"));

        panelMovimiento.add(new JLabel("Código del producto:"));
        panelMovimiento.add(txtCodigo);
        panelMovimiento.add(new JLabel("Cantidad:"));
        panelMovimiento.add(txtCantidad);

        JButton btnEntrada = new JButton("Registrar entrada");
        JButton btnSalida = new JButton("Registrar salida");
        JButton btnVolver = new JButton("Volver al menú");

        panelMovimiento.add(btnEntrada);
        panelMovimiento.add(btnSalida);
        panelMovimiento.add(btnVolver);

        add(panelMovimiento, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();

        pestañas.addTab(
                "Existencias actuales",
                new JScrollPane(tablaExistencias));

        pestañas.addTab(
                "Historial de movimientos",
                new JScrollPane(tablaMovimientos));

        JPanel panelReportes = new JPanel(new BorderLayout(8, 8));

        JPanel accionesReportes =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgotados = new JButton("Productos agotados");
        JButton btnBajos = new JButton("Existencias bajas");
        JButton btnValor = new JButton("Calcular valor total");

        accionesReportes.add(btnAgotados);
        accionesReportes.add(new JLabel("Límite de stock bajo:"));
        accionesReportes.add(txtLimite);
        accionesReportes.add(btnBajos);
        accionesReportes.add(btnValor);

        panelReportes.add(accionesReportes, BorderLayout.NORTH);
        panelReportes.add(
                new JScrollPane(tablaReporte), BorderLayout.CENTER);
        panelReportes.add(lblTotal, BorderLayout.SOUTH);

        pestañas.addTab("Reportes", panelReportes);

        add(pestañas, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout());

        lblResultado.setBorder(
                BorderFactory.createEmptyBorder(4, 10, 4, 10));

        inferior.add(lblResultado, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar tablas");
        inferior.add(btnActualizar, BorderLayout.EAST);

        add(inferior, BorderLayout.SOUTH);

        btnEntrada.addActionListener(e -> ejecutarMovimiento(true));
        btnSalida.addActionListener(e -> ejecutarMovimiento(false));
        btnActualizar.addActionListener(e -> actualizarTodo());

        btnVolver.addActionListener(e -> {
            new MenuPrincipal();
            dispose();
        });

        btnAgotados.addActionListener(e -> mostrarAgotados());
        btnBajos.addActionListener(e -> mostrarExistenciasBajas());
        btnValor.addActionListener(e -> mostrarValorTotal());

        actualizarTodo();
        setVisible(true);
    }

    private void ejecutarMovimiento(boolean esEntrada) {
        try {
            String codigo = txtCodigo.getText().trim();
            int cantidad = Integer.parseInt(
                    txtCantidad.getText().trim());

            Movimiento movimiento = esEntrada
                    ? movimientosDAO.registrarEntrada(codigo, cantidad)
                    : movimientosDAO.registrarSalida(codigo, cantidad);

            lblResultado.setText(
                    "Movimiento " + movimiento.getTipoMovimiento()
                    + " registrado. Existencia anterior: "
                    + movimiento.getExistenciaAnterior()
                    + "; nueva existencia: "
                    + movimiento.getExistenciaNueva() + ".");

            txtCantidad.setText("");
            actualizarTodo();

        } catch (NumberFormatException ex) {
            mostrarError("La cantidad debe ser un número entero.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void actualizarTodo() {
        cargarExistencias();
        cargarMovimientos();
    }

    private void cargarExistencias() {
        modeloExistencias.setRowCount(0);

        List<Producto> productos =
                productoDAO.obtenerListaProductos();

        for (Producto p : productos) {
            double valor =
                    p.getPrecioUnitario() * p.getCantidadDisponible();

            modeloExistencias.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                String.format("$%.2f", p.getPrecioUnitario()),
                p.getCantidadDisponible(),
                String.format("$%.2f", valor)
            });
        }
    }

    private void cargarMovimientos() {
        modeloMovimientos.setRowCount(0);

        SimpleDateFormat formato =
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Movimiento m : movimientosDAO.obtenerMovimientos()) {
            modeloMovimientos.addRow(new Object[]{
                formato.format(m.getFecha()),
                m.getCodigoProducto(),
                m.getNombreProducto(),
                m.getTipoMovimiento(),
                m.getCantidad(),
                m.getExistenciaAnterior(),
                m.getExistenciaNueva()
            });
        }
    }

    private void mostrarAgotados() {
        try {
            llenarReporte(reportesDAO.obtenerProductosAgotados());
            lblTotal.setText(
                    "Reporte: productos con existencia igual a cero.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarExistenciasBajas() {
        try {
            int limite =
                    Integer.parseInt(txtLimite.getText().trim());

            llenarReporte(
                    reportesDAO.obtenerProductosExistenciaBaja(limite));

            lblTotal.setText(
                    "Reporte: productos con existencia entre 1 y "
                    + limite + ".");

        } catch (NumberFormatException ex) {
            mostrarError("El límite debe ser un número entero.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarValorTotal() {
        try {
            modeloReporte.setRowCount(0);

            java.math.BigDecimal total =
                    reportesDAO.calcularValorTotalInventario();

            lblTotal.setText(
                    "Valor total del inventario: $"
                    + total.setScale(
                            2, java.math.RoundingMode.HALF_UP));

        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void llenarReporte(List<Producto> productos) {
        modeloReporte.setRowCount(0);

        for (Producto p : productos) {
            double valor =
                    p.getPrecioUnitario() * p.getCantidadDisponible();

            modeloReporte.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                String.format("$%.2f", p.getPrecioUnitario()),
                p.getCantidadDisponible(),
                String.format("$%.2f", valor)
            });
        }

        if (productos.isEmpty()) {
            lblTotal.setText(
                    "No se encontraron productos para este reporte.");
        }
    }

    private void mostrarError(String mensaje) {
        String texto = (mensaje == null || mensaje.isBlank())
                ? "Ocurrió un error al procesar la operación."
                : mensaje;

        lblResultado.setText("Error: " + texto);

        JOptionPane.showMessageDialog(
                this,
                texto,
                "Validación",
                JOptionPane.WARNING_MESSAGE);
    }
}

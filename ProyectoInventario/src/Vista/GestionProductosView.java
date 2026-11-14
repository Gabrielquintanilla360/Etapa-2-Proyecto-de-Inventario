package Vista;

import Dao.ProductoDAO;
import Modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GestionProductosView extends JFrame {

    JTextField txtCodigo, txtNombre, txtDescripcion, txtCategoria, txtPrecio, txtCantidad, txtBuscar;
    JButton btnGuardar, btnModificar, btnEliminar, btnRegresar, btnLogin;
    JTable tabla;
    DefaultTableModel modelo;
    ProductoDAO dao;

    public GestionProductosView() {
        dao = new ProductoDAO();

        setTitle("Gestión de Productos - Inventario");
        setSize(820, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Barra de Búsqueda 
        JLabel lBuscar = new JLabel("Buscar:");
        lBuscar.setBounds(20, 18, 55, 25);
        add(lBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(80, 18, 300, 25);
        add(txtBuscar);
        txtBuscar.setToolTipText("Escriba para buscar por código, nombre o categoría...");

        // Botones de navegación 
        btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(590, 18, 95, 25);
        btnRegresar.setFocusPainted(false);
        add(btnRegresar);

        btnLogin = new JButton("Salir");
        btnLogin.setBounds(695, 18, 95, 25);
        btnLogin.setFocusPainted(false);
        add(btnLogin);

        // Campos de la tabla
        JLabel l1 = new JLabel("Código:");
        l1.setBounds(20, 65, 80, 20);
        add(l1);
        txtCodigo = new JTextField();
        txtCodigo.setBounds(90, 65, 130, 25);
        add(txtCodigo);

        JLabel l2 = new JLabel("Nombre:");
        l2.setBounds(235, 65, 80, 20);
        add(l2);
        txtNombre = new JTextField();
        txtNombre.setBounds(300, 65, 180, 25);
        add(txtNombre);

        JLabel l3 = new JLabel("Descripción:");
        l3.setBounds(20, 100, 80, 20);
        add(l3);
        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(90, 100, 130, 25);
        add(txtDescripcion);

        JLabel l4 = new JLabel("Categoría:");
        l4.setBounds(235, 100, 80, 20);
        add(l4);
        txtCategoria = new JTextField();
        txtCategoria.setBounds(300, 100, 180, 25);
        add(txtCategoria);

        JLabel l5 = new JLabel("Precio ($):");
        l5.setBounds(20, 135, 80, 20);
        add(l5);
        txtPrecio = new JTextField();
        txtPrecio.setBounds(90, 135, 130, 25);
        add(txtPrecio);

        JLabel l6 = new JLabel("Cantidad:");
        l6.setBounds(235, 135, 80, 20);
        add(l6);
        txtCantidad = new JTextField();
        txtCantidad.setBounds(300, 135, 180, 25);
        add(txtCantidad);

        // Configuración de la tabla 
        modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Categoría");
        modelo.addColumn("Precio");
        modelo.addColumn("Cantidad");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 180, 770, 240);
        add(scroll);

        // Botones principales
        btnGuardar = new JButton("Registrar");
        btnGuardar.setBounds(210, 445, 125, 32);
        btnGuardar.setFocusPainted(false);
        add(btnGuardar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(345, 445, 125, 32);
        btnModificar.setFocusPainted(false);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(480, 445, 125, 32);
        btnEliminar.setFocusPainted(false);
        add(btnEliminar);

        cargarTabla("");

        // Consultar producto 
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if(fila >= 0) {
                    txtCodigo.setText(modelo.getValueAt(fila, 0).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                    txtDescripcion.setText(modelo.getValueAt(fila, 2).toString());
                    txtCategoria.setText(modelo.getValueAt(fila, 3).toString());
                    txtPrecio.setText(modelo.getValueAt(fila, 4).toString());
                    txtCantidad.setText(modelo.getValueAt(fila, 5).toString());
                    txtCodigo.setEditable(false); // Bloquear código por ser clave única
                }
            }
        });

        // Boton Buscar 
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String filtro = txtBuscar.getText().trim();
                cargarTabla(filtro);
            }
        });

        //Registrar producto
        btnGuardar.addActionListener(e -> {
            try {
                String c = txtCodigo.getText().trim();
                String n = txtNombre.getText().trim();
                String d = txtDescripcion.getText().trim();
                String cat = txtCategoria.getText().trim();
                String precioStr = txtPrecio.getText().trim();
                String cantidadStr = txtCantidad.getText().trim();

               // para que no quede informacion sin rellenar
                if(c.isEmpty() || n.isEmpty() || d.isEmpty() || cat.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por Favor Complete Todos Los Espacios.");
                    return;
                }

                double p = Double.parseDouble(precioStr);
                int q = Integer.parseInt(cantidadStr);

                if(q < 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad inicial no puede ser negativa.");
                    return;
                }

                Producto prod = new Producto(c, n, d, cat, p, q);
                if(dao.registrarProducto(prod)) {
                    JOptionPane.showMessageDialog(null, "¡Producto registrado con éxito!");
                    cargarTabla("");
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "El código de producto ya existe.");
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Verifique que el precio y la cantidad sean valores numéricos válidos.");
            }
        });

        // Proceso 1.4: Modificar producto
        btnModificar.addActionListener(e -> {
            try {
                String c = txtCodigo.getText().trim();
                String n = txtNombre.getText().trim();
                String d = txtDescripcion.getText().trim();
                String cat = txtCategoria.getText().trim();
                double p = Double.parseDouble(txtPrecio.getText().trim());
                int q = Integer.parseInt(txtCantidad.getText().trim());

                Producto prod = new Producto(c, n, d, cat, p, q);
                if(dao.modificarProducto(prod)) {
                    JOptionPane.showMessageDialog(null, "¡Información actualizada correctamente!");
                    cargarTabla("");
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro.");
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla y verifique los campos.");
            }
        });

        // Proceso 1.5: Eliminar producto previa confirmación
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if(fila >= 0) {
                String cod = modelo.getValueAt(fila, 0).toString();
                
                Object[] opciones = {"Sí", "No"};
                int opc = JOptionPane.showOptionDialog(null, 
                    "¿Está seguro de eliminar este producto?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, opciones, opciones[1]);

                if(opc == 0) {
                    if(dao.eliminarProducto(cod)) {
                        JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                        cargarTabla("");
                        limpiar();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla para eliminar.");
            }
        });

        // Navegación superior
        btnRegresar.addActionListener(e -> {
            new MenuPrincipal();
            dispose();
        });

        btnLogin.addActionListener(e -> {
            new LoginView();
            dispose();
        });

        setVisible(true);
    }

    public void cargarTabla(String filtro) {
        modelo.setRowCount(0);
        List<Producto> lista = dao.obtenerListaProductos();
        for(Producto p : lista) {
            if(filtro == null || filtro.isEmpty() || 
               p.getCodigo().toLowerCase().contains(filtro.toLowerCase()) || 
               p.getNombre().toLowerCase().contains(filtro.toLowerCase()) || 
               p.getCategoria().toLowerCase().contains(filtro.toLowerCase())) {
                
                modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getDescripcion(), p.getCategoria(), p.getPrecioUnitario(), p.getCantidadDisponible()});
            }
        }
    }

    public void limpiar() {
        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtBuscar.setText("");
    }
}

package Vista;

import javax.swing.*;

public class MenuPrincipal extends JFrame {

    JButton btnInventario, btnControlExistencias, btnSalir;

    public MenuPrincipal() {
        setTitle("Menú principal");
        setSize(320, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        btnInventario = new JButton("Gestión de productos");
        btnInventario.setBounds(50, 35, 210, 35);
        btnInventario.setFocusPainted(false);
        add(btnInventario);

        btnControlExistencias =
                new JButton("Control de existencias y reportes");
        btnControlExistencias.setBounds(50, 95, 210, 35);
        btnControlExistencias.setFocusPainted(false);
        add(btnControlExistencias);

        btnSalir = new JButton("Cerrar sesión");
        btnSalir.setBounds(50, 155, 210, 35);
        btnSalir.setFocusPainted(false);
        add(btnSalir);

        btnInventario.addActionListener(e -> {
            new GestionProductosView();
            dispose();
        });

        btnControlExistencias.addActionListener(e -> {
            new ControlExistenciasView();
            dispose();
        });

        btnSalir.addActionListener(e -> {
            new LoginView();
            dispose();
        });

        setVisible(true);
    }
}

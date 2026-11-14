package Vista;

import javax.swing.*;

public class MenuPrincipal extends JFrame {

    JButton btnInventario, btnSalir;

    public MenuPrincipal() {
        setTitle("Menu");
        setSize(300, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        btnInventario = new JButton("Inventario");
        btnInventario.setBounds(40, 40, 200, 35);
        btnInventario.setFocusPainted(false);
        add(btnInventario);

        btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setBounds(40, 95, 200, 35);
        btnSalir.setFocusPainted(false); 
        add(btnSalir);

        btnInventario.addActionListener(e -> {
            new GestionProductosView();
            dispose();
        });

        btnSalir.addActionListener(e -> {
            new LoginView();
            dispose();
        });

        setVisible(true);
    }
}
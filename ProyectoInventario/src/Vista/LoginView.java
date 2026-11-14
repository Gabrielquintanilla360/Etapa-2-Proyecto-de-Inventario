package Vista;

import Conexion.ConexionDB;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import javax.swing.*;
import java.awt.event.*;
import java.security.MessageDigest;

public class LoginView extends JFrame {

    JTextField txtUsuario;
    JPasswordField txtClave;
    JButton btnEntrar;

    public LoginView() {
        setTitle("Inicio De Sesión");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel l1 = new JLabel("Usuario:");
        l1.setBounds(30, 20, 100, 20);
        add(l1);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(30, 45, 220, 25);
        add(txtUsuario);

        JLabel l2 = new JLabel("Contraseña:");
        l2.setBounds(30, 80, 100, 20);
        add(l2);

        txtClave = new JPasswordField();
        txtClave.setBounds(30, 105, 220, 25);
        add(txtClave);

        btnEntrar = new JButton("Iniciar Sesión");
        btnEntrar.setBounds(30, 150, 220, 30);
        add(btnEntrar);

        verificarAdmin();

        btnEntrar.addActionListener(e -> {
            String u = txtUsuario.getText();
            String p = new String(txtClave.getPassword());

            if(validar(u, p)) {
                new MenuPrincipal();
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña o usuario incorrecto");
            }
        });

        setVisible(true);
    }

    public String sha256(String input) {
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            byte[] b = m.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch(Exception e) {
            return "";
        }
    }

    public void verificarAdmin() {
        try {
            MongoCollection<Document> col = ConexionDB.obtenerColeccionUsuarios();
            if(col.countDocuments() == 0) {
                col.insertOne(new Document("usuario", "admin").append("contrasena", sha256("1234")));
            }
        } catch(Exception e) {}
    }

    public boolean validar(String u, String p) {
        try {
            MongoCollection<Document> col = ConexionDB.obtenerColeccionUsuarios();
            Document d = col.find(eq("usuario", u)).first();
            if(d != null && d.getString("contrasena").equals(sha256(p))) {
                return true;
            }
        } catch(Exception e) {}
        return false;
    }

    public static void main(String[] args) {
        new LoginView();
    }
}
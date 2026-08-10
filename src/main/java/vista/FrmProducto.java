/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author acabi
 */
public class FrmProducto extends JInternalFrame {
    private JTextField txtId, txtNombre, txtPrecio, txtExistencia;
    private DefaultTableModel modeloTabla;
    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public FrmProducto() {
        super("Gestión de Productos", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtExistencia = new JTextField();

        panelForm.add(new JLabel("ID Producto:")); panelForm.add(txtId);
        panelForm.add(new JLabel("Nombre:")); panelForm.add(txtNombre);
        panelForm.add(new JLabel("Precio:")); panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Existencia (Stock):")); panelForm.add(txtExistencia);

        JButton btnGuardar = new JButton("Guardar Producto");
        btnGuardar.addActionListener(e -> guardarProducto());

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}, 0);
        JTable tabla = new JTable(modeloTabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(btnGuardar, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void guardarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtExistencia.getText());

            Producto p = new Producto(id, txtNombre.getText(), precio, stock);
            listaProductos.add(p);
            modeloTabla.addRow(new Object[]{p.getIdProducto(), p.getNombre(), p.getPrecio(), p.getExistencia()});
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
            limpiar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique los valores numéricos ingresados.");
        }
    }

    private void limpiar() {
        txtId.setText(""); txtNombre.setText(""); txtPrecio.setText(""); txtExistencia.setText("");
    }
}
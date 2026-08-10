/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import modelo.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author acabi
 */
public class FrmCliente extends JInternalFrame {
    private JTextField txtId, txtNombre, txtNit, txtTelefono, txtDireccion;
    private DefaultTableModel modeloTabla;
    private ArrayList<Cliente> listaClientes = new ArrayList<>();

    public FrmCliente() {
        super("Gestión de Clientes", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtNit = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();

        panelForm.add(new JLabel("ID:")); panelForm.add(txtId);
        panelForm.add(new JLabel("Nombre:")); panelForm.add(txtNombre);
        panelForm.add(new JLabel("NIT:")); panelForm.add(txtNit);
        panelForm.add(new JLabel("Teléfono:")); panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Dirección:")); panelForm.add(txtDireccion);

        JButton btnGuardar = new JButton("Guardar Cliente");
        btnGuardar.addActionListener(e -> guardarCliente());

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "NIT", "Teléfono", "Dirección"}, 0);
        JTable tabla = new JTable(modeloTabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(btnGuardar, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void guardarCliente() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Cliente c = new Cliente(id, txtNombre.getText(), txtNit.getText(), txtTelefono.getText(), txtDireccion.getText());
            listaClientes.add(c);
            modeloTabla.addRow(new Object[]{c.getId(), c.getNombre(), c.getNit(), c.getTelefono(), c.getDireccion()});
            JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.");
            limpiar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.");
        }
    }

    private void limpiar() {
        txtId.setText(""); txtNombre.setText(""); txtNit.setText(""); txtTelefono.setText(""); txtDireccion.setText("");
    }
}
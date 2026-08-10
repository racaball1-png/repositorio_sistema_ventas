/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import javax.swing.*;
/**
 *
 * @author Rigo_Acabal
 */


public class FrmPrincipal extends JFrame {
    private JDesktopPane desktopPane;

    public FrmPrincipal() {
        super("Sistema de Ventas - Formulario Contenedor MDI");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        JMenuBar menuBar = new JMenuBar();

        // Menú Mantenimiento (Catalogos)
        JMenu menuMantenimiento = new JMenu("Mantenimiento");
        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemProductos = new JMenuItem("Productos");

        itemClientes.addActionListener(e -> abrirFormulario(new FrmCliente()));
        itemProductos.addActionListener(e -> abrirFormulario(new FrmProducto()));

        menuMantenimiento.add(itemClientes);
        menuMantenimiento.add(itemProductos);

        // Menú Ventas
        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemFacturacion = new JMenuItem("Facturación");
        itemFacturacion.addActionListener(e -> abrirFormulario(new FrmFacturas()));
        menuVentas.add(itemFacturacion);

        menuBar.add(menuMantenimiento);
        menuBar.add(menuVentas);
        setJMenuBar(menuBar);
    }

    private void abrirFormulario(JInternalFrame iframe) {
        desktopPane.add(iframe);
        iframe.setVisible(true);
        try {
            iframe.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPrincipal().setVisible(true));
    }
}
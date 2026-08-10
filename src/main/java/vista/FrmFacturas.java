/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import dao.FacturaDAO;
import modelo.Cliente;
import modelo.DetalleFactura;
import modelo.Factura;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 *
 * @author Rigo_Acabal
 */
public class FrmFacturas extends JInternalFrame {

    private JTextField txtNumero, txtCliente, txtFecha, txtProducto, txtPrecio, txtCantidad;
    private JLabel lblSubtotal, lblTotal;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private FacturaDAO facturaDAO;
    
    // Lista temporal para almacenar los objetos Producto agregados a la tabla
    private ArrayList<Producto> listaProductosTabla = new ArrayList<>();

    public FrmFacturas() {
        super("Formulario de Facturación", true, true, true, true);
        this.setSize(750, 550);
        this.facturaDAO = new FacturaDAO();

        this.setLayout(new BorderLayout(10, 10));

        // --- PANEL NORTE: CABECERA DE FACTURA ---
        JPanel panelCabecera = new JPanel(new GridLayout(2, 4, 10, 10));
        panelCabecera.setBorder(BorderFactory.createTitledBorder("Datos de la Factura"));

        panelCabecera.add(new JLabel("No. Factura:"));
        int idSiguiente = facturaDAO.listar().size() + 1;
        txtNumero = new JTextField(String.valueOf(idSiguiente));
        txtNumero.setEditable(false);
        panelCabecera.add(txtNumero);

        panelCabecera.add(new JLabel("Fecha (yyyy-MM-dd):"));
        txtFecha = new JTextField(LocalDate.now().toString());
        panelCabecera.add(txtFecha);

        panelCabecera.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        panelCabecera.add(txtCliente);

        panelCabecera.add(new JLabel("")); 
        panelCabecera.add(new JLabel("")); 

        this.add(panelCabecera, BorderLayout.NORTH);

        // --- PANEL CENTRO ---
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));

        JPanel panelInputProducto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInputProducto.setBorder(BorderFactory.createTitledBorder("Agregar Artículo"));

        txtProducto = new JTextField(12);
        txtPrecio = new JTextField(5);
        txtCantidad = new JTextField(4);
        JButton btnAgregar = new JButton("Agregar");

        panelInputProducto.add(new JLabel("Producto:"));
        panelInputProducto.add(txtProducto);
        panelInputProducto.add(new JLabel("Precio:"));
        panelInputProducto.add(txtPrecio);
        panelInputProducto.add(new JLabel("Cantidad:"));
        panelInputProducto.add(txtCantidad);
        panelInputProducto.add(btnAgregar);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Producto", "Precio", "Cantidad", "Subtotal"}, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        panelCentro.add(panelInputProducto, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        this.add(panelCentro, BorderLayout.CENTER);

        // --- PANEL SUR ---
        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelTotales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblTotal = new JLabel("TOTAL: $0.00");
        lblTotal.setFont(new Font("Tahoma", Font.BOLD, 14));

        panelTotales.add(lblSubtotal);
        panelTotales.add(Box.createHorizontalStrut(20));
        panelTotales.add(lblTotal);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        JButton btnGuardar = new JButton("Guardar Factura");

        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnGuardar);

        panelSur.add(panelTotales, BorderLayout.NORTH);
        panelSur.add(panelAcciones, BorderLayout.SOUTH);

        this.add(panelSur, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProductoATabla());
        btnEliminar.addActionListener(e -> eliminarProductoDeTabla());
        btnGuardar.addActionListener(e -> guardarFactura());
    }

    private void agregarProductoATabla() {
        try {
            String prodNombre = txtProducto.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (prodNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre del producto.");
                return;
            }

            // Crear objeto Producto compatible con el Diagrama UML
            int idProd = listaProductosTabla.size() + 1;
            Producto p = new Producto(idProd, prodNombre, precio, 100);
            listaProductosTabla.add(p);

            double subtotal = precio * cantidad;
            modeloTabla.addRow(new Object[]{p.getIdProducto(), p.getNombre(), precio, cantidad, subtotal});

            limpiarCamposProducto();
            actualizarTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y Cantidad deben ser valores numéricos válidos.");
        }
    }

    private void eliminarProductoDeTabla() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            modeloTabla.removeRow(filaSeleccionada);
            listaProductosTabla.remove(filaSeleccionada);
            actualizarTotales();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        }
    }

    private void actualizarTotales() {
        double subtotalAcumulado = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            subtotalAcumulado += (double) modeloTabla.getValueAt(i, 4);
        }
        lblSubtotal.setText(String.format("Subtotal: $%.2f", subtotalAcumulado));
        lblTotal.setText(String.format("TOTAL: $%.2f", subtotalAcumulado));
    }

    private void guardarFactura() {
        String nombreCliente = txtCliente.getText().trim();
        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del cliente.");
            return;
        }

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la factura.");
            return;
        }

        try {
            int idFactura = Integer.parseInt(txtNumero.getText());
            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            
            // Instanciar objeto Cliente (derivado de Persona en el UML)
            Cliente clienteObj = new Cliente(1, nombreCliente, "CF", "00000000", "Ciudad");

            // Instanciar Factura con los tipos correctos del UML
            Factura nuevaFactura = new Factura(idFactura, fecha, clienteObj);

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                Producto p = listaProductosTabla.get(i);
                int cant = (int) modeloTabla.getValueAt(i, 3);
                double prec = (double) modeloTabla.getValueAt(i, 2);

                // Instanciar DetalleFactura con tipos UML
                DetalleFactura detalle = new DetalleFactura(p, cant, prec);
                nuevaFactura.agregarDetalle(detalle);
            }

            if (facturaDAO.guardar(nuevaFactura)) {
                JOptionPane.showMessageDialog(this, "Factura guardada exitosamente en memoria.");
                this.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la factura: " + ex.getMessage());
        }
    }

    private void limpiarCamposProducto() {
        txtProducto.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtProducto.requestFocus();
    }
}
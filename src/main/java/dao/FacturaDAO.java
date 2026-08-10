/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import modelo.DetalleFactura;
import modelo.Factura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author Rigo_Acabal
 */
public class FacturaDAO {

    public int obtenerSiguienteId() {
        String sql = "SELECT COALESCE(MAX(id_factura), 0) + 1 FROM factura";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el siguiente ID: " + e.getMessage());
        }
        return 1;
    }

    public boolean guardar(Factura factura) {
        String sqlFactura = "INSERT INTO factura (id_factura, fecha, cliente_nombre, total) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_factura (id_factura, producto_nombre, precio_unitario, cantidad, subtotal) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = Conexion.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción SQL

            // 1. Insertar Cabecera
            try (PreparedStatement stmtFactura = conn.prepareStatement(sqlFactura)) {
                stmtFactura.setInt(1, factura.getIdFactura());
                stmtFactura.setDate(2, Date.valueOf(factura.getFecha()));
                stmtFactura.setString(3, factura.getCliente().getNombre());
                stmtFactura.setDouble(4, factura.calcularTotal());
                stmtFactura.executeUpdate();
            }

            // 2. Insertar Detalle
            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                for (DetalleFactura det : factura.getDetalles()) {
                    stmtDetalle.setInt(1, factura.getIdFactura());
                    stmtDetalle.setString(2, det.getProducto().getNombre());
                    stmtDetalle.setDouble(3, det.getPrecioUnitario());
                    stmtDetalle.setInt(4, det.getCantidad());
                    stmtDetalle.setDouble(5, det.calcularSubtotal());
                    stmtDetalle.addBatch();
                }
                stmtDetalle.executeBatch();
            }

            conn.commit(); // Confirmar cambios en la BD
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            // Muestra en pantalla el error exacto de SQL que impide guardar
            JOptionPane.showMessageDialog(null, "Error SQL al guardar: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public List<Factura> listar() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM factura";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_factura");
                Date fechaSql = rs.getDate("fecha");
                String clienteNombre = rs.getString("cliente_nombre");

                modelo.Cliente clienteObj = new modelo.Cliente(1, clienteNombre, "CF", "0000", "Ciudad");
                Factura f = new Factura(id, fechaSql.toLocalDate(), clienteObj);
                lista.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar facturas: " + e.getMessage());
        }
        return lista;
    }
}

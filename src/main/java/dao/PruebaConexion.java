/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;

/**
 *
 * @author acabi
 */
public class PruebaConexion {
    public static void main(String[] args) {
        try {
            Connection conn = Conexion.getConexion();
            if (conn != null && !conn.isClosed()) {
                System.out.println("==========================================");
                System.out.println("   ¡CONEXIÓN EXITOSA CON MYSQL! ");
                System.out.println("==========================================");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("==========================================");
            System.out.println("   ERROR AL CONECTAR: " + e.getMessage());
            System.out.println("==========================================");
        }
    }
}
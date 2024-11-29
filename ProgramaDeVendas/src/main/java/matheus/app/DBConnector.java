package matheus.app;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBConnector {
    private static String url = "jdbc:mariadb://localhost:3306/";
    private static String driverName = "org.mariadb.jdbc.Driver";
    private static String username = "user";
    private static String password = "123";
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);

            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(true); 

            try (Statement stmt = con.createStatement()) {
                String command = "CREATE DATABASE IF NOT EXISTS mercado";
                stmt.executeUpdate(command);
            }

            con.setCatalog("mercado");

            try (Statement stmt = con.createStatement()) {
                String command = "CREATE TABLE IF NOT EXISTS clientes (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(30), cpf LONG, telefone LONG)";
                stmt.executeUpdate(command);
                command = "CREATE TABLE IF NOT EXISTS produtos (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(30), preço FLOAT, categoria VARCHAR(30))";
                stmt.executeUpdate(command);

                command = "SELECT COUNT(*) FROM produtos";
                int count = 0;

                ResultSet rs = stmt.executeQuery(command);
                if (rs.next()){
                    count = rs.getInt(1);
                }
                if (count == 0){
                      command = "INSERT INTO produtos (nome, preço, categoria) VALUES "
                              + "('luvas de boxe', 59.99, 'esporte'), "
                              + "('bola de futebol', 49.99, 'esporte'), "
                              + "('bastão de baseball', 99.99, 'esporte'), "
                              + "('camisa', 29.99, 'roupa'), "
                              + "('calça', 99.99, 'roupa'), "
                              + "('tenis', 299.99, 'roupa'), "
                              + "('TV', 1199.99, 'eletronicos'), "
                              + "('video game', 3899.99, 'eletronicos'), "
                              + "('geladeira', 3839.99, 'eletronicos')";
                      stmt.executeUpdate(command);
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("SQL error: " + ex.getMessage());
        }
        return con;
    }
}


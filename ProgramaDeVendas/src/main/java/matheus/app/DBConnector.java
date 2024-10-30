package matheus.app;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;

public class DBConnector {
    private static String url = "jdbc:mariadb://localhost:3306/";
    private static String driverName = "org.mariadb.jdbc.Driver";
    private static String username = "user";
    private static String password = "123";
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);

            // Cria a conexão sem especificar o banco de dados
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(true); // Habilita auto-commit

            // Criação do banco de dados "mercado"
            try (Statement stmt = con.createStatement()) {
                String command = "CREATE DATABASE IF NOT EXISTS mercado";
                stmt.executeUpdate(command);
            }

            // Agora que o banco de dados foi criado, conecte-se a ele
            con.setCatalog("mercado");

            // Criação das tabelas
            try (Statement stmt = con.createStatement()) {
                String command = "CREATE TABLE IF NOT EXISTS clientes (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(30), cpf LONG, telefone LONG)";
                stmt.executeUpdate(command);
                command = "CREATE TABLE IF NOT EXISTS produtos (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(30), preço FLOAT, categoria VARCHAR(30))";
                stmt.executeUpdate(command);
                
                // Inserindo dados na tabela produtos
                command = "INSERT INTO produtos (nome, preço, categoria) VALUES "
                        + "('luvas de boxe', 59.99, 'esporte'), "
                        + "('bola de futebol', 49.99, 'esporte'), "
                        + "('bastão de baseball', 99.99, 'esporte'), "
                        + "('camisa', 29.99, 'roupa'), "
                        + "('calça', 99.99, 'roupa'), "
                        + "('tenis', 299.99, 'roupa'), "
                        + "('TV', 1199.90, 'eletronicos'), "
                        + "('video game', 3899.99, 'eletronicos'), "
                        + "('geladeira', 3839.99, 'eletronicos')";
                stmt.executeUpdate(command);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("SQL error: " + ex.getMessage());
        }
        return con;
    }
}


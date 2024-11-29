package matheus.app;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement; 
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Connection connection = DBConnector.getConnection();
        Scanner input = new Scanner(System.in);
        int opcao = 0;
        System.out.println("Bem vindo ao nosso mercado!");

        while (true) {
            System.out.println("(1) Comprar (2) Sair");
            if (input.hasNextInt()) {
                opcao = input.nextInt();
                input.nextLine();
            }

            if (opcao == 1) {
                System.out.println("Gostaria de usar nosso cadastro?");
                while (true) {
                    System.out.println("(1) Sim (2) Não");
                    if (input.hasNextInt()) {
                        opcao = input.nextInt();
                        input.nextLine(); 
                    }

                    if (opcao == 1) {
                        System.out.print("Digite seu CPF (somente números): ");
                        long cpf = 0;
                        while (true) {
                            if (input.hasNextLong()) {
                                cpf = input.nextLong();
                                input.nextLine(); 
                                break;
                            } else {
                                input.nextLine();
                            }
                        }

                        String command = "SELECT cpf FROM clientes WHERE cpf=?";
                        try (PreparedStatement ps = connection.prepareStatement(command)) { 
                            ps.setLong(1, cpf);
                            ResultSet rs = ps.executeQuery();

                            if (rs.next()) {
                                System.out.println("Cliente já cadastrado.");
                            } else {
                                Long telefone = null;
                                String nome;
                                System.out.print("Digite seu telefone: ");
                                if(input.hasNextLong()){
                                    telefone = input.nextLong();
                                    input.nextLine();
                                }
                                System.out.print("Digite seu nome: ");
                                nome = input.nextLine();
                                input.nextLine();
                                command = "INSERT INTO clientes(nome,cpf,telefone) VALUES (?,?,?)";
                                try (PreparedStatement insertStatement = connection.prepareStatement(command)) {
                                    insertStatement.setString(1, nome);
                                    insertStatement.setLong(2,cpf);
                                    insertStatement.setLong(3,telefone);
                                    insertStatement.executeUpdate();
                                    System.out.println("Cliente cadastrado");
                                }
                            }
                        } catch (SQLException e) {
                            System.out.println("Erro ao consultar ou inserir no banco de dados: " + e.getMessage());
                        }
                    } 
                    break;
                }
                try {
                    String command = "SELECT nome FROM produtos";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(command);
                    String[] produtos = new String[9];
                    int item = 1; 
                    while(rs.next()){
                       produtos[item - 1] = rs.getString("nome");
                       System.out.println("item " + item + "." + produtos[item - 1]);
                       item++;
                    }
                    System.out.print("Escolha seu produto: ");
                    int produtoSelecionado = 0;
                    if(input.hasNextInt())
                        produtoSelecionado = input.nextInt();
                    System.out.println("Você comprou " + produtos[produtoSelecionado - 1]);
                }  
                catch(Exception e){
                    e.printStackTrace();
                }
            } 
            else {
              break;
            }
        }
    }
}

package appliacation;

import dao.BibliotecaDao;
import dao.DaoFactory;
import exceptions.DbException;
import exceptions.EmprestimoInvalidoException;
import exceptions.LivroNaoEncontradoException;
import model.Livro;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BibliotecaDao biblioteca;

        try {
            biblioteca = DaoFactory.createBibliotecaDao();
        } catch (DbException e) {
            throw new RuntimeException(e);
        }

        int op;

        do {
            System.out.println();
            System.out.println("------------ MENU ------------");
            System.out.println("-> Adicionar                :1");
            System.out.println("-> Buscar                   :2");
            System.out.println("-> Lista acervo             :3");
            System.out.println("-> Emprestar                :4");
            System.out.println("-> Devolver                 :5");
            System.out.println("-> Sair                     :0");

            System.out.print("#Operação: ");
            op = input.nextInt();
            Util.limparBuffer(input);

            switch (op) {
                case 1:
                    System.out.print("\nInforme o nome do livro: ");
                    String titulo = input.nextLine();

                    System.out.print("Informe o autor: ");
                    String autor = input.nextLine();

                    try {
                        biblioteca.adicionar(new Livro(0, titulo, autor, false));
                        System.out.println("\nLivro adicionado com sucesso!");
                    } catch (SQLException | EmprestimoInvalidoException | DbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("\nDeseja buscar por autor ou título? (Digite 't' para título ou 'a' para autor): ");
                        char operador = input.next().charAt(0);
                        Util.limparBuffer(input);

                        if (operador != 't' && operador != 'a') {
                            throw new LivroNaoEncontradoException();
                        }

                        System.out.print("Informe o " + (operador == 't' ? "título" : "autor") + " do livro: ");
                        String pesquisa = input.nextLine();

                        try {
                            List<Livro> livros;
                            livros = biblioteca.buscar(pesquisa, operador);

                            if (!livros.isEmpty()) {
                                System.out.println();
                                for (Livro livro : livros) {
                                    System.out.println("Id: " + livro.getId()
                                        + " | Titulo: " + livro.getTitulo()
                                        + " | Autor: " + livro.getAutor()
                                        + " | Está emprestado? " + (livro.isEstaEmprestado() ? "Sim" : "Não") + ";"
                                    );
                                }
                            } else {
                                throw new LivroNaoEncontradoException();
                            }
                        } catch (SQLException | LivroNaoEncontradoException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (Exception e) {   // Para o caso onde o usuário digita um termo com tipo incorreto
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        List<Livro> livros;
                        livros = biblioteca.listarAcervo();

                        if (!livros.isEmpty()) {
                            System.out.println();
                            for (Livro livro : livros) {
                                System.out.println("Id: " + livro.getId()
                                        + " | Titulo: " + livro.getTitulo()
                                        + " | Autor: " + livro.getAutor()
                                        + " | Está emprestado? " + (livro.isEstaEmprestado() ? "Sim" : "Não") + ";"
                                );
                            }
                        } else {
                            throw new LivroNaoEncontradoException();
                        }
                    } catch (SQLException | LivroNaoEncontradoException | DbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("\nInforme o ID do livro que você está emprestando: ");
                    int idEmprestar  = input.nextInt();
                    Util.limparBuffer(input);

                    try {
                        biblioteca.emprestar(idEmprestar);
                        System.out.println("Emprestado com sucesso!");
                    } catch (SQLException | EmprestimoInvalidoException | DbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("\nInforme o ID do livro que você está devolvendo: ");
                    int idDevolver  = input.nextInt();
                    Util.limparBuffer(input);

                    try {
                        biblioteca.devolver(idDevolver);
                        System.out.println("Devolvido com sucesso!");
                    } catch (SQLException | DbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0: break;

                default:
                    System.out.println("\nOps... Operação inválida!");
                    break;
            }
        } while (op != 0);

        try {
            biblioteca.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

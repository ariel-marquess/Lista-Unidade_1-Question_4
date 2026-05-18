package exceptions;

public class LivroNaoEncontradoException extends Exception {
    public LivroNaoEncontradoException() {
        super("\nERRO: livro não encontrado.");
    }
}

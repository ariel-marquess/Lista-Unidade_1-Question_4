package exceptions;

public class LimiteAcervoException extends RuntimeException {
    public LimiteAcervoException() {
        super("\nERRO: limite do acervo atingido.");
    }
}

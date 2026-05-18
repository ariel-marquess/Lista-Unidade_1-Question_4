package exceptions;

public class EmprestimoInvalidoException extends Exception {
    public EmprestimoInvalidoException(String message) {
        super("\nERRO: empréstimo inválido (" + message + ").");
    }
}

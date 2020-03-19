package unwe.register.UnweRegister.exceptions;

public class PasswordsNotMatchingException extends RuntimeException {
    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}

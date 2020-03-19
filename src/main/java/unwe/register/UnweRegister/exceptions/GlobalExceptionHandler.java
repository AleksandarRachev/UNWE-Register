package unwe.register.UnweRegister.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ElementNotPresentException.class, ElementAlreadyExistsException.class,
            PasswordsNotMatchingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleBadRequest(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadCredentialsException.class, WrongCredentialsException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> handleForbidden(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.FORBIDDEN);
    }

}

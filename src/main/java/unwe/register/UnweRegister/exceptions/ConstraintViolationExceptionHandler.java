package unwe.register.UnweRegister.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Optional<String> errorMsg = e.getBindingResult().getFieldErrors().stream()
                .filter(error -> error.getDefaultMessage() != null)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .findFirst();
        return new ResponseEntity<>(new ErrorMessage(errorMsg.orElse("No error messages")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorMessage> handleConstraintViolation(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        Optional<String> first = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .max(Comparator.naturalOrder());

        return new ResponseEntity<>(new ErrorMessage(first.orElse("No error messages")), HttpStatus.BAD_REQUEST);
    }
}

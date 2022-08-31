package com.vikash.expense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.validation.BindException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(
            {
                    ExpenseNotFoundException.class
            }
    )
    public <T extends BindException> ResponseEntity<ExceptionMessage> handleApiRequestException(final T ex, WebRequest request){
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
      ExceptionMessage exceptionMessage = ExceptionMessage.
              builder().
              message(messages).
              timestamp(new Date().toString()).
              status(HttpStatus.NOT_FOUND.value()).
              build();
      return new ResponseEntity<>(exceptionMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            {
                    EmailAlreadyRegisteredException.class
            }
    )
    public ResponseEntity<ExceptionMessage> handleApiRequestException(EmailAlreadyRegisteredException ex){
        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());
        ExceptionMessage exceptionMessage = ExceptionMessage.
                builder().
                message(messages).
                timestamp(new Date().toString()).
                status(HttpStatus.CONFLICT.value()).
                build();
        return new ResponseEntity<>(exceptionMessage,HttpStatus.CONFLICT);
    }

    @ExceptionHandler( ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessage> handleConstraintViolationException(ConstraintViolationException exception,WebRequest request){
        List<String> messages = exception.getConstraintViolations().stream()
                .map(ex -> ex.getMessage()).collect(Collectors.toList());
        ExceptionMessage exceptionMessage = ExceptionMessage.
                builder().
                message(messages).
                timestamp(new Date().toString()).
                status(HttpStatus.BAD_REQUEST.value()).
                build();
        return new ResponseEntity<>(exceptionMessage,HttpStatus.BAD_REQUEST);
    }


}

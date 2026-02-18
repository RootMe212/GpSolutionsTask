package com.gpsolutions.taskhotels.controller;

import com.gpsolutions.taskhotels.exception.ErrorResponse;
import com.gpsolutions.taskhotels.exception.HotelNotFound;
import com.gpsolutions.taskhotels.exception.InvalidHistogramParam;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HotelNotFound.class)
  public ResponseEntity<ErrorResponse> handleHotelNotFound(HotelNotFound ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Not Found",
        ex.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> handleValidationError(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ){
    Map<String,Object> error = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(errorField -> {
      String fieldName = ((FieldError) errorField).getField();
      String errorMessage = errorField.getDefaultMessage();
      error.put(fieldName, errorMessage);
    });

    Map<String,Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Validation Error");
    response.put("errors", error);
    response.put("path", request.getRequestURI());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(InvalidHistogramParam.class)
  public ResponseEntity<ErrorResponse> handleInvalidHistogramParam(
      InvalidHistogramParam ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        "Bad Request",
        ex.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handlerGenericError(
      Exception ex,
      HttpServletRequest request
  ){
    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error",
        ex.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

}

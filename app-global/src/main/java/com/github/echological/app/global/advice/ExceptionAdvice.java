package com.github.echological.app.global.advice;

import com.github.echological.app.global.constant.AVRHttpStatus;
import com.github.echological.app.global.exception.AVRBusinessValidationException;
import com.github.echological.app.global.exception.AVRContentBusinessValidationException;
import com.github.echological.app.global.model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    private static final String LOG_ERROR_MSG = "requestId {} error: {}";
    private static final String LOG_STACKTRACE_MSG = "requestId {} stacktrace: {}";

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();
        log.error(LOG_ERROR_MSG, requestId, ex.getMessage());
        log.error(LOG_STACKTRACE_MSG, requestId, ex.getStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.UNSUPPORTED_MEDIA_TYPE.getCode())
                .responseMessage(AVRHttpStatus.UNSUPPORTED_MEDIA_TYPE.getStatus())
                .requestId(requestId)
                .error(Stream.of("Unsupported media type.").collect(Collectors.toList()))
                .requestTimestamp(new Date().getTime())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();
        log.error(LOG_ERROR_MSG, requestId, ex.getMessage());
        log.error(LOG_STACKTRACE_MSG, requestId, ex.getStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.NO_HANDLER_FOUND.getCode())
                .responseMessage(AVRHttpStatus.NO_HANDLER_FOUND.getStatus())
                .requestId(requestId)
                .error(Stream.of("No handler found for this path.").collect(Collectors.toList()))
                .requestTimestamp(new Date().getTime())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();
        log.error(LOG_ERROR_MSG, requestId, ex.getMessage());
        log.error(LOG_STACKTRACE_MSG, requestId, ex.getStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.MESSAGE_NOT_READABLE.getCode())
                .responseMessage(AVRHttpStatus.MESSAGE_NOT_READABLE.getStatus())
                .requestId(requestId)
                .error(Stream.of("Message not readable").collect(Collectors.toList()))
                .requestTimestamp(new Date().getTime())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();

        List<Map<String, String>> errorList = new ArrayList<>();
        for(FieldError x:ex.getFieldErrors()){
            Map<String, String> error = new HashMap<>();
            error.put(x.getField(), x.getDefaultMessage());
            errorList.add(error);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.INVALID_ARGUMENT.getCode())
                .responseMessage(AVRHttpStatus.INVALID_ARGUMENT.getStatus())
                .requestId(requestId)
                .requestTimestamp(new Date().getTime())
                .error(errorList)
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<BaseResponse<Object>> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();
        log.error(LOG_ERROR_MSG, requestId, ex.getMessage());
        log.error(LOG_STACKTRACE_MSG, requestId, ex.getStackTrace());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.ERROR.getCode())
                .responseMessage(AVRHttpStatus.ERROR.getStatus())
                .requestId(requestId)
                .requestTimestamp(new Date().getTime())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();
        log.error(LOG_ERROR_MSG, requestId, ex.getMessage());
        log.error(LOG_STACKTRACE_MSG, requestId, ex.getStackTrace());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.ERROR.getCode())
                .responseMessage(AVRHttpStatus.ERROR.getStatus())
                .requestId(requestId)
                .requestTimestamp(new Date().getTime())
                .build());
    }

    @ExceptionHandler(AVRBusinessValidationException.class)
    public final ResponseEntity<BaseResponse<Object>> handleBusinessValidationException(AVRBusinessValidationException ex, WebRequest request) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();

        return ResponseEntity.status(ex.getHttpStatus()).body(BaseResponse.builder()
                .responseCode(ex.getResponseCode())
                .responseMessage(ex.getResponseMessage())
                .error(ex.getError())
                .requestId(requestId)
                .requestTimestamp(new Date().getTime())
                .content(null)
                .build());
    }

    @ExceptionHandler(AVRContentBusinessValidationException.class)
    public final ModelAndView handleContentBusinessValidationException(AVRBusinessValidationException ex, WebRequest request, Model model) {
        ex.printStackTrace();

        String requestId = UUID.randomUUID().toString();
        log.error(LOG_ERROR_MSG, requestId, ex.getMessage());
        log.error(LOG_STACKTRACE_MSG, requestId, ex.getStackTrace());

        model.addAttribute("requestId", requestId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404err");
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.addObject("requestId", requestId);

        return modelAndView;
    }

}

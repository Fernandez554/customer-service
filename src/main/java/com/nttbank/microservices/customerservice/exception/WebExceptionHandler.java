package com.nttbank.microservices.customerservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(-1) //Ordered.HIGHEST_PRECEDENCE
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                               ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse); //req -> this.renderErrorResponse(req)
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);

        if (error instanceof ConstraintViolationException violationException) {
            List<String> errors = violationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .bodyValue(errors);
        }
        if (error instanceof WebExchangeBindException bindException) {
            return handleValidationErrors(bindException);
        }

        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int statusCode = (int) errorAttributes.getOrDefault("status", 500);

        return ServerResponse.status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);

       /* Map<String, Object> generalError = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Map<String, Object> customError = new HashMap<>();

        //HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        String statusCode = String.valueOf(generalError.get("status"));
        Throwable error = getError(request); //Obtienes la excepcion de la peticion

        //switch enhanced
        switch (statusCode){
            case "400" -> {
                customError.put("message", error.getMessage());
                customError.put("status", 400);
                status = HttpStatus.BAD_REQUEST;
            }
            case "404" -> {
                customError.put("message", error.getMessage());
                customError.put("status", 404);
                status = HttpStatus.NOT_FOUND;
            }
            case "401" -> {
                customError.put("message", error.getMessage());
                customError.put("status", 401);
                status = HttpStatus.UNAUTHORIZED;
            }
            case "500" -> {
                customError.put("message", error.getMessage());
                customError.put("status", 500);
            }
        }
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));
*/


    }
    private Mono<ServerResponse> handleValidationErrors(WebExchangeBindException bindException) {
        List<String> errors = bindException.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation Error");
        response.put("message", "Invalid input data");
        response.put("errors", errors);

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}

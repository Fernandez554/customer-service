package com.nttbank.microservices.customerservice.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

  public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
      ApplicationContext applicationContext, ServerCodecConfigurer configure) {
    super(errorAttributes, resources, applicationContext);
    this.setMessageWriters(configure.getWriters());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(),
        this::renderErrorResponse); //req -> this.renderErrorResponse(req)
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
    Throwable error = getError(request);

    if (error instanceof WebExchangeBindException bindException) {
      return handleValidationErrors(bindException);
    }

    Map<String, Object> errorAttributes = getErrorAttributes(request,
        ErrorAttributeOptions.defaults());
    int statusCode = (int) errorAttributes.getOrDefault("status", 500);

    return ServerResponse.status(statusCode).contentType(MediaType.APPLICATION_JSON)
        .bodyValue(errorAttributes);

  }

  private Mono<ServerResponse> handleValidationErrors(WebExchangeBindException bindException) {
    List<String> errors = bindException.getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

    Map<String, Object> response = new HashMap<>();
    response.put("status", 400);
    response.put("error", "Validation Error");
    response.put("message", "Invalid input data");
    response.put("errors", errors);

    return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
  }
}

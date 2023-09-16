package ru.github.meperry.tms.api.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * @author Islam Khabibullin
 */
public abstract class BaseApi {

  protected WebClient webClient;

  public static final String AUTHORIZATION_HEADER = "Authorization";

  @Autowired
  public void setWebClient(WebClient webClient) {
    this.webClient = webClient;
  }

  protected <T, S> Mono<S> request(
      HttpMethod method,
      String uri,
      @Nullable String token,
      // TODO 16.09 возможно заменить на что-то другое в будущем
      @Nullable Mono<String> tokenSupplier,
      @Nullable T body,
      Class<S> responseClass
  ) {
    WebClient.RequestBodySpec request = getRequest(method, uri, token, body);

    return request.retrieve().bodyToMono(responseClass)
        .onErrorResume(
            WebClientResponseException.class,
            ex -> HttpStatus.FORBIDDEN.equals(ex.getStatusCode()) && tokenSupplier != null
                  // отправляем новый запрос с новым токеном
                  ? tokenSupplier.flatMap(newToken -> getRequest(method, uri, newToken, body).retrieve().bodyToMono(responseClass))
                  : Mono.error(ex)
        );
  }

  protected <T> Mono<ResponseEntity<Void>> request(
      HttpMethod method,
      String uri,
      @Nullable String token,
      @Nullable Mono<String> tokenSupplier,
      @Nullable T body
  ) {
    WebClient.RequestBodySpec request = getRequest(method, uri, token, body);

    return request.retrieve().toBodilessEntity()
        .onErrorResume(
            WebClientResponseException.class,
            ex -> HttpStatus.FORBIDDEN.equals(ex.getStatusCode()) && tokenSupplier != null
                  // отправляем новый запрос с новым токеном
                  ? tokenSupplier.flatMap(newToken -> getRequest(method, uri, newToken, body).retrieve().toBodilessEntity())
                  : Mono.error(ex)
        );
  }

  private <T> WebClient.RequestBodySpec getRequest(HttpMethod method, String uri,
      String token, T body) {
    WebClient.RequestBodySpec request = webClient.method(method).uri(uri);

    if (token != null) {
      request.header(AUTHORIZATION_HEADER, token);
    }

    if (body != null) {
      request.body(Mono.just(body), body.getClass());
    }
    return request;
  }
}

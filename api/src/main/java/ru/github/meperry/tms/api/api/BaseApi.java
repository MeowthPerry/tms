package ru.github.meperry.tms.api.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;
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
      @Nullable T body,
      Class<S> responseClass
  ) {
    WebClient.RequestBodySpec request = webClient.method(method).uri(uri);

    if (token != null) {
      request.header(AUTHORIZATION_HEADER, token);
    }

    if (body != null) {
      request.body(Mono.just(body), body.getClass());
    }

    return request.retrieve().bodyToMono(responseClass);
  }

  protected <T> Mono<ResponseEntity<Void>> request(
      HttpMethod method,
      String uri,
      @Nullable String token,
      @Nullable T body
  ) {
    WebClient.RequestBodySpec request = webClient.method(method).uri(uri);

    if (token != null) {
      request.header(AUTHORIZATION_HEADER, token);
    }

    if (body != null) {
      request.body(Mono.just(body), body.getClass());
    }

    return request.retrieve().toBodilessEntity();
  }
}

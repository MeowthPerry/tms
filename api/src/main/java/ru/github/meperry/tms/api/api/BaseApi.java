package ru.github.meperry.tms.api.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

  protected <T, S> Mono<S> request(HttpMethod method, String uri, T body, Class<S> responseClass) {
    return webClient.method(method)
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(body), body.getClass())
        .retrieve()
        .bodyToMono(responseClass);
  }

  protected <T, S> Mono<S> request(HttpMethod method, String uri, String token, T body, Class<S> responseClass) {
    return webClient.method(method)
        .uri(uri)
        .header(AUTHORIZATION_HEADER, token)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(body), body.getClass())
        .retrieve()
        .bodyToMono(responseClass);
  }
}

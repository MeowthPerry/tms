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

  @Autowired
  public void setWebClient(WebClient webClient) {
    this.webClient = webClient;
  }

  protected <T, S> Mono<S> request(HttpMethod method, T body, Class<S> responseClass) {
    return webClient.method(method)
        .uri("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(body), body.getClass())
        .retrieve()
        .bodyToMono(responseClass);
  }
}

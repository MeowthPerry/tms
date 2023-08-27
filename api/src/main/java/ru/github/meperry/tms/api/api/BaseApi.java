package ru.github.meperry.tms.api.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Islam Khabibullin
 */
public abstract class BaseApi {

  protected WebClient webClient;

  public BaseApi() {
  }

  @Autowired
  public void setWebClient(WebClient webClient) {
    this.webClient = webClient;
  }
}

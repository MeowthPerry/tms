package ru.github.meperry.tms.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Islam Khabibullin
 */
@Configuration                     // TODO 02.02 исправить .api.api
@ComponentScan({"ru.github.meperry.tms.api.api", "ru.github.meperry.tms.api.security.api"})
public class ApiConfig {

}
